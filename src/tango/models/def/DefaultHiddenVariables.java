/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.def;

import tango.experiment.Particle;
import tango.experiment.HiddenVariablesIF;
import tango.models.ModelItem;
import tango.utils.RandomUtils;

/**
 *
 * @author Chantal
 */
public class DefaultHiddenVariables extends ModelItem implements HiddenVariablesIF {

    private double theta;

    public DefaultHiddenVariables(String key, String name, String desc) {
         super(key, name, desc);
    }
    public DefaultHiddenVariables() {
         super("DEFAULT", "Default hidden vars", "Just contains orientation of particle (theta)");
    }

    @Override
    public void generateHiddenVariables(Particle pA) {
        theta = RandomUtils.getRandomValue() * 180.0;
    }
   
    /**
     * @return the theta
     */
    public double getTheta() {
        return theta;
    }

    /**
     * @param theta the theta to set
     */
    public void setTheta(double theta) {
        this.theta = theta;
    }
}
