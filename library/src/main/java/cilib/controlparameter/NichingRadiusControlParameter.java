/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.controlparameter;

import cilib.algorithm.AbstractAlgorithm;
import cilib.algorithm.Algorithm;
import cilib.functions.NichingFunction;
import cilib.problem.FunctionOptimisationProblem;
import cilib.problem.Problem;
import cilib.tuning.TuningProblem;

public class NichingRadiusControlParameter implements ControlParameter {
	
	private double defaultRadius = 0.01;

    @Override
    public double getParameter() {
    	Algorithm alg = AbstractAlgorithm.get();
    	Problem prob = alg.getOptimisationProblem();
    	
        if (prob instanceof TuningProblem) {
    		prob = ((TuningProblem) prob).getCurrentProblem();
    	}
    	
    	if (((FunctionOptimisationProblem) prob).getFunction() instanceof NichingFunction) {
    		return ((NichingFunction) ((FunctionOptimisationProblem) prob).getFunction()).getNicheRadius();
    	} else {
            System.out.println("Warning: using default niching radius. Function is not of type NichingFunction.");
			return defaultRadius;
        }
    }

    @Override
    public double getParameter(double min, double max) {
        return getParameter();
    }

    @Override
    public ControlParameter getClone() {
        return this;
    }
    
    public void setDefaultRadius(double defaultRadius) {
		this.defaultRadius = defaultRadius;
	}

}
