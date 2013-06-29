/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.experiment;

import tango.models.ModelItemIF;

/**
 *
 * @author Chantal
 */
public interface DetectorEfficiencyIF extends ModelItemIF {
    
        /** Determines if this particle is detected or not for a given detector angle */
        public boolean isDetected(Particle particle, Detector detector);
        
        public DetectorEfficiencyIF createInstanc();
}
