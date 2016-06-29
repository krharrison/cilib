/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies;

import cilib.pso.PSO;
import cilib.pso.particle.Particle;

public interface SwarmAdaptationStrategy {

    /**
     * Adapt the control parameters of all {@link Particle} entities in the swarm.
     *
     * @param algorithm The swarm which is to be adapted.
     */
    void adapt(PSO algorithm);

    /**
     * Clone the current {@link SwarmAdaptationStrategy}.
     *
     * @return A clone of this {@link SwarmAdaptationStrategy}.
     */
    SwarmAdaptationStrategy getClone();

    //TODO: define an interface method for iteration 0 to allow initialization

}
