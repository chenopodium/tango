/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.external;

import tango.models.def.DefaultHiddenVariables;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import tango.experiment.Detector;
import tango.experiment.Experiment;
import tango.experiment.ExperimentModel;
import tango.experiment.HiddenVariablesIF;
import tango.experiment.MeasurementFormulaIF;
import tango.models.ModelItem;
import tango.experiment.Particle;
import tango.prefs.PreferenceManager;
import tango.prefs.UserPref;

/**
 *
 * @author Chantal
 */
public class ExternalMeasurementFormula extends ModelItem implements MeasurementFormulaIF {

    String program;
    Preferences prefs = PreferenceManager.getPreferences();

    public ExternalMeasurementFormula(Experiment experiment) {
        super("EXTERNALFORMULA", "External formula", "Model that calls external program to measure the result");
        super.setExperiment(experiment);
    }

    @Override
    public String getDescription() {
        if (program != null) {
            return "Run "+program+" to measure result";
        }
        else return super.getDescription();
    }
    @Override
    public void init() {

        UserPref pref = PreferenceManager.getManager().extmeas;
        UserPref prefargs = PreferenceManager.getManager().extmeasargs;

        String msg = "Please enter the external program that computes the spin based on the detector angle and the hidden parameters of the particle<br>";
        msg += "The angle of the detector is in degrees<br>";
        ExperimentModel model = this.getExperiment().getModel();
        HiddenVariablesIF hidden = model.createHiddenVariables();
        if (hidden instanceof ExternalHiddenVariables) {
            ExternalHiddenVariables vars = (ExternalHiddenVariables) hidden;
            String extfile = vars.getPathToFile();
            msg += "The path to the hidden vars file is: " + extfile + "<br>";
            msg += "Use #detector and #file in the command line as variables for the angle and the external file<br>";
            msg += "Example: c:/bin/myeproprgram -d #detector -file #file";
        } else if (hidden instanceof DefaultHiddenVariables) {
            DefaultHiddenVariables vars = (DefaultHiddenVariables) hidden;
            double theta = vars.getTheta();
            msg += "The theta of particle is: " + theta + "<br>";
            msg += "Use #detector and #theta in the command line as variables for the detector angle and the particle angle<br>";
            msg += "Example: c:/bin/myeproprgram -d #detector -p #theta";
        }
        ExternalProgPanel panel = new ExternalProgPanel(pref);
        panel.setArgs(prefargs.getValue());
        panel.setDesc("<html>" + msg + "</html>");

        JOptionPane.showMessageDialog(null, panel);
        program = panel.getFile() + " " + panel.getArgs();

        pref.setValue(panel.getFile());
        prefargs.setValue(panel.getArgs());
        
        JOptionPane.showMessageDialog(null, "<html>Running an external program can <b>very slow</b>.<br>You might want to reduce the nr of pairs to < 1000");
    }

    @Override
    public int measure(Detector detector, Particle particle) {
        if (program == null) {
            init();
        }
        String cmd = program.replace("#detector", "" + detector.getAngleInDegrees());
        if (particle.getHiddenVars() instanceof ExternalHiddenVariables) {
            ExternalHiddenVariables vars = (ExternalHiddenVariables) particle.getHiddenVars();
            cmd = cmd.replace("#file", vars.getPathToFile());
        } else if (particle.getHiddenVars() instanceof DefaultHiddenVariables) {
            DefaultHiddenVariables vars = (DefaultHiddenVariables) particle.getHiddenVars();
            double theta = vars.getTheta();
            cmd = cmd.replace("#theta", "" + theta);
            cmd = cmd.replace("#particle", "" + theta);
        }
        return ExternalTools.getIntFromCommand(cmd);
    }
}
