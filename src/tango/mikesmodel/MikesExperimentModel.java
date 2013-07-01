/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.mikesmodel;

import tango.experiment.ExperimentModel;
import tango.experiment.HiddenVariablesIF;
import tango.experiment.Particle;
import tango.utils.RandomUtils;

/**
 *
 * @author Chantal
 */
public class MikesExperimentModel extends ExperimentModel {
  public MikesExperimentModel() {
        super("MikesExperimentalModel", "Mike's model (rewrite of Bryan's model)", "Mike's settings");
        this.setDetectorEfficiency(new MikesDetectorEfficiency());        
        this.setMeasurementFormula(new MikesMeasurementFormula());
        this.setAngleGenerator(new MikesAngleGenerator());
    }

    @Override
    public HiddenVariablesIF createHiddenVariables() {
        HiddenVariablesIF vars = new MikesHiddenVariables();
        return vars;
    }

    @Override
    public void shareHiddenVariables(Particle pA, Particle pB) {

        MikesHiddenVariables hiddenA = new MikesHiddenVariables();
        MikesHiddenVariables hiddenB = new MikesHiddenVariables();

        hiddenA.setDirection(+1);
        hiddenA.setTheta(RandomUtils.randomAngleInDeg());
        hiddenA.setRandPM(RandomUtils.getRandomValue());
        hiddenA.setRandMP(RandomUtils.getRandomValue());

        hiddenB.setDirection(-hiddenA.getDirection());   // negated (particle B moves in opposite direction of A)
        hiddenB.setTheta(hiddenA.getTheta());            // same as Alice
        hiddenB.setRandPM(hiddenA.getRandMP());          // swapped to avoid new random numbers
        hiddenB.setRandMP(hiddenA.getRandPM());          // swapped to avoid new random numbers

        pA.setHiddenVars(hiddenA);
        pB.setHiddenVars(hiddenB);
    }
}
