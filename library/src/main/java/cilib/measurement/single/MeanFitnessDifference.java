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
import cilib.entity.Property;
import cilib.measurement.Measurement;
import cilib.type.types.Real;

public class MeanFitnessDifference implements Measurement<Real> {
    @Override
    public Measurement<Real> getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm) {

        if(algorithm.getIterations() == 0) return Real.valueOf(0);

        SinglePopulationBasedAlgorithm pba = (SinglePopulationBasedAlgorithm) algorithm;

        double sum = 0;
        fj.data.List<Entity> topology = pba.getTopology();
        for (Entity e : topology) {
            sum += e.getFitness().getValue() - e.get(Property.PREVIOUS_FITNESS).getValue();
        }

        return Real.valueOf(sum / pba.getTopology().length());
    }
}
