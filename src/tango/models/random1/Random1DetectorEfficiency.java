/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.random1;

import tango.models.def.*;
import tango.experiment.Detector;
import tango.experiment.DetectorEfficiencyIF;
import tango.models.ModelItem;
import tango.experiment.Particle;
import tango.utils.RandomUtils;

/**
 *
 * @author Chantal
 */
public class Random1DetectorEfficiency extends ModelItem implements DetectorEfficiencyIF {
    

     public Random1DetectorEfficiency(String key, String name, String desc) {
        super(key, name, desc);
    }
     
    public Random1DetectorEfficiency() {
        super("RANDOM1EFF", "Random detection", "Detects random events");
    }
        
      @Override
    public DetectorEfficiencyIF createInstanc() {
        return new Random1DetectorEfficiency();
    }
    @Override
    public boolean isDetected(Particle particle, Detector detector) {
        return RandomUtils.getRandomValue()>0.5;
    }
    
}
