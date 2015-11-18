/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.iterationstrategies;

import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.pso.PSO;
import cilib.pso.crossover.operations.BoltzmannCrossoverSelection;
import cilib.pso.crossover.operations.PSOCrossoverOperation;
import cilib.pso.particle.Particle;

/**
 * An iteration strategy that uses different PSOCrossoverOperations to affect the
 * swarm of particles.
 */
public class PSOCrossoverIterationStrategy extends AbstractIterationStrategy<PSO> {

    private PSOCrossoverOperation crossoverOperation;

    /**
     * Default constructor
     */
    public PSOCrossoverIterationStrategy() {
        this.crossoverOperation = new BoltzmannCrossoverSelection();
    }

    /**
     * Copy constructor
     *
     * @param copy
     */
    public PSOCrossoverIterationStrategy(PSOCrossoverIterationStrategy copy) {
        this.crossoverOperation = copy.crossoverOperation.getClone();
    }

    /**
     * Clones this instance
     *
     * @return the clone
     */
    @Override
    public PSOCrossoverIterationStrategy getClone() {
        return new PSOCrossoverIterationStrategy(this);
    }

    /**
     *
     *
     * @param algorithm
     */
    @Override
    public void performIteration(PSO algorithm) {
        fj.data.List<Particle> topology = algorithm.getTopology();

        for (Particle current : topology) {
            current.getBehaviour().performIteration(current);
        }

        algorithm.setTopology(crossoverOperation.f(algorithm));
        topology = algorithm.getTopology();

        for (Particle current : topology) {
            for (Particle other : algorithm.getNeighbourhood().f(topology, current)) {
                if (current.getSocialFitness().compareTo(other.getNeighbourhoodBest().getSocialFitness()) > 0) {
                    other.setNeighbourhoodBest(current);
                }
            }
        }
    }

    public void setCrossoverOperation(PSOCrossoverOperation crossoverOperation) {
        this.crossoverOperation = crossoverOperation;
    }
}
