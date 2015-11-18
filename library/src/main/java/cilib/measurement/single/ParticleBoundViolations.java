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
import cilib.type.types.Bounds;
import cilib.type.types.Numeric;
import cilib.type.types.Real;

/**
 * Calculates the average number of particles in the current swarm that
 * violates boundary constraints. This measure can be used as an
 * indicator of whether the algorithm spend too much time exploring
 * in infeasible space (with respect to the boundary constraints).
 *
 */
public class ParticleBoundViolations implements Measurement<Real> {

    private static final long serialVersionUID = 2232130008790333636L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Measurement getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        SinglePopulationBasedAlgorithm populationBasedAlgorithm = (SinglePopulationBasedAlgorithm) algorithm;

        Iterator<Entity> populationIterator = populationBasedAlgorithm.getTopology().iterator();

        int numberOfViolations = 0;
        int populationSize = populationBasedAlgorithm.getTopology().length();

        while (populationIterator.hasNext()) {
            Entity entity = populationIterator.next();

            Iterator positionIterator = entity.getPosition().iterator();

            while (positionIterator.hasNext()) {
                Numeric position = (Numeric) positionIterator.next();
                Bounds bounds = position.getBounds();
                if (!bounds.isInsideBounds(position.doubleValue())) {
                    numberOfViolations++;
                    break;
                }
            }
        }
        return Real.valueOf((double) numberOfViolations / (double) populationSize);
    }
}
