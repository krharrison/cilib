/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive.adaptationstrategies;

import net.sourceforge.cilib.entity.behaviour.Behaviour;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * An interface to adapt the control parameters of a {@link Particle}.
 */
public interface AdaptationStrategy {
	
    /**
     * Adapt the control parameters of the provided {@link Particle}.
     *
     * @param particle  The particle to adapt.
     * @param algorithm The algorithm to which this particle belongs
     */
	Behaviour adapt(Particle particle, PSO algorithm);
	
    /**
     * Clone the current {@link AdaptationStrategy}.
     *
     * @return A clone of this {@link AdaptationStrategy}.
     */
	AdaptationStrategy getClone();
}
