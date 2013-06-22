/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.chantalsmodel;

import tango.defaultmodel.DefaultHiddenVariables;
import tango.experiment.Detector;
import tango.experiment.MeasurementFormulaIF;
import tango.experiment.ModelItem;
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
        double delta = theta+ detector.getAngleInDegrees();
        int spin = (int) Math.signum(Math.sin(delta));     
        return spin;
     }

}
