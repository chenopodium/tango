/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.random1;

import tango.models.def.*;
import tango.experiment.Particle;
import tango.models.HiddenVariables;
import tango.utils.RandomUtils;

/**
 *
 * @author Chantal
 */
public class Random1HiddenVariables extends HiddenVariables {

   
    public Random1HiddenVariables(String key, String name, String desc) {
         super(key, name, desc);
    }
    public Random1HiddenVariables() {
         super("RANDOM1HV", "Random hidden vars", "Does nothing");
    }

    @Override
    public void generateHiddenVariables(Particle pA) {
       // does nothing
    }
   
 
}
