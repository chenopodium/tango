/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.externalmodel;

import java.util.prefs.Preferences;
import tango.experiment.Particle;
import tango.experiment.HiddenVariablesIF;
import tango.experiment.ModelItem;
import tango.prefs.PreferenceManager;
import tango.utils.FileTools;

/**
 *
 * @author Chantal
 */
public class ExternalHiddenVariables extends ModelItem implements HiddenVariablesIF {

    private String externalFile;
    Preferences prefs = PreferenceManager.getPreferences();

    public ExternalHiddenVariables() {
        super("EXTERNAL_VARS", "External hidden vars", "Hidden variables are read from an external file");
    }

    @Override
    public String getDescription() {
        if (externalFile != null) {
            return "Uses data from "+externalFile+" as hidden variables";
        }
        else return super.getDescription();
    }
    @Override
    public void init() {
        String def = prefs.get("HIDDEN_VARS_FILE", ".");
        externalFile = FileTools.getFile("File with hidden variables", "*.*", def);
    }

    @Override
    public void generateHiddenVariables(Particle pA) {
        if (externalFile == null) {
            init();
        }
    }

    public String getPathToFile() {
        return externalFile;
    }
}
