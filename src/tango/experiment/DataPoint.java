package tango.experiment;

/**
 * @author Chantal Roth
 *
 * result of one experiment
 */
public class DataPoint {
   
    private long timestamp;
                
    private double angle_deg;
    
    private boolean detected;
    private int spin;
    

    public DataPoint(long timestamp, double angle_deg) {
        this.timestamp = timestamp;
        this.angle_deg = angle_deg;     
    }

  
    public double getAngleInDegrees() {
        return angle_deg;
    }
    public long getTimestamp() {
        return timestamp;
    }

    @Override
     public String toString() {
        return toStringBuilder().toString();
    }
    
    
     public StringBuilder toStringBuilder() {
        return new StringBuilder().append(timestamp).append(", ").append(detected).append(", ").append( angle_deg).append(", ").append(getSpin());
    }

    /**
     * @return the detected
     */
    public boolean isDetected() {
        return detected;
    }

    /**
     * @param detected the detected to set
     */
    public void setDetected(boolean detected) {
        this.detected = detected;
    }

    /**
     * @return the spin
     */
    public int getSpin() {
        return spin;
    }

    /**
     * @param spin the spin to set
     */
    public void setSpin(int spin) {
        this.spin = spin;
    }
}
