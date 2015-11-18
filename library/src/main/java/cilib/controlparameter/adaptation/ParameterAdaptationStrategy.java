/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.controlparameter.adaptation;

import cilib.controlparameter.AdaptableControlParameter;
import cilib.entity.Entity;
import cilib.util.Cloneable;

/**
 * This is an interface for parameter adaptation strategies, that is, strategies 
 * that will change the values of parameters.
 */
public interface ParameterAdaptationStrategy extends Cloneable {
    @Override
    ParameterAdaptationStrategy getClone();

    public void change(AdaptableControlParameter parameter);
    
    public void accepted(AdaptableControlParameter parameter, Entity entity, boolean accepted);
    
    public double recalculateAdaptiveVariables();
}
