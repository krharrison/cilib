/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.iterationstrategies.moo;

import fj.data.List;
import cilib.algorithm.AbstractAlgorithm;
import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.math.random.generator.Rand;
import cilib.problem.Problem;
import cilib.problem.solution.MOFitness;
import cilib.problem.solution.OptimisationSolution;
import cilib.pso.PSO;

/**
 * Implementation of the synchronous iteration strategy for PSO.
 *
 */
public class RelaxedNonDominatedMOOSynchronousIterationStrategy extends AbstractIterationStrategy<PSO> {
    /**
     * {@inheritDoc}
     */
    @Override
    public RelaxedNonDominatedMOOSynchronousIterationStrategy getClone() {
        return this;
    }

    /**
     * This is an Synchronous strategy:
     * <ol>
     * <li>For all particles:</li>
     * <ol><li>Update the particle velocity</li>
     *     <li>Update the particle position</li></ol>
     * <li>For all particles:</li>
     * <ol><li>Calculate the particle fitness</li>
     *     <li>For all particles in the current particle's neighbourhood:</li>
     *     <ol><li>Update the neighbourhood best</li></ol></ol>
     * </ol>
     *
     * @param pso The {@link PSO} to have an iteration applied.
     */
    @Override
    public void performIteration(PSO pso) {
        List<Particle> topology = pso.getTopology();

        for (Particle current : topology) {
            current.getBehaviour().performIteration(current);
        }

        Problem problem = AbstractAlgorithm.getAlgorithmList().head().getOptimisationProblem();
        for (Particle current : topology) {
            current.updateFitness(current.getBehaviour().getFitnessCalculator().getFitness(current));
            for (Particle other : pso.getNeighbourhood().f(topology, current)) {
                Particle p1 = current.getNeighbourhoodBest().getClone();
                Particle p2 = other.getNeighbourhoodBest().getClone();
                OptimisationSolution s1 = new OptimisationSolution(p1.getPosition().getClone(), problem.getFitness(p1.getPosition().getClone()));
                OptimisationSolution s2 = new OptimisationSolution(p2.getPosition().getClone(), problem.getFitness(p2.getPosition().getClone()));
                MOFitness fitness1 = (MOFitness)s1.getFitness();
                MOFitness fitness2 = (MOFitness)s2.getFitness();

                if (fitness1.compareTo(fitness2) > 0) {
                    other.setNeighbourhoodBest(current); // TODO: neighbourhood visitor?
                }
                else if (fitness1.compareTo(fitness2) == 0) {
                    int random = Rand.nextInt(20);
                    if (random > 10)
                        other.setNeighbourhoodBest(current);
                }
            }
        }
    }

}
