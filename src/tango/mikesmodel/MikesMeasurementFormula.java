/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.mikesmodel;

import tango.models.def.DefaultHiddenVariables;
import tango.experiment.Detector;
import tango.experiment.MeasurementFormulaIF;
import tango.models.ModelItem;
import tango.experiment.Particle;

/**
 *
 * @author Chantal
 */
public class MikesMeasurementFormula extends ModelItem implements MeasurementFormulaIF{

    public MikesMeasurementFormula() {
      super("MIKES_FORMULA", "Mikes formula", "Spin is sign (sin(angle))");
    }
   
    @Override
     public int measure(Detector detector, Particle particle) {   
        // might need to cast to MikesFormula if you are using more variables!
        DefaultHiddenVariables vars = (DefaultHiddenVariables) particle.getHiddenVars();
        double theta = vars.getTheta();
        
        if (particle.isB()) theta = theta + 180;
        
        double d = theta+ detector.getAngleInDegrees();
        
        int spin = -(int) Math.signum(Math.sin(Math.toRadians(d)));                
        
        return spin;
     }

   
}
