/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.mikesmodel;

import tango.experiment.AngleGeneratorIF;
import tango.models.ModelItem;
import tango.utils.RandomUtils;

/**
 *
 * @author Chantal
 */
public class MikesAngleGenerator extends ModelItem implements AngleGeneratorIF {
    
    public MikesAngleGenerator() {
        super("MIKE_GEN", "Mikes generator", "Generates random angles (steps of 45 degrees)");
    }
    
    @Override
    public AngleGeneratorIF createInstance(){
        return new MikesAngleGenerator();
    }
    @Override
    public double nextAngle() {       
       return  RandomUtils.randomAngleInDeg();                   
    }

}
