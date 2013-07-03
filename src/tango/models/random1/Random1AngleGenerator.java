/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.random1;

import tango.models.def.*;
import tango.experiment.AngleGeneratorIF;
import tango.models.ModelItem;
import tango.utils.RandomUtils;

/**
 *
 * @author Chantal
 */
public class Random1AngleGenerator extends ModelItem implements AngleGeneratorIF {
    
    public Random1AngleGenerator() {
        super("RANDOM1GEN", "Default generator", "Generates random angles (steps of 45 degrees)");
    }
    
    @Override
    public AngleGeneratorIF createInstance(){
        return new Random1AngleGenerator();
    }
    @Override
    public double nextAngle() {       
       return  RandomUtils.randomAngleInDeg();                   
    }

}
