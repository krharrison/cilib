/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.crossover.velocityprovider;

import fj.F;
import java.util.List;
import cilib.pso.particle.Particle;
import cilib.type.types.container.StructuredType;
import cilib.type.types.container.Vector;
import cilib.util.Vectors;

/**
 * This OffspringVelocityProvider sets an offspring's velocity to the average
 * velocity of its parents.
 */
public class AverageParentsOffspringVelocityProvider extends OffspringVelocityProvider {
    @Override
    public StructuredType f(List<Particle> parent, Particle offspring) {
        return Vectors.mean(fj.data.List.iterableList(parent).map(new F<Particle, Vector>() {
            @Override
            public Vector f(Particle f) {
                return (Vector) f.getVelocity();
            }
        })).valueE("");
    }
}
