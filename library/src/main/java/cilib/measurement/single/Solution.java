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
 */
public class Solution implements Measurement<StringType> {
    private static final long serialVersionUID = -7485598441585703760L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Solution getClone() {
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
