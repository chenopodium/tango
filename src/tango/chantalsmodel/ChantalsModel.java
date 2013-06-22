/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.chantalsmodel;

import tango.defaultmodel.DefaultAngleGenerator;
import tango.defaultmodel.DefaultHiddenVariables;
import tango.defaultmodel.DefaultMeasurementFormula;
import tango.experiment.ExperimentModel;
import tango.experiment.HiddenVariablesIF;

/**
 *
 * @author Chantal
 */
public class ChantalsModel extends ExperimentModel {
    
     public ChantalsModel() {
        super("CHANTAL", "Chantals model", "Chantals model that gets quite a good correlation by missing some events :-)");
        this.setDetectorEfficiency(new ChantalsDetectorEfficiency());
        
        this.setMeasurementFormula(new ChantalsMeasurementFormula());
        this.setAngleGenerator(new DefaultAngleGenerator());
    }

    @Override
    public HiddenVariablesIF createHiddenVariables() {
        return new DefaultHiddenVariables();
    }
}
