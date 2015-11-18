/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.crossover.velocityprovider;

import java.util.List;
import cilib.pso.crossover.particleprovider.WorstParentParticleProvider;
import cilib.pso.particle.Particle;
import cilib.type.types.container.StructuredType;

public class WorstParentOffspringVelocityProvider extends OffspringVelocityProvider {
    @Override
    public StructuredType f(List<Particle> parent, Particle offspring) {
        return new WorstParentParticleProvider().f(fj.data.List.iterableList(parent), offspring).getVelocity();
    }
}
