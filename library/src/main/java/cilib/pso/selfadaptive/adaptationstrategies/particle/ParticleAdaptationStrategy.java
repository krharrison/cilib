/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies.particle;

import cilib.pso.PSO;
import cilib.pso.particle.Particle;


public interface ParticleAdaptationStrategy {
    /**
     * Adapt the control parameters of the provided {@link Particle}.
     *
     * @param particle  The particle to adapt.
     * @param algorithm The algorithm to which this particle belongs
     */
    void adapt(Particle particle, PSO algorithm);

    /**
     * Clone the current {@link ParticleAdaptationStrategy}.
     *
     * @return A clone of this {@link ParticleAdaptationStrategy}.
     */
    ParticleAdaptationStrategy getClone();
}
