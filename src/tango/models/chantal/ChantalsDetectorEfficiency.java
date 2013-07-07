/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.chantal;

import tango.models.def.DefaultHiddenVariables;
import tango.experiment.Detector;
import tango.experiment.DetectorEfficiencyIF;
import tango.models.ModelItem;
import tango.experiment.Particle;

/**
 *
 * @author Chantal
 */
public class ChantalsDetectorEfficiency extends ModelItem implements DetectorEfficiencyIF{
    
    
    public ChantalsDetectorEfficiency() {
         super("CHANTAL", "Chantals efficiency", "Does not detect some events when the angle is close to 0 (sin(alpha))");
    }
     @Override
    public DetectorEfficiencyIF createInstanc() {
        return new ChantalsDetectorEfficiency();
    }
     @Override
    public boolean isDetected(Particle particle, Detector detector) {
        DefaultHiddenVariables vars = (DefaultHiddenVariables) particle.getHiddenVars();
        double theta = vars.getTheta();      
        double pdetect = Math.abs(Math.sin(Math.toRadians(theta + detector.getAngleInDegrees())));        
        if (Math.random()<=pdetect) return true;
        else return false;
     }

     /*
      *  
        int spin = (int) Math.signum(Math.sin(theta+ filter_angle));                
        double pdetect = 2.3*Math.abs(Math.sin(theta + filter_angle));
        
        if (Math.random()<=pdetect) return spin;
        else return Integer.MIN_VALUE;
      */
}
