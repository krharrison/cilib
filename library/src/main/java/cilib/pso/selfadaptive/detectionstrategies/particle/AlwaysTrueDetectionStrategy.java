/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.detectionstrategies.particle;

import cilib.pso.particle.Particle;

public class AlwaysTrueDetectionStrategy implements ParticleUpdateDetectionStrategy {

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
    public boolean detect(Particle entity) {
        return true;
    }


}
