package tango.experiment;

/**
 *
 * @author Chantal Roth
 */
public interface MeasurementFormulaIF extends ModelItemIF{

    public int measure(Detector detector, Particle partice);

    
}
