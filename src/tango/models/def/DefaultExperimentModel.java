/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.def;

import tango.experiment.ExperimentModel;
import tango.experiment.HiddenVariablesIF;
import tango.experiment.Particle;

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
    
    public DefaultExperimentModel(String key, String name, String description) {        
        super(key, name, description);
    }  

    @Override
    public HiddenVariablesIF createHiddenVariables() {
         HiddenVariablesIF vars = new DefaultHiddenVariables();
         return vars;
    }
    
    @Override
     public void shareHiddenVariables(Particle pA, Particle pB) {
       
        DefaultHiddenVariables hiddena = (DefaultHiddenVariables) createHiddenVariables();
        hiddena.generateHiddenVariables(pA);
               
        DefaultHiddenVariables hiddenb = (DefaultHiddenVariables) createHiddenVariables();
        hiddenb.generateHiddenVariables(pB);
        
        pA.setHiddenVars(hiddena);
        hiddenb.setTheta(hiddena.getTheta()+180.0);
        pB.setHiddenVars(hiddenb);    
    }
}
