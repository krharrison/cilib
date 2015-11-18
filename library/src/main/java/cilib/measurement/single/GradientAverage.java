/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.measurement.Measurement;
import cilib.type.types.Real;
import cilib.algorithm.Algorithm;
import cilib.functions.Gradient;
import cilib.problem.FunctionOptimisationProblem;
import cilib.problem.Problem;
import cilib.problem.solution.OptimisationSolution;
import cilib.type.types.container.Vector;


/**
 * This measurement returns for every iterations, the average of the gradient vector's 
 * components for the best particle in a neighbourhood
 * @author florent
 */
public class GradientAverage implements Measurement<Real> {
    
    @Override
    public GradientAverage getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
       OptimisationSolution best = algorithm.getBestSolution();
        Problem d = algorithm.getOptimisationProblem();
        FunctionOptimisationProblem fop = (FunctionOptimisationProblem)d;
        Gradient df = (Gradient)fop.getFunction();
        
        return Real.valueOf(df.getAverageGradientVector((Vector)best.getPosition()));
    }
    
}
