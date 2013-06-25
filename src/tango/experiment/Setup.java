package tango.experiment;

import tango.prefs.PreferenceManager;

/**
 * Class containing static settings for the experiment, such as what angles to
 * use
 */
public class Setup {

    
    PreferenceManager prefs;
    ExperimentModel model;
    public static Setup setup = new Setup();

    public static Setup getSetup() {
        return setup;
    }

    private Setup() {
        prefs = PreferenceManager.getManager();
    }

    public double getAngleDelta() {
        return prefs.getAngleDelta();
    }

    public double getNrAngles() {
        return prefs.getNrAngles();
    }

    public ExperimentModel getModel() {
        return model;
    }

    public void setExperimentModel(ExperimentModel model) {
        this.model = model;
    }
}
