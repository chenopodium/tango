/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.random1;

import tango.models.def.*;
import tango.experiment.ExperimentModel;
import tango.experiment.HiddenVariablesIF;

/**
 *
 * @author Chantal
 */
public class Random1ExperimentModel extends ExperimentModel {
    
    public Random1ExperimentModel() {
        super("RANDOM1MODEL", "Random1 model", "Random settings");
        this.setDetectorEfficiency(new Random1DetectorEfficiency());        
        this.setMeasurementFormula(new Random1MeasurementFormula());
        this.setAngleGenerator(new Random1AngleGenerator());
    }

    @Override
    public HiddenVariablesIF createHiddenVariables() {
         HiddenVariablesIF vars = new Random1HiddenVariables();
         return vars;
    }
}
