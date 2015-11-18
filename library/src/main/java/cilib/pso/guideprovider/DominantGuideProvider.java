/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.guideprovider;

import fj.data.List;
import cilib.algorithm.AbstractAlgorithm;
import cilib.pso.particle.Particle;
import cilib.entity.Topologies;
import cilib.entity.comparator.DominantFitnessComparator;
import cilib.pso.PSO;
import cilib.type.types.container.StructuredType;

/**
 *
 */
public class DominantGuideProvider implements GuideProvider {

    @Override
    public DominantGuideProvider getClone() {
        return this;
    }

    @Override
    public StructuredType get(Particle particle) {
        PSO pso = (PSO) AbstractAlgorithm.get();
        List<Particle> topology = pso.getTopology();
        Particle gbest = Topologies.getBestEntity(topology, new DominantFitnessComparator());

        return gbest.getBestPosition();
    }

}
