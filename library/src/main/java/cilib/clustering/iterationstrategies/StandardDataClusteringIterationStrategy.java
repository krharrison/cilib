/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.clustering.iterationstrategies;

import cilib.clustering.DataClusteringPSO;
import cilib.clustering.entity.ClusterParticle;

/**
 * This class handles an iteration of the data clustering algorithm described in :
 * <pre>
 * {@literal @}article{vanDerMerwe03,
 *  title={{Data Clustering using Particle Swarm Optimization }},
 *  author={van der Merwe, D.W.; Engelhrecht, A.P.},
 *  year={2003},
 *  journal={Congress on Evolutionary Computation},
 *  volume={1},
 *  pages={215-220}
 * }
 * </pre>
 *
 *
 */
public class StandardDataClusteringIterationStrategy extends SinglePopulationDataClusteringIterationStrategy {
    /*
     * Default constructor for StandardDataClusteringIterationStrategy
     */
    public StandardDataClusteringIterationStrategy() {
        super();
    }

    /*
     * Copy constructor for StandardDataClusteringIterationStrategy
     */
    public StandardDataClusteringIterationStrategy(StandardDataClusteringIterationStrategy copy) {
        super(copy);
    }

    /*
     * Clone method of StandardDataClusteringIterationStrategy
     */
    @Override
    public StandardDataClusteringIterationStrategy getClone() {
        return new StandardDataClusteringIterationStrategy(this);
    }

    /*
     * Performs an iteration of the standard data clustering algorithm.
     * Assigns data patterns to centroids, updates personal and neighbourhood
     * bests and updates the particles.
     * @param algorithm The algorithm that called this iteration strategy
     */
    @Override
    public void performIteration(DataClusteringPSO algorithm) {
        fj.data.List<ClusterParticle> topology = algorithm.getTopology();
        reinitialised = false;

        for(ClusterParticle particle : topology) {
            particle.getBehaviour().performIteration(particle);
        }

        for (ClusterParticle current : topology) {
            for (ClusterParticle other : algorithm.getNeighbourhood().f(topology, current)) {
                if (current.getSocialFitness().compareTo(other.getNeighbourhoodBest().getSocialFitness()) > 0) {
                    other.setNeighbourhoodBest(current);
                }
            }
        }
    }
}
