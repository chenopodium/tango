/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.experiment;

/**
 *
 * @author Chantal
 */
public class Experiment {
    
    Detector detectorA;
    Detector detectorB;
    
    ExperimentModel model;
    
    public Experiment(ExperimentModel model) {
       
        detectorA = new Detector("A");        
        detectorB = new Detector("B");
        setModel(model);
    }
    
   public ExperimentModel getModel() {
       return model;
   }
    
     public void runOnce() {
        // create two particles, one going to fildter A later on, one going to filter B
        Particle pA = new Particle("A");
        Particle pB = new Particle("B");
        
        model.shareHiddenVariables(pA, pB);
                       
        detectorA.nextAngle();
        detectorB.nextAngle();

        detectorA.measure(pA);        
        detectorA.recordResult();
        
        detectorB.measure(pB);
        detectorB.recordResult();
        
    }

    public Detector getDetectorA() {
        return detectorA;
    }
    public Detector getDetectorB() {
        return detectorB;
    }

    public void setModel(ExperimentModel model) {
        this.model = model;
        detectorA.setModel(model);
        detectorB.setModel(model);
    }

}
