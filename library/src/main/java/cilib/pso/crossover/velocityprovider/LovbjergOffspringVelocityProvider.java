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
 * This OffspringVelocityProvider calculates an offspring's velocity according to
 * Lovbjerg et al's hybrid PSO:
 * <p>
 * @INPROCEEDINGS{L??vbjerg01hybridparticle,
 *   author = {Morten L??vbjerg and Thomas Kiel Rasmussen and Thiemo Krink},
 *   title = {Hybrid Particle Swarm Optimiser with Breeding and Subpopulations},
 *   booktitle = {Proceedings of the Genetic and Evolutionary Computation Conference (GECCO-2001},
 *   year = {2001},
 *   pages = {469--476},
 *   publisher = {Morgan Kaufmann}
 * }
 * </p>
 */
public class LovbjergOffspringVelocityProvider extends OffspringVelocityProvider {
    @Override
    public StructuredType f(List<Particle> parent, Particle offspring) {
        Vector velocity = (Vector) offspring.getVelocity();

        return Vectors.sumOf(fj.data.List.iterableList(parent).map(new F<Particle, Vector>() {
            @Override
            public Vector f(Particle f) {
                return (Vector) f.getVelocity();
            }
        })).valueE("Cannot sum vectors").normalize().multiply(velocity.length());
    }
}
