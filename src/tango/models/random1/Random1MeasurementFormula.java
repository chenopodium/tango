/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.random1;

import tango.models.def.*;
import tango.experiment.Detector;
import tango.experiment.MeasurementFormulaIF;
import tango.models.ModelItem;
import tango.experiment.Particle;
import tango.utils.RandomUtils;

/**
 *
 * @author Chantal
 */
public class Random1MeasurementFormula extends ModelItem implements MeasurementFormulaIF{

    public Random1MeasurementFormula() {
      super("RANDOM1MODEL", "Random formula", "Return random value");
    }
   
    @Override
     public int measure(Detector detector, Particle particle) {   
        return RandomUtils.getRandomValue() > 0.5 ? 1: 0;
     }

   
}
