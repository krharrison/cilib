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
import cilib.measurement.Measurement;
import cilib.type.types.Real;

/**
 *
 */
public class CollectiveFitness implements Measurement<Real> {
    private static final long serialVersionUID = 6171032748690594619L;

    @Override
    public Measurement getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        SinglePopulationBasedAlgorithm pba = (SinglePopulationBasedAlgorithm) algorithm;

        double collectiveFitness = 0.0;
        fj.data.List<Entity> local = pba.getTopology();
        for (Entity e : local) {
            collectiveFitness += e.getFitness().getValue();
        }

        return Real.valueOf(collectiveFitness);
    }

}
