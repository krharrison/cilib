/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.niching.merging.detection;

import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Topologies;
import cilib.entity.comparator.SocialBestFitnessComparator;
import cilib.pso.particle.Particle;
import cilib.type.types.container.Vector;

public class VectorBasedMergeDetection extends MergeDetection {

    @Override
    public Boolean f(SinglePopulationBasedAlgorithm a, SinglePopulationBasedAlgorithm b) {
        Particle p1 = (Particle) Topologies.getBestEntity(a.getTopology(), new SocialBestFitnessComparator());
        Particle p2 = (Particle) Topologies.getBestEntity(b.getTopology(), new SocialBestFitnessComparator());
        Vector v1 = ((Vector) p1.getBestPosition()).subtract((Vector) p1.getPosition());
        Vector v2 = ((Vector) p2.getBestPosition()).subtract((Vector) p2.getPosition());

        return v1.dot(v2) > 0;
   }

}
