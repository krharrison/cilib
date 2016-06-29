/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies.particle;

import cilib.pso.PSO;
import cilib.pso.particle.Particle;

public class DoNothingParticleAdaptationStrategy implements ParticleAdaptationStrategy {

    /**
     * Adapt the control parameters of all {@link Particle} entities in the swarm.
     *
     * @param algorithm The swarm which is to be adapted.
     */
    public void adapt(Particle p, PSO algorithm){
        //D nothing
    }

    /**
     * Clone the current {@link DoNothingParticleAdaptationStrategy}.
     *
     * @return A clone of this {@link DoNothingParticleAdaptationStrategy}.
     */
    public DoNothingParticleAdaptationStrategy getClone(){return this;}


}
