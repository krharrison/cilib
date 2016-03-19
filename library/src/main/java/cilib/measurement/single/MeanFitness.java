/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Entity;
import cilib.measurement.Measurement;
import cilib.problem.solution.InferiorFitness;
import cilib.type.types.Real;

/**
 * Returns the mean fitness of entities which are have non-Inferior Fitness values
 */
public class MeanFitness implements Measurement<Real> {
    @Override
    public Measurement<Real> getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        SinglePopulationBasedAlgorithm pba = (SinglePopulationBasedAlgorithm) algorithm;

        double sum = 0;
        int count = 0;
        fj.data.List<Entity> topology = pba.getTopology();
        for (Entity e : topology) {
            cilib.problem.solution.Fitness f = e.getFitness();
            if(f.compareTo(InferiorFitness.instance()) != 0) {
                sum += e.getFitness().getValue();
                count++;
            }
        }

        return Real.valueOf(sum / count);
    }
}
