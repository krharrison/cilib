/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.hpso.detectionstrategies;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ProportionalControlParameter;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.util.selection.weighting.behavior.NormalizedSuccessRatio;

/**
 * Detects the behaviour has stagnated if the normalized success ratio (successes / iterations) is less than 
 * some value. This is checked every windowSize iterations.
 */
public class BehaviourStagnationDetectionStrategy implements BehaviorChangeTriggerDetectionStrategy {

	private ControlParameter period;
	private ControlParameter threshold;
	private NormalizedSuccessRatio weighting;
	
    /**
     * Construct a new {@link PersonalBestStagnationDetectionStrategy} with a
     * default window size of 10.
     */
    public BehaviourStagnationDetectionStrategy() {
    	period = ConstantControlParameter.of(10);
    	threshold = new ProportionalControlParameter(0.5);
        weighting = new NormalizedSuccessRatio();
    }

    /**
     * Construct a copy of the given {@link PersonalBestStagnationDetectionStrategy}.
     *
     * @param copy the {@link PersonalBestStagnationDetectionStrategy} to copy.
     */
    public BehaviourStagnationDetectionStrategy(BehaviourStagnationDetectionStrategy copy) {
        this.period = copy.period.getClone();
        this.threshold = copy.threshold.getClone();
        this.weighting = copy.weighting;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BehaviourStagnationDetectionStrategy getClone() {
        return new BehaviourStagnationDetectionStrategy(this);
    }
    
	/**
	 *
	 */
	@Override
	public boolean detect(Particle particle) {
		int iters = AbstractAlgorithm.get().getIterations();

        if (iters % period.getParameter() == 0 && weighting.getRatio(particle.getBehaviour()) < threshold.getParameter()) {
        	return true;
        }
        
        return false;
	}
	
    public void setPeriod(ControlParameter period) {
        this.period = period;
    }
    
    public void setThreshold(ControlParameter threshold){
    	this.threshold = threshold;
    }

}
