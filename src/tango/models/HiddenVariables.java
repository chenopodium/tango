/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models;

import tango.experiment.HiddenVariablesIF;
import tango.experiment.Particle;
import tango.utils.RandomUtils;

/**
 *
 * @author Chantal
 */
public  class HiddenVariables extends ModelItem implements HiddenVariablesIF{
  
    public HiddenVariables(String key, String name, String desc) {
         super(key, name, desc);
    }
    
     @Override
    public void generateHiddenVariables(Particle pA) {
        // default does nothing
    }

}
