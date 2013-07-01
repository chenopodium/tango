package tango.experiment;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import tango.prefs.PreferenceManager;
import tango.utils.FileTools;

public class CorrelationResults {

    static Setup setup = Setup.getSetup();
    
    
    /** stores the neq and eq results. There is abucket for each of the angles such as  0, 45, 90, 135, 180, 225, 270, 360 
     * */
    double correl[][];
    static final int EQ = 1;
    static final int NEQ = 2;
    static final int TOTALNR = 0;
   
    private int total;
    private int nocoincidence;
    private int detected;
    
    public CorrelationResults() {        
        
    }
    
    public void computeCorrelations(Detector detectorA, Detector detectorB) {
        int buckets =  PreferenceManager.getManager().getNrAngles() ;
        correl = new double[buckets][6];
        
        ArrayList<DataPoint> dataA = detectorA.getResults();
        ArrayList<DataPoint> dataB = detectorB.getResults();
     
        for (int i = 0; i < dataA.size(); i++) {
            DataPoint a = dataA.get(i);
            DataPoint b = dataB.get(i);
            addCorrelation(a, b);
        }
    }
    
    
    /** compute raw product moment correlations */
    public double computeRawCorrelations(double angle_in_degrees) {
        if (angle_in_degrees == 360) {
            angle_in_degrees = 0;
        }
        int bucket = getBucket(angle_in_degrees);
        //well, eq are the number of ++ or -11... 
        double eq = getEq(bucket);
        double neq = getNeq(bucket);
        // the total (usually eq + neq)
        double tot = getTotal(bucket);
        double Rab = 0;
        // we don't want division by 0 :-)
        if (tot > 0) {
            Rab = (eq - neq) / (eq + neq);
        } else {
            //err("Not enough data points for " + angle_in_degrees+":"+tot);
        }

        return Rab;
    }

    /** Get number of same, which is PP and MM */
    public double getEq(int bucket) {
        double eq = correl[bucket][EQ];
        return eq;
    }

    /** Get number of unequal spins, which is MP and PM */
    public double getNeq(int bucket) {
        double Neq = correl[bucket][NEQ];
        return Neq;
    }

    /** Get number of unequal spins, which is MP and PM */
    public double getTotal(int bucket) {
        double tot = correl[bucket][TOTALNR];
        return tot;
    }

    /** Add data point consisting of angle between A and B and both spins */
    private void addCorrelation(DataPoint pointA, DataPoint pointB) {
        if (pointA.isDetected() && pointB.isDetected()) {
            double anglediff = pointA.getAngleInDegrees() - pointB.getAngleInDegrees();
            addCorrelation(anglediff, (int)pointA.getSpin(), (int)pointB.getSpin());
        }
    }
    private void addCorrelation(double deg, int spina, int spinb) {
        //	p("Adding data point:"+point);
        this.total++;
        this.detected++;
    
        deg = Math.abs(deg);
//        if (deg < 0) {
//            deg = deg + 360;
//        }
        // we store the result in buckets of 22.5 degrees - that is more than sufficient
        // but if desired we could of course store more (but typically the test angles are at least 22.5 or even 45
        // degrees apart
        int bucket = this.getBucket(deg);

        // one more measurement
        correl[bucket][TOTALNR]++;
      
        // we get a correlation if both results are the same, otherwise no correlation
        // AND WE MEASURE ALL DATA POINTS!
        double eq = spina == spinb ? 1 : 0;
        double neq = 1.0 - eq;

        // add the eq and neq to the table
        correl[bucket][EQ] += eq;
        correl[bucket][NEQ] += neq;
        
    }

    private void err(String msg) {
        System.err.println("CorrelationResult: " + msg);
    }

    @Override
    public String toString() {
        String res = "";
    
        res += toRawCorrelationString();
        res +=" \n";
        
        return res;
    }
    private String toRawCorrelationString() {
        StringBuilder s = new StringBuilder();
        
        s=s.append("\nRaw product moment correlation\n\nAngleAB (deg), Rab, eq, neq, total count\n");
        DecimalFormat f = new DecimalFormat("0.000");
        double delta = PreferenceManager.getManager().getAngleDelta();
        for (double deg = 0; deg < 360; deg += delta) {
            double rab = computeRawCorrelations(deg);
            int bucket = getBucket(deg);
            // nr of equal spins
            double eq = getEq(bucket);
            // nr of not equal spins
            double neq = getNeq(bucket);
            if (neq + eq > 0) {
                s = s.append(deg).append(", ").append(f.format(rab)).append(",  ").append( eq).append( ",  ").append( neq ).append(",  ").append( (neq + eq) ).append("\n");
            }
        }
        return s.toString();
    }

    /** returns x/y coordintaes for a plot to draw the correlations
     * [0] contains x (the angle in degrees)
     * [1] contains y (the rab value)
     * @return
     */
    public double[][] getCorrelations() {
        int nr = PreferenceManager.getManager().getNrAngles() ;
       // p("Got nr angles: "+nr);
        double xy[][] = new double[nr+1][2];
        int i = 0;
        double delta = PreferenceManager.getManager().getAngleDelta();
        //p("Got angle delta: "+delta);
        for (double deg = 0; deg <= 360; deg +=delta ) {
            double rab = computeRawCorrelations(deg);
            xy[i][0] = deg;
            xy[i][1] = rab;
            i++;
        }
        return xy;
    }

    /** Convert angle in degrees to bucket nr */
    public int getBucket(double angle_in_degrees) {
        // only positive buckets...
        angle_in_degrees = Math.abs(angle_in_degrees);
       
        angle_in_degrees = angle_in_degrees % 360;
        
        double delta = PreferenceManager.getManager().getAngleDelta();
        int bucket = (int) (angle_in_degrees / delta + 0.5) % correl.length;
                   
        return bucket;
    }

    private void p(String msg) {
        System.out.println("CorrelationResults: " + msg);

    }

    public void addNoCoincidence() {
        this.nocoincidence++;
        total++;
    }

    /**
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @return the undetected
     */
    public int getNoCoincidences() {
        return nocoincidence;
    }

    /**
     * @return the detected
     */
    public int getCoincidences() {
        return detected;
    }

   
    
    public void saveResults() {
          
        // now compute and print stats
        String res = toString();
        String filename = "correlations";
        String path = setup.prefs.getOutputFolder();        
        FileTools.writeStringToFile(new File(path+filename + ".csv"), res, false);
    }
}
