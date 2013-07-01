/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.mike;

import tango.models.def.DefaultHiddenVariables;

/**
 *
 * @author Chantal
 */
public class MikesHiddenVariables extends DefaultHiddenVariables {

    private int direction;
    private double theta;
    private double randPM;
    private double randMP;
   
    public MikesHiddenVariables() {
         super("MikesHiddenVariables", "Mike's hidden variables", "Rewrite of Bryan's hidden variable simulation");
    }

    public int getDirection() {
        return direction;
    }
    
    @Override
    public double getTheta() {
        return theta;
    }

    public double getRandPM() {
        return randPM;
    }

    public double getRandMP() {
        return randMP;
    }

    public void setDirection(int direction) {
        this.direction=direction;
    }
    
    @Override
    public void setTheta(double theta) {
        this.theta=theta;
    }

    public void setRandPM(double randPM) {
        this.randPM=randPM;
    }

    public void setRandMP(double randMP) {
        this.randMP=randMP;
    }
}
