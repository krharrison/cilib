/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.controlparameter;

import cilib.util.Cloneable;

/**
 * A {@linkplain cilib.controlparameter.ControlParameter control parameter} is a
 * parameter that is used within most {@linkplain cilib.algorithm.Algorithm algorithm}
 * types. These parameters are updatable and can be changed over time, if required.
 */
public interface ControlParameter extends Cloneable {

    /**
     * Get the value of the represented parameter.
     * @return The value of the represented parameter.
     */
    double getParameter();
    
    double getParameter(double min, double max);
    
    /**
     * {@inheritDoc}
     */
    @Override
    ControlParameter getClone();
}
