/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.defaultmodel;

import tango.experiment.AngleGeneratorIF;
import tango.experiment.ModelItem;
import tango.utils.RandomUtils;

/**
 *
 * @author Chantal
 */
public class DefaultAngleGenerator extends ModelItem implements AngleGeneratorIF {
    
    public DefaultAngleGenerator() {
        super("DEFAULT", "Default generator", "Generates random angles (steps of 45 degrees)");
    }
    
    @Override
    public AngleGeneratorIF createInstance(){
        return new DefaultAngleGenerator();
    }
    @Override
    public double nextAngle() {       
       return  RandomUtils.randomAngleInDeg();                   
    }

}
