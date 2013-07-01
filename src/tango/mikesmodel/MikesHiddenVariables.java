/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.mikesmodel;

import tango.models.HiddenVariables;


/**
 *
 * @author Chantal
 */
public class MikesHiddenVariables extends HiddenVariables {

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
        this.direction = direction;
    }

    
    public void setTheta(double theta) {
        this.theta = theta;
    }

    public void setRandPM(double randPM) {
        this.randPM = randPM;
    }

    public void setRandMP(double randMP) {
        this.randMP = randMP;
    }
}
