/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.positionprovider;

import cilib.entity.Entity;
import cilib.problem.solution.Fitness;

/**
 */
public class IterationNeighbourhoodBestUpdateStrategy implements NeighbourhoodBestUpdateStrategy {
    private static final long serialVersionUID = 9029103734770326975L;

    /**
     * {@inheritDoc}
     */
    public IterationNeighbourhoodBestUpdateStrategy getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Fitness getSocialBestFitness(Entity entity) {
        return entity.getFitness();
    }

}
