/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single.diversity.normalisation;

import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;

/**
 * Diversity normalisation based on a control parameter.
 */
public class NormalisationParameter implements DiversityNormalisation {

    protected ControlParameter normalisationParameter;

    /**
     * Default constructor.
     */
    public NormalisationParameter() {
        normalisationParameter = ConstantControlParameter.of(1.0);
    }

    /**
     * @return the normalising parameter
     */
    @Override
    public double getNormalisationParameter(SinglePopulationBasedAlgorithm algorithm) {
        return this.normalisationParameter.getParameter();
    }

    /**
     * Set the value to be used as a normalisation parameter
     * @param value The new normalisation parameter.
     */
    public void setNormalisationParameter(ControlParameter value) {
        this.normalisationParameter = value;
    }

    /**
     * Gets the normalisation parameter.
     * @return the normalisation parameter.
     */
    public ControlParameter getNormalisationParameter() {
        return normalisationParameter;
    }
}
