/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.type.types.StringType;
import cilib.type.types.container.Vector;

/**
 * Print the position of the best entity within a topology.
 *
 */
public class BestEntityPosition implements Measurement<StringType> {
    private static final long serialVersionUID = 5808686984197365658L;

    /**
     * {@inheritDoc}
     */
    @Override
    public BestEntityPosition getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringType getValue(Algorithm algorithm) {
        Vector solution = (Vector) algorithm.getBestSolution().getPosition();
        return new StringType(solution.toString());
    }

}
