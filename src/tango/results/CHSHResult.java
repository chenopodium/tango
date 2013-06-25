/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.results;

import java.text.DecimalFormat;
import tango.experiment.CorrelationResults;
import tango.experiment.Setup;

/**
 *
 * @author Chantal
 */
public class CHSHResult extends AbstractResult {

    public static double A1 = 0;
    public static double A2 = 90;
    public static double B1 = 45;
    public static double B2 = 135;
    
    double a1b1;
    double a1b2;
    double a2b1;
    double a2b2;

    
    public CHSHResult(CorrelationResults corr) {
        super(corr);
        setName("CHSH result");
        setDescription("Classical maximum is 2.0, QM result is 2.8");
        a1b1 = Math.abs(A1 - B1);
        a1b2 = Math.abs(A1 - B2);
        a2b1 = Math.abs(A2 - B1);
        a2b2 = Math.abs(A2 - B2);
    }
    public String getId() {
        return "CHSH";
    }

    @Override
    public double computeValue() {
        double RA1B1 = computeRawCorrelations(Math.abs(A1 - B1));
        double RA1B2 = computeRawCorrelations(Math.abs(A1 - B2));
        double RA2B1 = computeRawCorrelations(Math.abs(A2 - B1));
        double RA2B2 = computeRawCorrelations(Math.abs(A2 - B2));

        double chsh = Math.abs(RA1B1 - RA1B2 + RA2B1 + RA2B2);
        return chsh;
    }

    public String getStringResult() {
        double RA1B1 = computeRawCorrelations(a1b1);
        double RA1B2 = computeRawCorrelations(a1b2);
        double RA2B1 = computeRawCorrelations(a2b1);
        double RA2B2 = computeRawCorrelations(a2b2);

        double qA1B1 = computeQmValue(a1b1);
        double qA1B2 = computeQmValue(a1b2);
        double qA2B1 = computeQmValue(a2b1);
        double qA2B2 = computeQmValue(a2b2);
        
        double chsh = Math.abs(RA1B1 - RA1B2 + RA2B1 + RA2B2);

        DecimalFormat f = new DecimalFormat("0.00");
        String res = "Combination, dAngle, QM, RA1B1 \n";
        res += "A1B1, " + a1b1 + ", "+f.format(qA1B1)+" , " + f.format(RA1B1) + "\n";
        res += "A1B2, " + a1b2 + ", "+f.format(qA1B2)+ ", " + f.format(RA1B2) + "\n";
        res += "A2B1, " + a2b1 + ", "+f.format(qA2B1)+", " + f.format(RA2B1) + "\n";
        res += "A2B2, " + a2b2 + ", "+f.format(qA2B2)+" , " + f.format(RA2B2) + "\n";

        
        res += "|R(A1B1) - R(A1B2) + R(A2B1) + R(A2B2)|,   " + f.format(chsh);        
        return res;
    
    }
    public double[] redAngles() {
        double[] res = {a1b2};
        return res;
    }

    @Override
    public double[] greenAngles() {
        double[] res = {a1b1, a2b1, a2b2};
        return res;
    }

    @Override
    public double computeValueForAngle(double angle_deg) {
        double y = -Math.cos(Math.toRadians(angle_deg));
        return y;
    }
}
