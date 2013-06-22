/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.chantalsmodel;

import tango.defaultmodel.DefaultHiddenVariables;
import tango.dummymodel.DummyDetectorEfficiency;
import tango.experiment.Detector;
import tango.experiment.DetectorEfficiencyIF;
import tango.experiment.ModelItem;
import tango.experiment.Particle;

/**
 *
 * @author Chantal
 */
public class ChantalsDetectorEfficiency extends ModelItem implements DetectorEfficiencyIF{
    
    
    public ChantalsDetectorEfficiency() {
         super("CHANTAL", "Chantals efficiency", "Does not detect some events when the angle is close to 0");
    }
     @Override
    public DetectorEfficiencyIF createInstanc() {
        return new ChantalsDetectorEfficiency();
    }
     @Override
    public boolean isDetected(Particle particle, Detector detector) {
        DefaultHiddenVariables vars = (DefaultHiddenVariables) particle.getHiddenVars();
        double theta = vars.getTheta();                
        double pdetect = 2.3*Math.abs(Math.sin(theta + detector.getAngleInDegrees()));        
        if (Math.random()<=pdetect) return true;
        else return false;
     }

}
