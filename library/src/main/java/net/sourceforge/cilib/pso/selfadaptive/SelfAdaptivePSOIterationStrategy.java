/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive;

import java.util.List;

import net.sourceforge.cilib.entity.behaviour.Behaviour;

public interface SelfAdaptivePSOIterationStrategy {
	
    static final double EPSILON = 0.000000001;
	
    /**
     * Get the current behaviour pool.
     * @return The current {@link List} of {@link ParticleBehaviour} objects.
     */
    public List<Behaviour> getBehaviourPool();

}
