/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.measurement.Measurement;
import cilib.algorithm.Algorithm;
import cilib.functions.Gradient;
import cilib.problem.FunctionOptimisationProblem;
import cilib.problem.Problem;
import cilib.problem.solution.OptimisationSolution;
import cilib.type.types.StringType;
import cilib.type.types.container.Vector;
/**
 *
 * @author florent
 */
public class GradientVector implements Measurement<StringType>  {
    
    @Override
    public GradientVector getClone() {
        return this;
    }
    
    @Override
    public StringType getValue(Algorithm algorithm) {
       OptimisationSolution best = algorithm.getBestSolution();
        Problem d = algorithm.getOptimisationProblem();
        FunctionOptimisationProblem fop = (FunctionOptimisationProblem)d;
        Gradient df = (Gradient)fop.getFunction();
        
        return new StringType((df.getGradientVector((Vector)best.getPosition())).toString());
    }
}
