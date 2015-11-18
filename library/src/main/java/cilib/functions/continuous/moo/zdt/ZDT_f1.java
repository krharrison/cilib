/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.moo.zdt;

import cilib.functions.ContinuousFunction;
import cilib.type.types.container.Vector;

/**
 *
 */
final class ZDT_f1 extends ContinuousFunction {

    private static final long serialVersionUID = 921516091265963637L;

    ZDT_f1() { }

    @Override
    public Double f(Vector input) {
        return input.doubleValueOf(0);
    }
}
