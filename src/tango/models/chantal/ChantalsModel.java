/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.chantal;

import tango.models.def.DefaultAngleGenerator;
import tango.models.def.DefaultHiddenVariables;
import tango.experiment.HiddenVariablesIF;
import tango.models.def.DefaultExperimentModel;

/**
 *
 * @author Chantal
 */
public class ChantalsModel extends DefaultExperimentModel {
    
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
