/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.mikesmodel;

import tango.models.def.DefaultDetectorEfficiency;
import tango.experiment.Detector;
import tango.experiment.DetectorEfficiencyIF;
import tango.models.ModelItem;
import tango.experiment.Particle;

/**
 *
 * @author Chantal
 */
public class MikesDetectorEfficiency extends DefaultDetectorEfficiency {
    

    public MikesDetectorEfficiency() {
        super("MIKE", "Default 100% efficiency", "Detects all events");
    }
        
      @Override
    public DetectorEfficiencyIF createInstanc() {
        return new MikesDetectorEfficiency();
    }
    @Override
    public boolean isDetected(Particle particle, Detector detector) {
        return true;
    }
    
}
