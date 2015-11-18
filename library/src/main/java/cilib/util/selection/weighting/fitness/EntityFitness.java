/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.util.selection.weighting.fitness;

import cilib.entity.Entity;
import cilib.problem.solution.Fitness;

/**
 * Obtain the fitness of an entity.
 * @param <E> The entity type.
 */
public interface EntityFitness<E extends Entity> {

    /**
     * Obtain the {@code Fitness} of the provided entity.
     * @param entity The entity to query.
     * @return The obtained {@link Fitness} value.
     */
    Fitness getFitness(E entity);
}
