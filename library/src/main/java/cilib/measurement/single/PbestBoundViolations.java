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
import cilib.type.types.Bounds;
import cilib.type.types.Numeric;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;

/**
 * Calculates the average number of personal best positions in
 * the current swarm that violates boundary constraints.
 *
 */
public class PbestBoundViolations implements Measurement<Real> {

    private static final long serialVersionUID = 7547646366505677446L;

    /**
     * {@inheritDoc}
     */
    @Override
    public PbestBoundViolations getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        SinglePopulationBasedAlgorithm populationBasedAlgorithm = (SinglePopulationBasedAlgorithm) algorithm;

        int numberOfViolations = 0;
        int populationSize = populationBasedAlgorithm.getTopology().length();
        fj.data.List<Entity> local = populationBasedAlgorithm.getTopology();
        for (Entity populationEntity : local) {
            Vector pbest = (Vector) populationEntity.get(Property.BEST_POSITION);
            if (pbest == null) {
                throw new UnsupportedOperationException("Entity is not a particle.");
            }

            for (Numeric position : pbest) {
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
