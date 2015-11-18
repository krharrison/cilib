/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single.diversity.centerinitialisationstrategies;

import cilib.entity.Entity;
import cilib.type.types.container.Vector;

/**
 * Calculates the center of a given topology.
 */
public interface CenterInitialisationStrategy {

    /**
     * Get the population center.
     * @return the populationCenter
     */
    Vector getCenter(fj.data.List<? extends Entity> topology);

}
