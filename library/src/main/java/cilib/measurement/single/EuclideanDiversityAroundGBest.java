/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Entity;
import cilib.measurement.Measurement;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;
import cilib.util.distancemeasure.DistanceMeasure;
import cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * TODO: Complete this javadoc.
 */
public class EuclideanDiversityAroundGBest implements Measurement<Real> {

    private static final long serialVersionUID = 8221420456303029095L;

    @Override
    public EuclideanDiversityAroundGBest getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        SinglePopulationBasedAlgorithm populationBasedAlgorithm = (SinglePopulationBasedAlgorithm) algorithm;

        Vector center = (Vector) algorithm.getBestSolution().getPosition();
        DistanceMeasure distance = new EuclideanDistanceMeasure();
        double diameter = 0;

        fj.data.List<Entity> topology = populationBasedAlgorithm.getTopology();
        for (Entity entity : topology) {
            diameter += distance.distance(center, (Vector) entity.getPosition());
        }

        return Real.valueOf(diameter / topology.length());
    }
}
