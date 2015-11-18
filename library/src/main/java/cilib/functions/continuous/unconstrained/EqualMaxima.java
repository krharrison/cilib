/**
 * __ __ _____ _/ /_/ /_ Computational Intelligence Library (CIlib) / ___/ / / /
 * __ \ (c) CIRG @ UP / /__/ / / / /_/ / http://cilib.net \___/_/_/_/_.___/
 */
package cilib.functions.continuous.unconstrained;

import cilib.functions.ContinuousFunction;
import cilib.functions.Gradient;
import cilib.functions.NichingFunction;
import cilib.type.types.Numeric;
import cilib.type.types.container.Vector;

/**
 *This is a maximisation problem
 * Minimum: 0.0 R(0, 1)^1
 *
 */
public class EqualMaxima extends ContinuousFunction implements Gradient, NichingFunction {

    private static final long serialVersionUID = -5261002551096587662L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double sum = 0.0;
        for (int i = 0; i < input.size(); ++i) {
            sum += Math.pow(Math.sin(5.0 * Math.PI * input.doubleValueOf(i)), 6.0);
        }
        return sum;

    }

    public double getAverageGradientVector(Vector x) {
        double sum = 0;
        for (Numeric n : getGradientVector(x)) {
            sum += n.doubleValue();
        }
        return sum / x.size();
    }

    public double getGradientVectorLength(Vector x) {
        return getGradientVector(x).length();
    }

    public Vector getGradientVector(Vector x) {
        Vector.Builder vectorBuilder = Vector.newBuilder();

        for (int i = 0; i < x.size(); ++i) {
            vectorBuilder.add(30.0 * Math.PI * Math.pow(Math.sin(5.0 * Math.PI * x.doubleValueOf(i)), 5.0) * Math.cos(5.0 * Math.PI * x.doubleValueOf(i)));
        }

        return vectorBuilder.build();
    }

    @Override
    public double getNicheRadius() {
        return 0.01;
    }
}
