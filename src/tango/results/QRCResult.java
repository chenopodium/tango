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
public class QRCResult extends AbstractResult {

    static final double A1 = 0;
    static final double B1 = 0;
    static final double A2 = 67.5;
    static final double B2 = 22.5;
    double d0 = 0;
    double d1 = 22.5;
    double d2 = 45;
    double d3 = 67.5;
    
    double N0;
    double N1;
    double N2;
    double N3;

    public QRCResult(CorrelationResults corr) {
        super(corr);
        setName("QRC result");
        setDescription("Quantum Randy Challenge, N1 - N2 - N3");
    }

    @Override
    public double computeValue() {
        N0 = getEq(d0);
        N1 = getNeq(d1);
        N2 = getEq(d2);
        N3 = getNeq(d3);

        double res = (N1 - N2 - N3);
        return res;
    } 

    @Override
    public String getStringResult() {
        computeValue();
        DecimalFormat f = new DecimalFormat("0");
        String res="Classical result: N1 (U)> N2 (E)+ N3 (U) \n\n";
        res += "N0(E)("+d0+") =  " + f.format(N0) + "\n";
        res += "N1(U)("+d1+") = " + f.format(N1) + "\n";
        res += "N2(E)("+d2+") = " + f.format(N2) + "\n";
        res += "N3(U)("+d3+") = " + f.format(N3) + "\n\n";    
        
        boolean classical = (N1 <= (N2 + N3)) ;
        String eq = classical? "<= " : ">";
        res+=f.format(N1)+" "+ eq+" "+f.format(N2)+ "+"+f.format(N3)+ " (="+f.format(N2+N3)+") = > ";
        res+= classical ? "classical result" : "QM result";

        return res;

    }
    @Override
    public String getId() {
        return "QRC";
    }

    @Override
    public double[] redAngles() {
        double[] res = {d1, d3};
        return res;
    }

    @Override
    public double[] greenAngles() {
        double[] res = {d0, d2};
        return res;
    }

    @Override
    public double computeValueForAngle(double angle_deg) {
        double y = Math.cos(Math.toRadians(angle_deg));
        return y;
    }
}
