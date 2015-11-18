/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.dynamic.responsestrategies;

import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Entity;
import cilib.entity.Topologies;
import cilib.pso.particle.Particle;

public class NeighbourhoodBestSentriesReactionStrategy<E extends SinglePopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy {
    private static final long serialVersionUID = -2142727048293776335L;

    public NeighbourhoodBestSentriesReactionStrategy(NeighbourhoodBestSentriesReactionStrategy<E> rhs) {
        super(rhs);
    }

    @Override
    public NeighbourhoodBestSentriesReactionStrategy<E> getClone() {
        return new NeighbourhoodBestSentriesReactionStrategy<E>(this);
    }

    @Override
	protected <P extends Particle, A extends SinglePopulationBasedAlgorithm<P>> void performReaction(
			A algorithm) {
        for (Entity entity : Topologies.getNeighbourhoodBestEntities(algorithm.getTopology(), algorithm.getNeighbourhood())) {
            entity.getPosition().randomise();
            // TODO: What is the influence of reevaluation?
//            entity.calculateFitness(false);
        }
    }
}
