/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.guideprovider;

import cilib.algorithm.AbstractAlgorithm;
import cilib.entity.Topologies;
import cilib.entity.comparator.SocialBestFitnessComparator;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.type.types.container.StructuredType;

public class GBestGuideProvider implements GuideProvider {

    public GBestGuideProvider getClone() {
        return this;
    }

    public StructuredType get(Particle particle) {
        PSO pso = (PSO) AbstractAlgorithm.get();
        fj.data.List<Particle> topology = pso.getTopology();
        Particle gbest = Topologies.getBestEntity(topology, new SocialBestFitnessComparator());

        return gbest.getBestPosition();
    }

}
