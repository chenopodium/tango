/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models.mike;

import javax.swing.JOptionPane;
import tango.models.def.DefaultHiddenVariables;
import tango.experiment.Detector;
import tango.experiment.MeasurementFormulaIF;
import tango.models.ModelItem;
import tango.experiment.Particle;
import tango.models.bryan.BryansHiddenVariables;
import tango.utils.RandomUtils;

/**
 *
 * @author Chantal
 */
public class MikesMeasurementFormula extends ModelItem implements MeasurementFormulaIF{

    private boolean errorshown;
    
    private class Spin {
        public int pm;
        public int mp;
    }

    public MikesMeasurementFormula() {
      super("MIKES_FORMULA", "Mikes formula", "Spin is sign (sin(angle))");
    }
  

    static int dichotomic(double x) {
        return x>=0 ? +1 : -1;
    }

    static int dichotomic(double x, double dx) {
        return x>0 ? +1 : x<0 ? -1 : dx>=0 ? +1 : -1;
    }

    @Override
    public int measure(Detector detector, Particle particle) {

        if (!(particle.getHiddenVars() instanceof MikesHiddenVariables)) {
            if (!errorshown) {
                JOptionPane.showMessageDialog(null, "Mike's Measurement also requires Mike's Hidden Variables :-). Can you please pick the model with the LHV button?");
            }
            errorshown = true;
            return 0;
        }

        MikesHiddenVariables hidden = (MikesHiddenVariables) particle.getHiddenVars();

    //  extract hidden variables
        double theta=Math.toRadians(hidden.getTheta()+22.5/2);
        int direction=hidden.getDirection();
        double randPM=hidden.getRandPM();
        double randMP=hidden.getRandMP();

    //  compute difference between theta and the filter setting
        double filterAngle=Math.toRadians(detector.getAngleInDegrees());
        double delta=filterAngle-theta;

    //  calculate digital quadrature from theta
        int nP=dichotomic(+Math.cos(theta),-Math.sin(theta)); // eliminates random hidden variables used by Bryan
        int nM=dichotomic(+Math.sin(theta),+Math.cos(theta)); // eliminates random hidden variables used by Bryan

        double gamma=nM*nP*(Math.PI/4);     // part of simplification of Bryan's probability computations

        double probPM=(1-direction*nM*Math.sin(delta-gamma))/2; // simplified probability computations
        double probMP=(1-direction*nM*Math.cos(delta+gamma))/2; // simplified probability computations

        Spin spin = new Spin();
        spin.pm=dichotomic(randPM-probPM); // probabilistic determination of plus/minus selection
        spin.mp=dichotomic(randMP-probMP); // probabilistic determination of minus/plus selection
        return spin.pm;
    }   
}
