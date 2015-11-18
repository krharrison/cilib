/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.unconstrained;

import cilib.functions.ContinuousFunction;
import cilib.type.types.container.Vector;

import com.google.common.base.Preconditions;

/**
 * Schaffer's second function.
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-100, 100]</li>
 * </ul>
 * </p>
 *
 * R(-100.0,100.0)^2
 */
public class Schaffer2 extends ContinuousFunction {

    private static final long serialVersionUID = 7289010453718555694L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "Schaffer2 function is only defined for 2 dimensions");

        double sum_squares = input.doubleValueOf(0) * input.doubleValueOf(0) + input.doubleValueOf(1) * input.doubleValueOf(1);
        double term1 = Math.pow(sum_squares, 0.25);
        double term2 = 50 * Math.pow(sum_squares, 0.1) + 1;
        return term1 * term2;
    }
}
