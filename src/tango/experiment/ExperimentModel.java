/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.experiment;

/**
 *
 * @author Chantal
 */
public abstract class ExperimentModel extends ModelItem {
    private MeasurementFormulaIF measurementFormula;  
    private DetectorEfficiencyIF detectorEfficiency;
    private AngleGeneratorIF angleGenerator;
            
    public ExperimentModel(String key, String name, String description) {        
        super(key, name, description);
    }            
    
    @Override
    public void init() {
        measurementFormula.init();
    }

     public abstract HiddenVariablesIF createHiddenVariables() ;
    
    public void shareHiddenVariables(Particle pA, Particle pB) {
       
        HiddenVariablesIF hidden = createHiddenVariables();
        hidden.generateHiddenVariables(pA);
        
        
        pA.setHiddenVars(hidden);
        pB.setHiddenVars(hidden);    
    }

    /**
     * @return the measurementFormula
     */
    public MeasurementFormulaIF getMeasurementFormula() {
        return measurementFormula;
    }

    /**
     * @param measurementFormula the measurementFormula to set
     */
    public void setMeasurementFormula(MeasurementFormulaIF measurementFormula) {
        this.measurementFormula = measurementFormula;
    }

   
    /**
     * @return the detectorEfficiency
     */
    public DetectorEfficiencyIF getDetectorEfficiency() {
        return detectorEfficiency;
    }

    /**
     * @param detectorEfficiency the detectorEfficiency to set
     */
    public void setDetectorEfficiency(DetectorEfficiencyIF detectorEfficiency) {
        this.detectorEfficiency = detectorEfficiency;
    }

    /**
     * @return the angleGenerator
     */
    public AngleGeneratorIF getAngleGenerator() {
        return angleGenerator;
    }

    /**
     * @param angleGenerator the angleGenerator to set
     */
    public void setAngleGenerator(AngleGeneratorIF angleGenerator) {
        this.angleGenerator = angleGenerator;
    }

  
   
}
