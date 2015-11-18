/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.guideprovider;

import cilib.algorithm.AbstractAlgorithm;
import cilib.math.random.generator.Rand;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.type.types.container.StructuredType;

/**
 * A concrete implementation of {@link GuideProvider} where the neighbourhood
 * best position of a random particle gets selected as a guide.
 */
public class RandomGuideProvider implements GuideProvider {

    public RandomGuideProvider() {}

    @Override
    public RandomGuideProvider getClone() {
        return this;
    }

    @Override
    public StructuredType get(Particle particle) {
        fj.data.List<Particle> topology = ((PSO) AbstractAlgorithm.get()).getTopology();
        return topology.index(Rand.nextInt(topology.length())).getBestPosition();
    }
}
