/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.activation;

import cilib.type.types.Real;
import cilib.type.types.container.Vector;

/**
 * The linear activation function, f(x) = x; f '(x) = 1; Since it
 * is unbounded, the linear function has no active range, and
 * these values are set to positive and negative max double.
 */
public class Linear extends ActivationFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Real f(Real input) {
        return input;
    }

    /**
     * {@inheritDoc}
     */
    public double f(double input) {
        return input;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getGradient(Vector x) {
        return Vector.of(1.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getGradient(double number) {
        return 1.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLowerActiveRange() {
        return -Double.MAX_VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getUpperActiveRange() {
        return Double.MAX_VALUE;
    }

    @Override
    public Linear getClone() {
        return this;
    }
}
