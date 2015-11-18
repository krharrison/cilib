/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.decorators;

import cilib.functions.ContinuousFunction;
import cilib.type.types.container.Vector;
import fj.F;

/**
 * Function implementation to accept a function and to return the reciprocal
 * of that function value.
 *
 */
public class InvertedFunctionDecorator extends ContinuousFunction {

    private static final long serialVersionUID = -7506823207533866371L;
    private F<Vector, Double> function;

    @Override
    public Double f(Vector input) {
        double innerFunctionValue = function.f(input);

        if (innerFunctionValue == 0) {
            throw new ArithmeticException("Inner function evaluation equated to 0. Division by zero is undefined");
        }

        return (1.0 / innerFunctionValue);
    }

    public F<Vector, Double> getFunction() {
        return function;
    }

    public void setFunction(F<Vector, Double> function) {
        this.function = function;
    }
}
