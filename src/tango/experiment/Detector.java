package tango.experiment;

import java.util.ArrayList;

/**
 * @author Chantal Roth 
 */
public class Detector {

    private String name;
    
    private ArrayList<DataPoint> results;
    
    private DataPoint current_result;
   
    private AngleGeneratorIF angleGenerator;
    private DetectorEfficiencyIF detectorEfficiency;
    private MeasurementFormulaIF measurementFormula;
   
    private ExperimentModel model;

    public Detector(String name) {
        this.name = name;
    }
    public ArrayList<DataPoint> getResults() {
        return results;
    }
   
    public String getResultsAsCsv() {
        String nl = "\n";
        String s = "Results of detector "+this.name+nl;
        s += "particle nr, timestamp, detected, detector angle, spin"+nl;
        if (results == null) {
            s="No results yet";
            return s;
        }
        int count = 0;
        for (DataPoint dp: results) {
            count++;
            s += count+", "+dp.toString()+nl;
        }
        return s;
    }
    public int getNrResults() {
        if (results == null) return 0;
        return results.size();
    }
    public int getNrDetected() {
        if (results == null) return 0;
        int count = 0;
        int nr = results.size();
        for (int i = 0; i < nr; i++) {
            DataPoint dp = results.get(i);
            if (dp.isDetected()) count++;
        }
        return count;
    }
    public int getNrUndetected() {
        return getNrResults() - getNrDetected();
    }
    public double getPercentDetected() {
        double tot = this.getNrResults();
        double det = this.getNrDetected();
        if (tot > 0) {
            return det*100.0/tot;
        }
        else return 0;
    }
    public void restartExperiment() {
        results = new  ArrayList<DataPoint>();
        nextAngle();
    }

    public void recordResult() {
        if (results == null) results = new  ArrayList<DataPoint>();
        if (current_result != null) results.add(current_result);
    }
    public void nextAngle(){   
        if (getAngleGenerator() == null) {
            setAngleGenerator(getModel().getAngleGenerator().createInstance());
            
        }
        double current_angle_deg = getAngleGenerator().nextAngle();        
        current_result = new DataPoint(System.currentTimeMillis(), current_angle_deg);
    }

    public double getAngleInDegrees() {
        return current_result.getAngleInDegrees();
    }
  
    @Override
    public String toString() {
        return "Detector " + name + ", current angle: " + current_result.getAngleInDegrees();
    }

    /*
     * Compute if the result is + or - 1 based on the particle and the angle of
     * the filter
     */
    public int measure(Particle particle) {
        
        if (this.getDetectorEfficiency() == null) {
            setDetectorEfficiency(getModel().getDetectorEfficiency().createInstanc());
        }
        
        if (this.getMeasurementFormula() == null) {
            setMeasurementFormula(getModel().getMeasurementFormula());
        }
        boolean detected = getDetectorEfficiency().isDetected(particle, this);
        current_result.setDetected(detected);        
        int spin =  getMeasurementFormula().measure(this, particle);
        current_result.setSpin(spin);;
        return spin;
    }  

    
    /**
     * @param model the model to set
     */
    public void setModel(ExperimentModel model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void resetData() {
        this.results = null;
    }
    public void resetModel() {
        resetData();
        this.setAngleGenerator(null);
        this.setDetectorEfficiency(null);
        this.setMeasurementFormula(null);
    }

    /**
     * @return the angleGenerator
     */
    public AngleGeneratorIF getAngleGenerator() {
        if (angleGenerator == null) angleGenerator = getModel().getAngleGenerator().createInstance();
        return angleGenerator;
    }

    /**
     * @param angleGenerator the angleGenerator to set
     */
    public void setAngleGenerator(AngleGeneratorIF angleGenerator) {
        this.angleGenerator = angleGenerator;
    }

    /**
     * @return the detectorEfficiency
     */
    public DetectorEfficiencyIF getDetectorEfficiency() {
        if (detectorEfficiency == null) detectorEfficiency = getModel().getDetectorEfficiency().createInstanc();
        return detectorEfficiency;
    }

    /**
     * @param detectorEfficiency the detectorEfficiency to set
     */
    public void setDetectorEfficiency(DetectorEfficiencyIF detectorEfficiency) {
        this.detectorEfficiency = detectorEfficiency;
    }

    /**
     * @return the model
     */
    public ExperimentModel getModel() {
        return model;
    }

    /**
     * @return the measurementFormula
     */
    public MeasurementFormulaIF getMeasurementFormula() {
        if (measurementFormula == null) measurementFormula = getModel().getMeasurementFormula();
        return measurementFormula;
    }

    /**
     * @param measurementFormula the measurementFormula to set
     */
    public void setMeasurementFormula(MeasurementFormulaIF measurementFormula) {
        this.measurementFormula = measurementFormula;
    }
}
