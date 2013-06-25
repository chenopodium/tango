/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.results;

import java.util.logging.Level;
import java.util.logging.Logger;
import tango.experiment.CorrelationResults;

/**
 *
 * @author Chantal
 */
public abstract class AbstractResult {

    protected CorrelationResults corr;
    private String name;
    private String description;

    public AbstractResult(CorrelationResults corr) {
        this.corr = corr;
    }
    public String getId() {
        return name;
    }

    public double computeRawCorrelations(double angle_deg) {
        return corr.computeRawCorrelations(angle_deg);
    }

    public abstract double[] redAngles();

    public abstract double[] greenAngles();

    public abstract String getStringResult();
    
    public boolean hasRedAngle(double angle_deg) {
        return hasAngle(angle_deg, redAngles());
    }
    
    public double getEq(double angle_deg) {
        int bucket = corr.getBucket(angle_deg);
        double res = corr.getEq(bucket);
        p("getEq("+angle_deg+ ") -> b="+bucket+", value="+res);
        return res;
    }
    public double getNeq(double angle_deg) {
        int bucket = corr.getBucket(angle_deg);
        double res = corr.getNeq(bucket);
        p("getEq("+angle_deg+ ") -> b="+bucket+", value="+res);
        return res;
    }
    @Override
    public String toString() {
        String s = name+","+description+"\n\n";
        s += this.getStringResult();
        return s;
    }

    public boolean hasGreenAngle(double angle_deg) {
        return hasAngle(angle_deg, greenAngles());
    }

    public boolean hasAngle(double angle_deg, double angles[]) {

        for (double d : angles) {
            if (d == angle_deg) {
                return true;
            }
        }
        return false;
    }

    public double computeQmValue(double angle_deg) {
        return computeValueForAngle(angle_deg);
    }
    public abstract double computeValue();

    public abstract double computeValueForAngle(double angle_deg);

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
     private static void p(String msg) {
        Logger.getLogger(AbstractResult.class.getName()).log(Level.INFO, msg);
    }
}
