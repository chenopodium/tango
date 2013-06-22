/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.defaultmodel;

import tango.experiment.Detector;
import tango.experiment.DetectorEfficiencyIF;
import tango.experiment.ModelItem;
import tango.experiment.Particle;

/**
 *
 * @author Chantal
 */
public class DefaultDetectorEfficiency extends ModelItem implements DetectorEfficiencyIF {
    

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
