/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import java.util.Iterator;

import cilib.algorithm.Algorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Entity;
import cilib.measurement.Measurement;
import cilib.problem.solution.InferiorFitness;
import cilib.type.types.Real;


/**
 * Return the fitness of the best {@link Entity} in the current iteration.
 */
public class IterationBestFitness implements Measurement<Real>{

    @Override
    public IterationBestFitness getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        SinglePopulationBasedAlgorithm populationBasedAlgorithm = (SinglePopulationBasedAlgorithm) algorithm;

        Iterator<Entity> populationIterator = populationBasedAlgorithm.getTopology().iterator();

        cilib.problem.solution.Fitness bestFitness = InferiorFitness.instance();

        while (populationIterator.hasNext()) {
            Entity entity = populationIterator.next();
            cilib.problem.solution.Fitness entityFitness = entity.getFitness();

            if(Double.isNaN(entityFitness.getValue())) continue;

            if(entityFitness.compareTo(bestFitness) > 0) {
                bestFitness = entityFitness;
            }
        }

        return Real.valueOf(bestFitness.getValue());
    }

}