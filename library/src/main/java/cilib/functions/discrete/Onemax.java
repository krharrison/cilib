/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.discrete;

import cilib.functions.ContinuousFunction;
import cilib.type.types.container.Vector;


/**
 * Implementation of the Onemax (counting ones) problem.
 * Intended for bit strings of arbitrary length
 *
**/
public class Onemax extends ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double result = 0.0;

        for (int i = 0; i < input.size(); i++) {
            if(input.booleanValueOf(i)) {
                result++;
            }
        }

        return result;
    }
}
