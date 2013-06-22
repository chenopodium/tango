/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.defaultmodel;

import tango.experiment.ExperimentModel;
import tango.experiment.HiddenVariablesIF;

/**
 *
 * @author Chantal
 */
public class DefaultExperimentModel extends ExperimentModel {
    
    public DefaultExperimentModel() {
        super("DEFAULT", "Default model", "Default settings");
        this.setDetectorEfficiency(new DefaultDetectorEfficiency());        
        this.setMeasurementFormula(new DefaultMeasurementFormula());
        this.setAngleGenerator(new DefaultAngleGenerator());
    }

    @Override
    public HiddenVariablesIF createHiddenVariables() {
         HiddenVariablesIF vars = new DefaultHiddenVariables();
         return vars;
    }
}
