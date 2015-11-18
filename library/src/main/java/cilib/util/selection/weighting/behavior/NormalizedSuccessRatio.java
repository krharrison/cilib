/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.util.selection.weighting.behavior;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.entity.behaviour.Behaviour;

/**
 * Obtains the ratio of the ParticleBehavior based on its successes normalized by
 * the number of iterations in which it was used.
 */
public class NormalizedSuccessRatio implements ParticleBehaviorRatio {
	private ControlParameter unusedReplacement;
	
	public NormalizedSuccessRatio(){
		unusedReplacement = ConstantControlParameter.of(0);
	}
	
	public NormalizedSuccessRatio(NormalizedSuccessRatio copy){
		this.unusedReplacement = copy.unusedReplacement;
	}
	
	public NormalizedSuccessRatio getClone(){
		return new NormalizedSuccessRatio(this);
	}
	
	public void setUnusedReplacement(ControlParameter value){
		unusedReplacement = value;
	}
	
    @Override
    public double getRatio(Behaviour particleBehavior) {
    	int iterationCounter = particleBehavior.getIterationCounter();
    	//normalize the success counter based on the number of iterations counter. If it hasn't been used, assignthe unusedReplacement value
    	double temp = iterationCounter > 0 ? particleBehavior.getSuccessCounter() / (double)iterationCounter : unusedReplacement.getParameter();

    	//added to prevent crashing when no behaviour had a success (i.e., all weights would be 0).
    	return temp + Double.MIN_VALUE;
    }
}
