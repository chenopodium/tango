/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.experiment;

import java.util.ArrayList;
import tango.bryansmodel.BryansMeasurementFormula;
import tango.bryansmodel.BryansModel;
import tango.chantalsmodel.ChantalsDetectorEfficiency;
import tango.chantalsmodel.ChantalsMeasurementFormula;
import tango.chantalsmodel.ChantalsModel;
import tango.defaultmodel.DefaultAngleGenerator;
import tango.defaultmodel.DefaultDetectorEfficiency;
import tango.defaultmodel.DefaultExperimentModel;
import tango.defaultmodel.DefaultMeasurementFormula;
import tango.externalmodel.ExternalAngleGenerator;
import tango.externalmodel.ExternalDetectorEfficiency;
import tango.externalmodel.ExternalExperimentModel;
import tango.externalmodel.ExternalMeasurementFormula;
import tango.guiutils.GuiUtils;

/**
 *
 * @author Chantal
 */
public class ModelFactory {
    
    public ExperimentModel getModel(String key, Experiment exp) {
        for (ExperimentModel model: getPossibleModels(exp)) {
            if (model.getKey().equalsIgnoreCase(key )) {
                return model;
            }
        }
        GuiUtils.msg("Not sure which model this is: "+key);
        return null;
    }
    public static ArrayList<ExperimentModel> getPossibleModels(Experiment exp) {
        ArrayList<ExperimentModel> models = new ArrayList<ExperimentModel>();
        models.add(new DefaultExperimentModel());
        models.add(new ChantalsModel());
        models.add(new BryansModel());
        models.add(new ExternalExperimentModel(exp));
        return models;
    }
    public static ArrayList<DetectorEfficiencyIF> getPossibleDetectorEfficencies(Experiment exp) {
        ArrayList<DetectorEfficiencyIF> eff = new ArrayList<DetectorEfficiencyIF> ();
        eff.add(new DefaultDetectorEfficiency());
        eff.add(new ChantalsDetectorEfficiency());
        eff.add(new ExternalDetectorEfficiency(exp));
        return eff;        
    }
    public static ArrayList<MeasurementFormulaIF> getPossibleMeasurementFormulas(Experiment exp) {
        ArrayList<MeasurementFormulaIF> form = new ArrayList<MeasurementFormulaIF> ();
        form.add(new DefaultMeasurementFormula());
        form.add(new ChantalsMeasurementFormula());
        form.add(new BryansMeasurementFormula());
        form.add(new ExternalMeasurementFormula(exp));
        return form;
    }
     public static ArrayList<AngleGeneratorIF> getPossibleAngleGenerators() {
        ArrayList<AngleGeneratorIF> ang = new ArrayList<AngleGeneratorIF> ();
        ang.add(new DefaultAngleGenerator());
        ang.add(new ExternalAngleGenerator());        
        return ang;
    }
     
 
    
}
