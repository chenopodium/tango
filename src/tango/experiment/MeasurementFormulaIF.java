package tango.experiment;

import tango.models.ModelItemIF;

/**
 *
 * @author Chantal Roth
 */
public interface MeasurementFormulaIF extends ModelItemIF{

    public int measure(Detector detector, Particle partice);

    
}
