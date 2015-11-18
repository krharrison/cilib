/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.unconstrained;

import cilib.functions.ContinuousFunction;
import cilib.functions.Differentiable;
import cilib.type.types.Numeric;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;
import fj.F;

/**
 * <p>Spherical function.</p>
 *
 * <p><b>Reference:</b> X. Yao, Y. Liu, G. Liu, <i>Evolutionary Programming
 * Made Faster</i>,  IEEE Transactions on Evolutionary Computation,
 * 3(2):82--102, 1999</p>
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Continuous</li>
 * <li>Separable</li>
 * <li>Regular</li>
 * <li>Convex</li>
 * </ul>
 * </p>
 *
 * f(x) = 0; x = (0,0,...,0)
 * x e [-5.12, 5.12]
 *
 * R(-5.12, 5.12)^30
 *
 */
public class Spherical extends ContinuousFunction implements Differentiable {

    private static final long serialVersionUID = 5811377575647995206L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        return input.foldLeft(0.0, new F<Numeric, Double>() {
            @Override
            public Double f(Numeric x) {
                return x.doubleValue() * x.doubleValue();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getGradient(Vector x) {
        return x.map(new F<Numeric, Numeric>() {
            @Override
            public Numeric f(Numeric x) {
                return Real.valueOf(x.doubleValue() * 2);
            }
        });
    }
}
