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
 * <p><b>Levy.</b></p>
 *
 * R(-10, 10)^30
 *
 */
public class Levy extends ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double firstTerm = Math.pow(Math.sin(3 * Math.PI * input.doubleValueOf(0)), 2);

        double secondTerm = 0;
        for(int i = 0; i < input.size() - 1; i++) {
            double x1 = input.doubleValueOf(i);
            double x2 = input.doubleValueOf(i + 1);

            secondTerm += Math.pow(x1 - 1, 2)
                * (1 + Math.pow(Math.sin(3 * Math.PI * x2), 2));
        }

        double thirdTerm = (input.doubleValueOf(input.size() - 1) - 1)
            * (1 + Math.pow(Math.sin(2 * Math.PI * input.doubleValueOf(input.size() - 1)), 2));

        return firstTerm + secondTerm + thirdTerm;
    }
}
