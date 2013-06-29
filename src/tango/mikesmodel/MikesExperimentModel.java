/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.mikesmodel;

import tango.experiment.ExperimentModel;
import tango.experiment.HiddenVariablesIF;

/**
 *
 * @author Chantal
 */
public class MikesExperimentModel extends ExperimentModel {
    
    public MikesExperimentModel() {
        super("MIKES_MODEL", "Mikes model", "Mikes settings");
        this.setDetectorEfficiency(new MikesDetectorEfficiency());        
        this.setMeasurementFormula(new MikesMeasurementFormula());
        this.setAngleGenerator(new MikesAngleGenerator());
    }

    @Override
    public HiddenVariablesIF createHiddenVariables() {
         HiddenVariablesIF vars = new MikesHiddenVariables();
         return vars;
    }
}
