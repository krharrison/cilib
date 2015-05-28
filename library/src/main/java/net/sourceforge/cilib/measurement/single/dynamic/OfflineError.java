/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.dynamic;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.DynamicOptimisationProblem;
import net.sourceforge.cilib.type.types.Real;

/**
 * OfflineError computes the average over the error of the best solution 
 * found since the last environmental change occurred.
 *
 * NOTE: For this measurement to be used, a resolution of 1 has to be set for
 * the measurement.
 */

public class OfflineError implements Measurement<Real> {

	private double sum;
	private int cycleSize;
	private int iterationsSinceLastChange;
	
	public OfflineError(){
		sum = 0;
		cycleSize = 50;
		iterationsSinceLastChange = 0;
	}
	
    public Measurement<Real> getClone() {
        return this;
    }

    public Real getValue(Algorithm algorithm) {
        DynamicOptimisationProblem function = (DynamicOptimisationProblem) algorithm.getOptimisationProblem();
        
        iterationsSinceLastChange++;
        double currentError = function.getError(algorithm.getBestSolution().getPosition());
        sum += currentError;
        
        double result = sum / iterationsSinceLastChange;

        //if a change occurred, reset the sum
        if(algorithm.getIterations() % cycleSize == 0){
        	sum = 0;
        	iterationsSinceLastChange = 0;
        }
        
        return Real.valueOf(result);
    }
    
    public void setCycleSize(int cycleSize){
    	this.cycleSize = cycleSize;
    }
    
    public int getCycleSize(){
    	return this.cycleSize;
    }
}