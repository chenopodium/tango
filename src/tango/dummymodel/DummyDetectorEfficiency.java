/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.dummymodel;

import javax.swing.JOptionPane;
import tango.experiment.Detector;
import tango.experiment.DetectorEfficiencyIF;
import tango.experiment.ModelItem;
import tango.experiment.Particle;
import tango.guiutils.GuiUtils;
import tango.utils.RandomUtils;

/**
 *
 * @author Chantal
 */
public class DummyDetectorEfficiency extends ModelItem implements DetectorEfficiencyIF {
      double prob=-1;
    
    public DummyDetectorEfficiency() {
          super("DUMMY_EFFICIENCY", "Random efficiency", "Random detection of particle, based on probability given by user");
    }
    
     @Override
    public DetectorEfficiencyIF createInstanc() {
        return new DummyDetectorEfficiency();
    }
    @Override
    public boolean isDetected(Particle particle, Detector detector) {
        if (prob < 0) {
             String ans = JOptionPane.showInputDialog(null,"Probabilty of detection (0 - 100)", "100");
             try {
                 prob = Double.parseDouble(ans)/100.0;                 
             }
             catch (Exception e) {
                 GuiUtils.msg("Could not convert "+ans+" to a probabilty from 1-100");
             }
        }
        return RandomUtils.getRandomValue()<prob;
    }

   
}
