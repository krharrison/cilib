/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.hybrid;

import java.util.ArrayList;
import java.util.List;

import cilib.functions.ContinuousFunction;
import cilib.type.types.container.Vector;

/**
 * Implementation to create a simple hybrid function that is composed of
 * several continuous functions. The hybrid function value is the sum
 * of the individual function values.
 */
public class SimpleHybridFunction extends ContinuousFunction {

    private final List<ContinuousFunction> functions;

    public SimpleHybridFunction() {
        this.functions = new ArrayList<ContinuousFunction>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double result = 0.0;

        for(ContinuousFunction function : functions) {
            result += function.f(input);
        }

        return result;
    }

   /**
     * Adds a function to the hybrid function.
     * @param function
     */
    public void addFunction(ContinuousFunction function) {
        functions.add(function);
    }
}
