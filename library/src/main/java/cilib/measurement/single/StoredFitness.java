/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Entity;
import cilib.entity.Property;
import cilib.measurement.Measurement;
import cilib.problem.solution.Fitness;
import cilib.type.types.Real;

/**
 * This measurement only works for PSO. Simply find the Entity with the Best person best fitness in the population without re-calculating the fitness.
 */
public class StoredFitness implements Measurement<Real> {

    private static final long serialVersionUID = 6502384299554109943L;

    /**
     * {@inheritDoc}
     */
    @Override
    public StoredFitness getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        Fitness best = null;
        SinglePopulationBasedAlgorithm<Entity> currentAlgorithm = (SinglePopulationBasedAlgorithm) algorithm;
        for (Entity e : currentAlgorithm.getTopology()) {
            if (best == null || ((Fitness) e.get(Property.BEST_FITNESS)).compareTo(best) > 0) {
                best = ((Fitness) e.get(Property.BEST_FITNESS));
            }
        }
        return Real.valueOf(best.getValue());
    }
}
