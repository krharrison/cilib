/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.entity.visitor;

import cilib.entity.Entity;
import cilib.measurement.single.diversity.centerinitialisationstrategies.CenterInitialisationStrategy;
import cilib.measurement.single.diversity.centerinitialisationstrategies.GBestCenterInitialisationStrategy;
import cilib.type.types.container.Vector;
import cilib.util.distancemeasure.DistanceMeasure;
import cilib.util.distancemeasure.EuclideanDistanceMeasure;
import fj.F2;

/**
 * Determine the radius of the current {@link Topology} centered on the given
 * {@link CenterInitialisationStrategy}.
 */
public class RadiusVisitor extends TopologyVisitor<Entity, Double> {

    private CenterInitialisationStrategy populationCenter;
    protected DistanceMeasure distanceMeasure;

    /**
     * Default constructor.
     */
    public RadiusVisitor() {
        this.populationCenter = new GBestCenterInitialisationStrategy();
        this.distanceMeasure = new EuclideanDistanceMeasure();
    }

    /**
     * Gets the strategy used for calculating the center of the topology.
     *
     * @return the {@linkplain CenterInitialisationStrategy}.
     */
    public CenterInitialisationStrategy getPopulationCenter() {
        return populationCenter;
    }

    /**
     * Sets the {@linkplain CenterInitialisationStrategy} to use for calculating
     * the center of the {@linkplain Topology}.
     *
     * @param centerCalculator  the {@linkplain CenterInitialisationStrategy} to
     *                          use.
     */
    public void setPopulationCenter(CenterInitialisationStrategy centerCalculator) {
        this.populationCenter = centerCalculator;
    }

    /**
     * Gets the distance measure used.
     *
     * @return the {@linkplain DistanceMeasure}.
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    /**
     * Sets the distance measure to use.
     *
     * @param distanceMeasure the {@linkplain DistanceMeasure} to use.
     */
    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    @Override
    public Double f(fj.data.List<Entity> topology) {
        final Vector center = populationCenter.getCenter(topology);

        return topology.foldLeft(new F2<Double, Entity, Double>() {
            public Double f(Double acc, Entity v) {
                double distance = distanceMeasure.distance(center, v.getPosition());
                return (distance > acc) ? distance : acc;
            }
        }, 0.0);
    }
}
