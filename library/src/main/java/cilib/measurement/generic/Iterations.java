/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.generic;

import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.type.types.Int;

/**
 *
 */
public class Iterations implements Measurement<Int> {
    private static final long serialVersionUID = -1689111168205874937L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterations getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int getValue(Algorithm algorithm) {
        return Int.valueOf(algorithm.getIterations());
    }

}
