/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.util.selection.weighting.fitness;

import cilib.entity.Entity;
import cilib.entity.SocialEntity;
import cilib.problem.solution.Fitness;

/**
 * Obtain the social best fitness value from a {@link SocialEntity}.
 * @param <E> 	The type that is both an {@code Entity} and a
 *				{@code SocialEntity}. An example of such an entity is the
 *				{@link cilib.pso.particle.Particle}.
 */
public class SocialBestFitness<E extends Entity & SocialEntity> implements EntityFitness<E> {

    /**
     * {@inheritDoc}
     * Obtains the social based fitness from the {@link SocialEntity}.
     */
    @Override
    public Fitness getFitness(E entity) {
        return entity.getSocialFitness();
    }

}
