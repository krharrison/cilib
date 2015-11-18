/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import fj.F;
import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.type.types.Real;
import cilib.problem.FunctionOptimisationProblem;
import cilib.functions.KnownOptimum;
import cilib.type.types.container.Vector;

/*
 * Measurement to obtain the known optimum value of a function
 * if it exists.
 */
public class FunctionOptimum implements Measurement<Real> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FunctionOptimum getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        F<Vector, ? extends Number> f = ((FunctionOptimisationProblem)algorithm.getOptimisationProblem()).getFunction();
        return Real.valueOf(((KnownOptimum<Double>)f).getOptimum());
    }

}
