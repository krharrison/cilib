/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single.dynamic;

import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.problem.DynamicOptimisationProblem;
import cilib.type.types.Real;

public class Error implements Measurement<Real> {

    public Measurement<Real> getClone() {
        return this;
    }

    public Real getValue(Algorithm algorithm) {
        DynamicOptimisationProblem function = (DynamicOptimisationProblem) algorithm.getOptimisationProblem();
        return Real.valueOf(function.getError(algorithm.getBestSolution().getPosition()));
    }

}
