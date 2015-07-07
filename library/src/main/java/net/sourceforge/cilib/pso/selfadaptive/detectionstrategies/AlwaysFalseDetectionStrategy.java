/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive.detectionstrategies;

import net.sourceforge.cilib.pso.particle.Particle;

/**
 * Detection strategy that always return false, preventing a particle from updating its 
 * parameters
 */
public class AlwaysFalseDetectionStrategy implements ParameterUpdateTriggerDetectionStrategy {

	@Override
	public boolean detect(Particle particle) {
		return false;
	}

	@Override
	/**
	 * Clone this detection strategy by simply returning a new one.
	 */
	public AlwaysFalseDetectionStrategy getClone() {
		return new AlwaysFalseDetectionStrategy();
	}


}
