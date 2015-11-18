/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.crossover.pbestupdate;

import java.util.List;
import cilib.pso.particle.Particle;
import cilib.type.types.container.StructuredType;

/**
 * This OffspringPBestProvider sets an offspring's pBest to its current position.
 */
public class CurrentPositionOffspringPBestProvider extends OffspringPBestProvider {
    @Override
    public StructuredType f(List<Particle> parent, Particle offspring) {
        return offspring.getPosition();
    }
}
