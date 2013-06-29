/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.external;

import java.util.ArrayList;
import tango.experiment.AngleGeneratorIF;
import tango.experiment.DetectorEfficiencyIF;
import tango.models.ModelItem;
import tango.guiutils.GuiUtils;
import tango.prefs.PreferenceManager;
import tango.utils.ErrorHandler;

/**
 *
 * @author Chantal
 */
public class ExternalAngleGenerator extends ModelItem implements AngleGeneratorIF {

    ArrayList<Double> angles;
    int pos;

    public ExternalAngleGenerator() {
        super("EXTERNAL", "External angles", "Reads detector angles from the user");
    }

    @Override
    public AngleGeneratorIF createInstance() {
        return new ExternalAngleGenerator();
    }

    @Override
    public String getDescription() {
        if (angles != null) {
            return "Got "+angles.size()+" user defined angles";
        }
        else return super.getDescription();
    }
    @Override
    public void init() {
        Exception e = new Exception("stack trace");
        p(ErrorHandler.getString(e));
        angles = ExternalTools.getListOfValues(PreferenceManager.getManager().extanglesa, "Please provide the list of angles to use");
        if (angles != null && angles.size() > 0) {
            GuiUtils.msg("Got " + angles.size() + " angles");
        } else {
            GuiUtils.msg("Got no angles, please try again");
        }

    }

    @Override
    public double nextAngle() {
        if (angles == null || angles.size() < 1) {
            init();
        }
        if (angles == null) {
            return 0;
        }
        if (pos >= angles.size()) {
            pos = 0;
        }
        
        return angles.get(pos++);
    }
}
