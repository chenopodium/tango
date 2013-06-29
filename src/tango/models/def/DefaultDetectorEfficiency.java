/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.def;

import tango.experiment.Detector;
import tango.experiment.DetectorEfficiencyIF;
import tango.models.ModelItem;
import tango.experiment.Particle;

/**
 *
 * @author Chantal
 */
public class DefaultDetectorEfficiency extends ModelItem implements DetectorEfficiencyIF {
    

     public DefaultDetectorEfficiency(String key, String name, String desc) {
        super(key, name, desc);
    }
     
    public DefaultDetectorEfficiency() {
        super("DEFAULT", "Default 100% efficiency", "Detects all events");
    }
        
      @Override
    public DetectorEfficiencyIF createInstanc() {
        return new DefaultDetectorEfficiency();
    }
    @Override
    public boolean isDetected(Particle particle, Detector detector) {
        return true;
    }
    
}
