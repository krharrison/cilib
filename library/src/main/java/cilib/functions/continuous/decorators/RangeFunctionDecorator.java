/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.decorators;

import cilib.controlparameter.ControlParameter;
import cilib.functions.ContinuousFunction;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;

/**
 * Applies the decorated function to a specified range of the input vector.
 */
public class RangeFunctionDecorator extends ContinuousFunction {

    private ContinuousFunction function;
    private ControlParameter start;
    private ControlParameter end;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        int fromIndex = Real.valueOf(start.getParameter()).intValue();
        int toIndex = Real.valueOf(end.getParameter()).intValue();

        Vector rangedInput = input.copyOfRange(fromIndex, toIndex);

        return function.f(rangedInput);
    }

    /**
     * Get the decorated function.
     * @return The decorated function.
     */
    public ContinuousFunction getFunction() {
        return function;
    }

    /**
     * Set the function that is to be decorated.
     * @param function The function to decorated.
     */
    public void setFunction(ContinuousFunction function) {
        this.function = function;
    }

    /**
     * Get the start index of the range.
     * @return The start index.
     */
    public ControlParameter getStart() {
        return start;
    }

    /**
     * Set the start index of the range.
     * @param start The start index.
     */
    public void setStart(ControlParameter start) {
        this.start = start;
    }

    /**
     * Get the end index of the range.
     * @return The end index.
     */
    public ControlParameter getEnd() {
        return end;
    }

    /**
     * Set the end index of the range.
     * @param end The end index.
     */
    public void setEnd(ControlParameter end) {
        this.end = end;
    }
}
