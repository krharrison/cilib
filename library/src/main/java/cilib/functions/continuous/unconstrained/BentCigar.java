/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.unconstrained;

import cilib.functions.ContinuousFunction;
import cilib.type.types.container.Vector;

/**
 * <p><b>Bent Cigar.</b></p>
 *
 * <p>
 * Reference:
 * </p>
 * <p>
 * Liang, J. J., B. Y. Qu, and P. N. Suganthan.
 * "Problem Definitions and Evaluation Criteria for the CEC 2013 Special Session
 * on Real-Parameter Optimization." (2013).
 * </p>
 *
 */
public class BentCigar extends ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double sum = 0;

        for (int i = 1; i < input.size(); i++) {
            sum += input.doubleValueOf(i) *
                input.doubleValueOf(i);
        }

        return input.doubleValueOf(0) * input.doubleValueOf(0)
            + 1E6 * sum;
    }
}
