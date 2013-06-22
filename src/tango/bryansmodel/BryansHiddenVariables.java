/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.bryansmodel;

import tango.defaultmodel.DefaultHiddenVariables;

/**
 *
 * @author Chantal
 */
public class BryansHiddenVariables extends DefaultHiddenVariables {
    
    private double vars[];
            
    public BryansHiddenVariables() {
        super("BRYAN", "Bryans hidden vars", "Based on Bryans 2D spin model");
    }
    /**
     * @return the vars
     */
    public double[] getVars() {
        return vars;
    }

    /**
     * @param vars the vars to set
     */
    public void setVars(double[] vars) {
        this.vars = vars;
    }
    
    
    
}
