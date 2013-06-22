package tango.experiment;

import tango.prefs.PreferenceManager;

/**
 * Class containing static settings for the experiment, such as what angles to
 * use
 */
public class Setup {

    /** Default angles to compute CHSH  */
    public static double A1 = 0;
    public static double A2 = 90;
    public static double B1 = 45;
    public static double B2 = 135;
    
    
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
