/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.decorators;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.functions.ContinuousFunction;
import cilib.type.types.container.Vector;

/**
 * ShiftedFunctionDecorator.
 *
 * Characteristics:
 *
 * Let c be a fixed positive number, then:
 *
 * Horizontal Shift:
 * If g(x) = f(x-c), then
 *             (c > 0) means that g(x) is f(x) shifted c units to the right
 *             (c < 0) means that g(x) is f(x) shifted c units to the left
 *
 * Vertical Shift:
 * If g(x) = f(x) + c, then
 *             (c > 0) means that g(x) is f(x) shifted c units upwards
 *             (c < 0) means that g(x) is f(x) shifted c units downwards
 *
 */
public class ShiftedFunctionDecorator extends ContinuousFunction {

    private static final long serialVersionUID = 8687711759870298103L;
    private ContinuousFunction function;
    private ControlParameter verticalShift;
    private ControlParameter horizontalShift;
    private Vector shiftVector;
    private boolean randomShift;

    public ShiftedFunctionDecorator() {
        this.verticalShift = ConstantControlParameter.of(0.0);
        this.horizontalShift = ConstantControlParameter.of(0.0);
        this.shiftVector = null;
        this.randomShift = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        shiftVector = randomShift
                ? (shiftVector == null || input.size() != shiftVector.size()) ? Vector.newBuilder().copyOf(input).buildRandom() : shiftVector
                : Vector.fill(horizontalShift.getParameter(), input.size());

        return function.f(input.subtract(shiftVector)) + verticalShift.getParameter();
    }

    /**
     * @return the function
     */
    public ContinuousFunction getFunction() {
        return function;
    }

    /**
     * @param function the function to set
     */
    public void setFunction(ContinuousFunction function) {
        this.function = function;
    }

    /**
     * Get the horizontal shift (X-axis) associated with this function.
     * @return The horizontal shift in the X-axis
     */
    public ControlParameter getHorizontalShift() {
        return horizontalShift;
    }

    /**
     * Set the amount of horizontal shift to be applied to the function during evaluation.
     * @param horizontalShift The amount of horizontal shift.
     */
    public void setHorizontalShift(ControlParameter horizontalShift) {
        this.horizontalShift = horizontalShift;
    }

    /**
     * Get the vertical shift (Y-axis) associated with this function.
     * @return The vertical shift in the Y-axis
     */
    public ControlParameter getVerticalShift() {
        return verticalShift;
    }

    /**
     * Set the amount of vertical shift to be applied to the function during evaluation.
     * @param verticalShift the amount of vertical shift.
     */
    public void setVerticalShift(ControlParameter verticalShift) {
        this.verticalShift = verticalShift;
    }

    public void setRandomShift(boolean randomShift) {
        this.randomShift = randomShift;
    }

    public boolean getRandomShift() {
        return randomShift;
    }
}
