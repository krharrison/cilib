/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.controlparameter.initialisation;

import cilib.controlparameter.AdaptableControlParameter;
import cilib.util.Cloneable;

/*
 * This is an interface for initialisation strategies specific to parameters.
 */
public interface ControlParameterInitialisationStrategy<E> extends Cloneable {

    @Override
    ControlParameterInitialisationStrategy getClone();

    void initialise(AdaptableControlParameter parameter);

}
