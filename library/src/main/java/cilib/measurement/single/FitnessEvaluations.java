/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.type.types.Int;

/**
 *
 */
public class FitnessEvaluations implements Measurement<Int> {
    private static final long serialVersionUID = 8843539724541605245L;

    /**
     * {@inheritDoc}
     */
    @Override
    public FitnessEvaluations getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int getValue(Algorithm algorithm) {
        int evaluations = algorithm.getOptimisationProblem().getFitnessEvaluations();
        return Int.valueOf(evaluations);
    }

}
