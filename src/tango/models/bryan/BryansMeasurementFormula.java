/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.bryan;

import javax.swing.JOptionPane;
import tango.experiment.Detector;
import tango.experiment.MeasurementFormulaIF;
import tango.models.ModelItem;
import tango.experiment.Particle;
import tango.utils.RandomUtils;

/**
 *
 * @author Chantal
 */
public class BryansMeasurementFormula extends ModelItem implements MeasurementFormulaIF {

    private boolean errorshown;

    public BryansMeasurementFormula() {
        super("BRYAN", "Bryans formula", "Based on the 2D spin model");
    }

    public void check() {
       JOptionPane.showMessageDialog(null, "Bryans Measurement also requires Bryans Hidden Variables :-). I suggest you just pick Bryans model with the LHV button");
            
    }

    @Override
    public int measure(Detector detector, Particle particle) {

        if (!(particle.getHiddenVars() instanceof BryansHiddenVariables)) {
            if (!errorshown) {
                JOptionPane.showMessageDialog(null, "Bryans Measurement also requires Bryans Hidden Variables :-). Can you please pick the model with the LHV button?");
            }
            errorshown = true;
            return 0;
        }
        BryansHiddenVariables hiddenVars = (BryansHiddenVariables) particle.getHiddenVars();
        double vars[] = hiddenVars.getVars();

        double filter_angle_deg = detector.getAngleInDegrees();

        double nP = vars[0];
        double nM = vars[1];
        double theta = vars[4];
        double phi = vars[5];

        // The first backup
        //radF is the polarization angle
        double radF = Math.toRadians(filter_angle_deg);
        double radA = Math.toRadians(theta);

        double phiRad = Math.toRadians(phi);

        double DA1 = Math.pow(Math.cos((phiRad) / 2.), 2);
        double DA2 = Math.pow(Math.sin((phiRad) / 2.), 2);
        double D1 = Math.cos(radF - radA) + Math.sin(radF - radA);
        double D2 = Math.cos(radF + radA) - Math.sin(radF + radA);
        double D3 = Math.cos(radF - radA) - Math.sin(radF - radA);
        double D4 = Math.cos(radF + radA) + Math.sin(radF + radA);

        double aPM = (D1 * DA1 + D2 * DA2) / 1.41421356;
        double aMP = (D3 * DA1 + D4 * DA2) / 1.41421356;

        // For the second bucket
        double radFShift = Math.toRadians(filter_angle_deg + 90);

        double D1Shift = Math.cos(radFShift - radA) + Math.sin(radFShift - radA);
        double D2Shift = Math.cos(radFShift + radA) - Math.sin(radFShift + radA);
        double D3Shift = Math.cos(radFShift - radA) - Math.sin(radFShift - radA);
        double D4Shift = Math.cos(radFShift + radA) + Math.sin(radFShift + radA);

        double aPMShift = (D1Shift * DA1 + D2Shift * DA2) / 1.41421356;
        double aMPShift = (D3Shift * DA1 + D4Shift * DA2) / 1.41421356;

        // Used in calculating the return values (1 or 0) for each bucket
        double probPM = 0.0;
        double probMP = 0.0;


        if (nP == 1. && nM == 1.) {
            probPM = 0.5 * (1 + aMP);
            probMP = 0.5 * (1 - aPMShift);
        } else if (nP == 1. && nM == -1.) {
            probPM = 0.5 * (1 + aPM);
            probMP = 0.5 * (1 - aMPShift);
        } else if (nP == -1. && nM == 1.) {
            probPM = 0.5 * (1 - aPM);
            probMP = 0.5 * (1 + aMPShift);
        } else if (nP == -1. && nM == -1.) {
            probPM = 0.5 * (1 - aMP);
            probMP = 0.5 * (1 + aPMShift);
        }

        int[] hits = new int[2];
        hits[0] = RandomUtils.getRandomValue() > probPM ? 1 : 0;
        hits[1] = RandomUtils.getRandomValue() > probMP ? 1 : 0;
        return hits[0];
    }
}
