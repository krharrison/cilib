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
import cilib.type.types.Bounds;
import cilib.type.types.Numeric;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;

/**
 * Calculates the average number of violations of boundary constraints
 * with respect to each dimension. This measure can be used as an
 * indicator of whether the algorithm spend too much time exploring
 * in infeasible space (with respect to the boundary constraints).
 *
 */
public class DimensionBoundViolationsPerParticle implements Measurement<Real> {

    private static final long serialVersionUID = -3633155366562479197L;

    /** Creates a new instance of DimensionBoundViolationsPerParticle. */
    public DimensionBoundViolationsPerParticle() {
    }

    /**
     * Copy constructor. Creates a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public DimensionBoundViolationsPerParticle(DimensionBoundViolationsPerParticle copy) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DimensionBoundViolationsPerParticle getClone() {
        return new DimensionBoundViolationsPerParticle(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        SinglePopulationBasedAlgorithm populationBasedAlgorithm = (SinglePopulationBasedAlgorithm) algorithm;
        double sumOfAverageViolations = 0.0;
        int populationSize = populationBasedAlgorithm.getTopology().length();
        int numberOfViolations;

        fj.data.List<Entity> local = populationBasedAlgorithm.getTopology();
        for (Entity populationEntity : local) {
            numberOfViolations = 0;

            for (Numeric position : (Vector) populationEntity.getPosition()) {
                Bounds bounds = position.getBounds();

                if (!bounds.isInsideBounds(position.doubleValue())) {
                    numberOfViolations++;
                }
            }
            sumOfAverageViolations += numberOfViolations;
        }

        return Real.valueOf(sumOfAverageViolations / (double) populationSize);
    }
}
