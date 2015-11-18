/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.hpso.pheromoneupdate;

import cilib.pso.particle.Particle;
import cilib.util.Cloneable;

/**
 * Interface to calculate the change in pheromone level for the adaptive HPSO
 * using pheromones
 */
public interface PheromoneUpdateStrategy extends Cloneable {

    /**
     * Calculates the amount that a behavior's pheromone level should change
     *
     * @param particle the {@linkplain Particle} which affects the behavior's pheromone level
     * @return the change in pheromone for a {@linkplain Particle}'s behavior
     */
    double updatePheromone(Particle particle);
}
