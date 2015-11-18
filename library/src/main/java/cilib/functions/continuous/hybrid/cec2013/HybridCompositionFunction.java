/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.hybrid.cec2013;

import java.util.ArrayList;
import java.util.List;

import cilib.functions.ContinuousFunction;
import cilib.type.types.container.Vector;

/**
 * Implementation to create hybrid composition functions from the CEC2013 benchmark functions.
 * <p>
 * Reference:
 * </p>
 * <p>
 * Liang, J. J., B. Y. Qu, and P. N. Suganthan.
 * "Problem Definitions and Evaluation Criteria for the CEC 2013 Special Session
 * on Real-Parameter Optimization." (2013).
 * </p>
 */
public class HybridCompositionFunction extends ContinuousFunction {

    private final List<SingleFunction> functions;

    public HybridCompositionFunction() {
        this.functions = new ArrayList();
    }

    @Override
    public Double f(Vector input) {
        double totalWeight = 0;
        for (SingleFunction s : functions) {
            totalWeight += s.getWeight(input);
        }

        double sum = 0;
        for (SingleFunction s : functions) {
            double weight = totalWeight == 0
                ? (1.0 / input.size())
                : (s.getWeight(input) / totalWeight);

            sum += weight * s.f(input);
        }

        return sum;
    }

    /**
     * Adds a function to be composed.
     * @param function
     */
    public void addFunction(SingleFunction function) {
        functions.add(function);
    }
}
