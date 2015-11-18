/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.decorators;

import cilib.functions.ContinuousFunction;
import cilib.type.types.container.Vector;

/**
 * <p>
 * Composes two given functions as per F13 and F14 in the CEC 2005 benchmark functions.
 * </p>
 * <p>
 * returns F_outer(F_inner(input))
 * </p>
 */
public class CompositeFunctionDecorator extends ContinuousFunction {
    private ContinuousFunction innerFunction;
    private ContinuousFunction outerFunction;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        return outerFunction.f(Vector.of(innerFunction.f(input)));
    }

    /**
     * Get the decorated function.
     * @return The decorated function.
     */
    public ContinuousFunction getInnerFunction() {
        return innerFunction;
    }

    /**
     * Set the function that is to be decorated.
     * @param function The function to decorated.
     */
    public void setInnerFunction(ContinuousFunction function) {
        this.innerFunction = function;
    }

    /**
     * Get the decorated function.
     * @return The decorated function.
     */
    public ContinuousFunction getOuterFunction() {
        return outerFunction;
    }

    /**
     * Set the function that is to be decorated.
     * @param function The function to decorated.
     */
    public void setOuterFunction(ContinuousFunction function) {
        this.outerFunction = function;
    }
}
