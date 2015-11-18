/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.entity.comparator;

import java.io.Serializable;
import java.util.Comparator;
import cilib.algorithm.AbstractAlgorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.pso.particle.Particle;
import cilib.entity.SocialEntity;
import cilib.problem.MOOptimisationProblem;
import cilib.problem.solution.MOFitness;
import cilib.math.random.generator.Rand;

/**
 * Compare two {@link SocialEntity} instances, based on the available social best
 * fitness.
 * @param <E> The {@code SocialEntity} type.
 *
 */
public class RelaxedNonDominatedFitnessComparator <E extends SocialEntity> implements Comparator<E>, Serializable  {

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(E o1, E o2) {
        SinglePopulationBasedAlgorithm populationBasedAlgorithm = (SinglePopulationBasedAlgorithm) AbstractAlgorithm.getAlgorithmList().index(0);
        MOOptimisationProblem problem = ((MOOptimisationProblem)populationBasedAlgorithm.getOptimisationProblem());

        Particle p1 = (Particle)o1;
        Particle p2 = (Particle)o2;
        MOFitness fitness1 = ((MOFitness)problem.getFitness(p1.getBestPosition()));
        MOFitness fitness2 = ((MOFitness)problem.getFitness(p2.getBestPosition()));

        int value = fitness1.compareTo(fitness2);
        if (fitness1.compareTo(fitness2) == 0) {
            int random = Rand.nextInt(20);
            if (random > 10)
                value *= -1;
        }
        return  value;
    }
}

