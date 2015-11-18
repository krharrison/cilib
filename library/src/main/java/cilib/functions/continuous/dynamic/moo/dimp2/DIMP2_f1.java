/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.dynamic.moo.dimp2;

import cilib.functions.ContinuousFunction;
import cilib.type.types.container.Vector;

/**
 * This function is the f1 function of the DIMP2 problem defined in the following paper:
 * W.T. Koo and C.K. Goh and K.C. Tan. A predictive gradien strategy for multiobjective
 * evolutionary algorithms in a fast changing environment, Memetic Computing, 2:87-110,
 * 2010.
 *
 */

public class DIMP2_f1 extends ContinuousFunction {

    //Domain("R(0, 1)")

    /**
     * Evaluates the function.
     */
    @Override
    public Double f(Vector x) {
        double value = Math.abs(x.doubleValueOf(0));
        if (value > 1.0)
            value = ((double)value) - 1.0;
        return value;
    }
}
