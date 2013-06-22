/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.bryansmodel;

import tango.defaultmodel.DefaultAngleGenerator;
import tango.defaultmodel.DefaultDetectorEfficiency;
import tango.defaultmodel.DefaultHiddenVariables;
import tango.defaultmodel.DefaultMeasurementFormula;
import tango.experiment.ExperimentModel;
import tango.experiment.HiddenVariablesIF;
import tango.experiment.Particle;
import tango.utils.RandomUtils;

/**
 *
 * @author Chantal
 */
public class BryansModel extends ExperimentModel{
    
      public BryansModel() {
        super("BRYAN", "Bryans model", "Bryans model based on 2D spin");
        this.setDetectorEfficiency(new DefaultDetectorEfficiency());
        
        this.setMeasurementFormula(new DefaultMeasurementFormula());
        this.setAngleGenerator(new DefaultAngleGenerator());
    }

    @Override
    public HiddenVariablesIF createHiddenVariables() {
        return new DefaultHiddenVariables();
    }
    
    @Override
    public void shareHiddenVariables(Particle pA, Particle pB) {
        // LHV are nP, nM, theta and phi
        double theta = RandomUtils.getRandomValue() * 180.0;
        int nP = RandomUtils.getRandomValue() > 0.5 ? 1 : -1;
        int nM = RandomUtils.getRandomValue() > 0.5 ? 1 : -1;
        int sign = RandomUtils.getRandomValue() > 0.5 ? 1 : -1;
        int signA = 1;
        double phi = 0;
        
        double[] varsA = {nP, nM, sign, signA, theta, phi};

        BryansHiddenVariables hiddenA = new BryansHiddenVariables();
        BryansHiddenVariables hiddenB = new BryansHiddenVariables();
        
        hiddenA.setVars(varsA);
        hiddenA.setTheta(theta);
        hiddenB.setTheta(theta);
        
        pA.setHiddenVars(hiddenA);
        pB.setHiddenVars(hiddenB);
        
        int nPb, nMb;

        // Keep trying random numbers until we reach satisfactory conditions
        if (nP == 1 && nM == 1) {
            do {
                nPb = RandomUtils.getRandomValue() > 0.5 ? 1 : -1;
                nMb = RandomUtils.getRandomValue() > 0.5 ? 1 : -1;
            } while ((nPb == 1 && nMb == 1) || (nPb == -1 && nMb == +1) || (nPb == +1 && nMb == -1));
        } else if (nP == 1 && nM == -1) {
            do {
                nPb = RandomUtils.getRandomValue() > 0.5 ? 1 : -1;
                nMb = RandomUtils.getRandomValue() > 0.5 ? 1 : -1;
            } while ((nPb == 1 && nMb == -1) || (nPb == -1 && nMb == -1) || (nPb == +1 && nMb == +1));
        } else if (nP == -1 && nM == 1) {
            do {
                nPb = RandomUtils.getRandomValue() > 0.5 ? 1 : -1;
                nMb = RandomUtils.getRandomValue() > 0.5 ? 1 : -1;
            } while ((nPb == -1 && nMb == 1) || (nPb == -1 && nMb == -1) || (nPb == 1 && nMb == 1));
        } else {
            do {
                nPb = RandomUtils.getRandomValue() > 0.5 ? 1 : -1;
                nMb = RandomUtils.getRandomValue() > 0.5 ? 1 : -1;
            } while ((nPb == -1 && nMb == -1) || (nPb == 1 && nMb == -1) || (nPb == -1 && nMb == +1));
        }


        double[] varsB = {nPb, nMb, sign, signA, theta, phi};
        hiddenB.setVars(varsB);
    }
}
