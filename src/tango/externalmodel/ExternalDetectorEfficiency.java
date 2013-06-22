/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.externalmodel;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import tango.defaultmodel.DefaultHiddenVariables;
import tango.experiment.Detector;
import tango.experiment.DetectorEfficiencyIF;
import tango.experiment.Experiment;
import tango.experiment.ExperimentModel;
import tango.experiment.HiddenVariablesIF;
import tango.experiment.ModelItem;
import tango.experiment.Particle;
import tango.prefs.PreferenceManager;
import tango.prefs.UserPref;
import tango.utils.Executor;

/**
 *
 * @author Chantal
 */
public class ExternalDetectorEfficiency extends ModelItem implements DetectorEfficiencyIF {

    String program;
    Preferences prefs = PreferenceManager.getPreferences();

    public ExternalDetectorEfficiency(Experiment exp) {
        super("EXTERNAL_EFFICIENY", "External efficiency", "Run external program to compute detector efficiency based on detector angle and particle hidden variables");
        super.setExperiment(exp);
    }

    @Override
    public DetectorEfficiencyIF createInstanc() {
        return new ExternalDetectorEfficiency(super.getExperiment());
    }

    @Override
    public void init() {
        UserPref pref = PreferenceManager.getManager().exteff;
        String msg = "Please enter the external program that determines if a particle is detected, based on the detector angle the hidden parameters of the particle<br>";
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
        panel.setArgs("-d #detector -p #theta");
        panel.setDesc("<html>" + msg + "</html>");

        JOptionPane.showMessageDialog(null, panel);
        program = panel.getFile() + " " + panel.getArgs();
        pref.setValue(panel.getFile());
        JOptionPane.showMessageDialog(null, "<html>Running an external program can <b>very slow</b>.<br>You might want to reduce the nr of pairs to < 1000");

    }

    @Override
    public String getDescription() {
        if (program != null) {
            return "Run " + program + " to compute detector efficiency";
        } else {
            return super.getDescription();
        }
    }

    @Override
    public boolean isDetected(Particle particle, Detector detector) {
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
        }
        String result = Executor.execute(cmd);

        boolean b = result != null && !result.equals("0");
        sp("Got result: " + result + " from " + cmd + ", not 0 means true: " + b);
        return b;
    }

    private static void sp(String msg) {
        System.out.println("ExternalTools: " + msg);
        Logger.getLogger(ExternalDetectorEfficiency.class.getName()).log(Level.INFO, msg);
    }
}
