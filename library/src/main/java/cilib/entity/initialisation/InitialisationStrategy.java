/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.entity.initialisation;

import cilib.entity.Property;
import cilib.util.Cloneable;

/**
 *
 */
public interface InitialisationStrategy<E> extends Cloneable {

    @Override
    InitialisationStrategy getClone();

    void initialise(Property key, E entity);

}
