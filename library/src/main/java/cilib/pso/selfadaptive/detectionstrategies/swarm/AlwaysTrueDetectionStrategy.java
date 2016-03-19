/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.detectionstrategies.swarm;

import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.selfadaptive.detectionstrategies.particle.ParticleUpdateDetectionStrategy;

public class AlwaysTrueDetectionStrategy implements SwarmUpdateDetectionStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public AlwaysTrueDetectionStrategy getClone() {
        return new AlwaysTrueDetectionStrategy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean detect(PSO pso) {
        return true;
    }
}
