/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.crossover.pbestupdate;

import java.util.List;
import cilib.algorithm.AbstractAlgorithm;
import cilib.pso.particle.Particle;
import cilib.type.types.container.StructuredType;
import cilib.type.types.container.Vector;

/**
 * This OffspringPBestProvider sets an offspring's pBest to the gBest.
 */
public class GBestOffspringPBestProvider extends OffspringPBestProvider {
    @Override
    public StructuredType f(List<Particle> parent, Particle offspring) {
        return (Vector) AbstractAlgorithm.get().getBestSolution().getPosition();
    }
}
