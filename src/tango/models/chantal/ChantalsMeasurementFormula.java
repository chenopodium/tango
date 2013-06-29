/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.chantal;

import tango.models.def.DefaultHiddenVariables;
import tango.experiment.Detector;
import tango.experiment.MeasurementFormulaIF;
import tango.models.ModelItem;
import tango.experiment.Particle;

/**
 *
 * @author Chantal
 */
public class ChantalsMeasurementFormula extends ModelItem implements MeasurementFormulaIF {
    
     
    public ChantalsMeasurementFormula(){ 
      super("CHANTAL", "Chantals formula", "Spin is sign(sin(angle))");
    }
    
    
    @Override
     public int measure(Detector detector, Particle particle) {   
        DefaultHiddenVariables vars = (DefaultHiddenVariables) particle.getHiddenVars();
        double theta = vars.getTheta();
         if (particle.isB()) theta = theta + 180;
        double delta = theta+ detector.getAngleInDegrees();
        int spin = (int) Math.signum(-Math.sin(Math.toRadians(delta)));     
        return spin;
     }

}
