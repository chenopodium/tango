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
public interface AngleGeneratorIF  extends ModelItemIF  {
    public double nextAngle();
    
    public AngleGeneratorIF createInstance();
}
