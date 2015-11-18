/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.multiple;


import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.problem.solution.OptimisationSolution;
import cilib.type.types.container.Vector;

/**
 * Multiple fitness.
 *
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @version 1.0
 * @deprecated This class is no longer valid. A combination of the
 *             {@link CompositeMeasurement} and {@link cilib.measurement.single.Fitness}
 *             should be used instead
 */
@Deprecated
public class MultipleFitness implements Measurement<Vector> {
    private static final long serialVersionUID = -255308745515061075L;

    /**
     * {@inheritDoc}
     */
    @Override
    public MultipleFitness getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getValue(Algorithm algorithm) {
        Vector.Builder fitnessValues = Vector.newBuilder();

        for (OptimisationSolution solution : algorithm.getSolutions()) {
            Double fitness = solution.getFitness().getValue();
            fitnessValues.add(fitness);
        }

        return fitnessValues.build();
    }

}
