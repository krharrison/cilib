/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.dynamic.moo.fda2;

import cilib.functions.ContinuousFunction;
import cilib.type.types.container.Vector;

/**
 * This function is the g function of the FDA2 problem defined on page 429 in the following paper:
 * M.Farina, K.Deb, P.Amato. Dynamic multiobjective optimization problems: test cases, approximations
 * and applications, IEEE Transactions on Evolutionary Computation, 8(5): 425-442, 2003
 *
 */
public class FDA2_g extends ContinuousFunction {

    private static final long serialVersionUID = 8726700022515610264L;

    //Domain("R(-1, 1)^15")

    /**
     * Evaluates the function.
     */
    @Override
    public Double f(Vector input) {
        double sum = 1.0;

        for (int k=0; k < input.size(); k++) {
            sum += Math.pow(input.doubleValueOf(k), 2);
        }

        return sum;
    }

}
