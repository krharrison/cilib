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
import cilib.entity.comparator.NonDominatedFitnessComparator;
import cilib.pso.PSO;
import cilib.type.types.container.StructuredType;

/**
 *
 */
public class NonDominatedGuideProvider implements GuideProvider {

    @Override
    public NonDominatedGuideProvider getClone() {
        return this;
    }

    @Override
    public StructuredType get(Particle particle) {
        PSO pso = (PSO) AbstractAlgorithm.get();
        List<Particle> topology = pso.getTopology();
        Particle gbest = Topologies.getBestEntity(topology, new NonDominatedFitnessComparator());

        return gbest.getBestPosition();
    }

}
