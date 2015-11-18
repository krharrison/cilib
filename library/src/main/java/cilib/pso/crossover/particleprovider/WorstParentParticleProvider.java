/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.crossover.particleprovider;

import fj.data.List;
import cilib.pso.particle.Particle;
import cilib.util.selection.Samples;
import cilib.util.selection.recipes.ElitistSelector;

/**
 * This ParticleProvider selects a particle's worst parent to be replaced.
 */
public class WorstParentParticleProvider extends ParticleProvider {
    @Override
    public Particle f(List<Particle> parents, Particle offspring) {
        return new ElitistSelector<Particle>().on(parents).select(Samples.last()).get(0);
    }
}
