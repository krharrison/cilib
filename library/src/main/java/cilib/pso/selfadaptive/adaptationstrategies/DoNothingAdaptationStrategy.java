/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies;

import cilib.pso.PSO;
import cilib.pso.particle.Particle;

public class DoNothingAdaptationStrategy implements SwarmAdaptationStrategy{

    /**
     * Adapt the control parameters of all {@link Particle} entities in the swarm.
     *
     * @param algorithm The swarm which is to be adapted.
     */
    public void adapt(PSO algorithm){
        //Do nothing
    }

    /**
     * Clone the current {@link DoNothingAdaptationStrategy}.
     *
     * @return A clone of this {@link DoNothingAdaptationStrategy}.
     */
    public DoNothingAdaptationStrategy getClone(){return this;}


}
