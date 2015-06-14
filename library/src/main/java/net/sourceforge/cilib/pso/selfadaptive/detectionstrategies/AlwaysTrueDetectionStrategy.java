/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive.detectionstrategies;

import net.sourceforge.cilib.pso.particle.Particle;

/**
 * Detection strategy that always return true, allowing a particle to update its parameters
 * every iteration.
 */
public class AlwaysTrueDetectionStrategy implements ParameterUpdateTriggerDetectionStrategy {

	@Override
	public boolean detect(Particle particle) {
		return true;
	}

	@Override
	/**
	 * Clone this detection strategy by simply returning a new one.
	 */
	public AlwaysTrueDetectionStrategy getClone() {
		return new AlwaysTrueDetectionStrategy();
	}


}
