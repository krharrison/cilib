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
import cilib.entity.visitor.DiameterVisitor;
import cilib.measurement.Measurement;
import cilib.type.types.Real;

/**
 * Calculates the swarm diameter as the maximum euclidean distance between any
 * two particles.
 *
 *
 *
 */
public class Diameter implements Measurement<Real> {
    private static final long serialVersionUID = 5136996282460480831L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Diameter getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        SinglePopulationBasedAlgorithm popAlg = (SinglePopulationBasedAlgorithm) algorithm;
        fj.data.List<Entity> topology = popAlg.getTopology();
        return Real.valueOf(new DiameterVisitor().f(topology));
    }

}
