/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.defaultmodel;

import tango.experiment.Detector;
import tango.experiment.MeasurementFormulaIF;
import tango.experiment.ModelItem;
import tango.experiment.Particle;

/**
 *
 * @author Chantal
 */
public class DefaultMeasurementFormula extends ModelItem implements MeasurementFormulaIF{

    public DefaultMeasurementFormula() {
      super("DEFAULT", "Default formula", "Spin is sign (sin(angle))");
    }
   
    @Override
     public int measure(Detector detector, Particle particle) {   
        DefaultHiddenVariables vars = (DefaultHiddenVariables) particle.getHiddenVars();
        double theta = vars.getTheta();
        
        int spin = (int) Math.signum(Math.sin(theta+ detector.getAngleInDegrees()));                
        
        return spin;
     }

   
}