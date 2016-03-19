/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;

public class ParameterBounds {
    private ControlParameter lowerBound;
    private ControlParameter upperBound;

    public ParameterBounds() {
        this.lowerBound = ConstantControlParameter.of(0.0);
        this.upperBound = ConstantControlParameter.of(1.0);
    }

    public ParameterBounds(double lBound, double uBound) {
        this.lowerBound = ConstantControlParameter.of(lBound);
        this.upperBound = ConstantControlParameter.of(uBound);
    }

    public ParameterBounds(ControlParameter lowerBound, ControlParameter upperBound){
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public boolean isInsideBounds(double value) {
        if (Double.compare(value, upperBound.getParameter()) <= 0 && Double.compare(value, lowerBound.getParameter()) >= 0) {
            return true;
        }

        return false;
    }

    public void setUpperBound(ControlParameter upperBound) {
        this.upperBound = upperBound;
    }

    public ControlParameter getUpperBound() {
        return upperBound;
    }

    public void setLowerBound(ControlParameter lowerBound) {
        this.lowerBound = lowerBound;
    }

    public ControlParameter getLowerBound() {
        return lowerBound;
    }

    public double getRange() {
        return upperBound.getParameter() - lowerBound.getParameter();
    }
}
