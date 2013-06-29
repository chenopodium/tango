/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.external;

import tango.experiment.Experiment;
import tango.experiment.ExperimentModel;
import tango.experiment.HiddenVariablesIF;

/**
 *
 * @author Chantal
 */
public class ExternalExperimentModel extends ExperimentModel {
    
    HiddenVariablesIF vars;
    public ExternalExperimentModel(Experiment exp) {
        super("EXTERNAL", "External model", "Model that reads settings from external files");
        this.setDetectorEfficiency(new ExternalDetectorEfficiency(exp));        
        this.setMeasurementFormula(new ExternalMeasurementFormula(exp));
        this.setAngleGenerator(new ExternalAngleGenerator());
    }

    @Override
    public void init() {
        this.getDetectorEfficiency().init();
        this.createHiddenVariables().init();
        this.getMeasurementFormula().init();
        this.getAngleGenerator().init();
    }
    @Override
    public HiddenVariablesIF createHiddenVariables() {
         if (vars == null) {
             vars = new ExternalHiddenVariables();
             vars.init();
         }
         return vars;
    }
}
