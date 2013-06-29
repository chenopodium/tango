/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models;

import java.util.ArrayList;
import tango.experiment.AngleGeneratorIF;
import tango.experiment.DetectorEfficiencyIF;
import tango.experiment.Experiment;
import tango.experiment.ExperimentModel;
import tango.experiment.MeasurementFormulaIF;
import tango.models.bryan.BryansMeasurementFormula;
import tango.models.bryan.BryansModel;
import tango.models.chantal.ChantalsDetectorEfficiency;
import tango.models.chantal.ChantalsMeasurementFormula;
import tango.models.chantal.ChantalsModel;
import tango.models.def.DefaultAngleGenerator;
import tango.models.def.DefaultDetectorEfficiency;
import tango.models.def.DefaultExperimentModel;
import tango.models.def.DefaultMeasurementFormula;
import tango.models.external.ExternalAngleGenerator;
import tango.models.external.ExternalDetectorEfficiency;
import tango.models.external.ExternalExperimentModel;
import tango.models.external.ExternalMeasurementFormula;
import tango.guiutils.GuiUtils;
import tango.mikesmodel.MikesAngleGenerator;
import tango.mikesmodel.MikesDetectorEfficiency;
import tango.mikesmodel.MikesExperimentModel;
import tango.mikesmodel.MikesMeasurementFormula;
import tango.models.dummy.DummyDetectorEfficiency;

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
        models.add(new MikesExperimentModel());
        models.add(new ExternalExperimentModel(exp));
        return models;
    }
    public static ArrayList<DetectorEfficiencyIF> getPossibleDetectorEfficencies(Experiment exp) {
        ArrayList<DetectorEfficiencyIF> eff = new ArrayList<DetectorEfficiencyIF> ();
        eff.add(new DefaultDetectorEfficiency());
        eff.add(new ChantalsDetectorEfficiency());
        eff.add(new ExternalDetectorEfficiency(exp));
        eff.add(new MikesDetectorEfficiency());
        eff.add(new DummyDetectorEfficiency());
        return eff;        
    }
    public static ArrayList<MeasurementFormulaIF> getPossibleMeasurementFormulas(Experiment exp) {
        ArrayList<MeasurementFormulaIF> form = new ArrayList<MeasurementFormulaIF> ();
        form.add(new DefaultMeasurementFormula());
        form.add(new ChantalsMeasurementFormula());
        form.add(new BryansMeasurementFormula());
        form.add(new MikesMeasurementFormula());
        form.add(new ExternalMeasurementFormula(exp));
        return form;
    }
     public static ArrayList<AngleGeneratorIF> getPossibleAngleGenerators() {
        ArrayList<AngleGeneratorIF> ang = new ArrayList<AngleGeneratorIF> ();
        ang.add(new DefaultAngleGenerator());
        ang.add(new MikesAngleGenerator());
        ang.add(new ExternalAngleGenerator());        
        return ang;
    }
     
 
    
}
