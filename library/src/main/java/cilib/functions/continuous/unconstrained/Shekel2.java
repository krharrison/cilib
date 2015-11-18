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
 * Shekel2 (also Foxhole) function.
 *
 * R(-65.536, 65.536)^2
 * Minimum: 0.9980038
 *
 */
public class Shekel2 extends ContinuousFunction {

    private final double[][] a = new double[2][25];

    public Shekel2() {
        int index = 0;
        for (int j = -32; j <= 32; j += 16) {
            for (int i = -32; i <= 32; i += 16) {
                a[0][index] = i;
                a[1][index] = j;
                index++;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "Shekel2 function is only defined for 2 dimensions");

        double resultI = 0.0;
        for (int i = 1; i <= 25; i++) {
            double resultJ = 0.0;
            for (int j = 0; j < 2; j++) {
                resultJ += Math.pow(input.doubleValueOf(j) - a[j][i-1], 6);
            }
            resultJ = i + resultJ;
            resultI += 1 / resultJ;
        }

        return 1.0 / (resultI + 0.002);
    }
}
