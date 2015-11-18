/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single.diversity;

import java.util.Iterator;
import cilib.algorithm.Algorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Entity;
import cilib.measurement.Measurement;
import cilib.measurement.single.diversity.centerinitialisationstrategies.CenterInitialisationStrategy;
import cilib.measurement.single.diversity.centerinitialisationstrategies.SpatialCenterInitialisationStrategy;
import cilib.measurement.single.diversity.normalisation.DiversityNormalisation;
import cilib.measurement.single.diversity.normalisation.NormalisationParameter;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;
import cilib.util.distancemeasure.DistanceMeasure;
import cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * TODO: Add JavaDoc.
 *
 */
public class Diversity implements Measurement<Real> {

    private static final long serialVersionUID = 7417526206433000209L;
    protected DistanceMeasure distanceMeasure;
    protected CenterInitialisationStrategy populationCenter;
    protected DiversityNormalisation normalisationParameter;

    public Diversity() {
        distanceMeasure = new EuclideanDistanceMeasure();
        populationCenter = new SpatialCenterInitialisationStrategy();
        normalisationParameter = new NormalisationParameter();
    }

    public Diversity(Diversity other) {
        this.distanceMeasure = other.distanceMeasure;
        this.populationCenter = other.populationCenter;
        this.normalisationParameter = other.normalisationParameter;
    }

    @Override
    public Diversity getClone() {
        return new Diversity(this);
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        SinglePopulationBasedAlgorithm populationBasedAlgorithm = (SinglePopulationBasedAlgorithm) algorithm;
        int numberOfEntities = populationBasedAlgorithm.getTopology().length();

        Vector center = populationCenter.getCenter(populationBasedAlgorithm.getTopology());
        Iterator<? extends Entity> populationIterator = populationBasedAlgorithm.getTopology().iterator();

        double distanceSum = 0.0;

        while (populationIterator.hasNext()) {
            Vector currentEntityPosition = (Vector) (((Entity) populationIterator.next()).getPosition());
            distanceSum += distanceMeasure.distance(center, currentEntityPosition);
        }

        distanceSum /= numberOfEntities;
        distanceSum /= normalisationParameter.getNormalisationParameter(populationBasedAlgorithm);

        return Real.valueOf(distanceSum);
    }

    /**
     * @return the distanceMeasure
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    /**
     * @param distanceMeasure the distanceMeasure to set
     */
    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    /**
     * @return the normalisationParameter
     */
    public DiversityNormalisation getNormalisationParameter() {
        return normalisationParameter;
    }

    /**
     * @param normalisationParameter the normalisationParameter to set
     */
    public void setNormalisationParameter(DiversityNormalisation normalisationParameter) {
        this.normalisationParameter = normalisationParameter;
    }

    /**
     * @return the populationCenter
     */
    public CenterInitialisationStrategy getPopulationCenter() {
        return populationCenter;
    }

    /**
     * @param populationCenter the populationCenter to set
     */
    public void setPopulationCenter(CenterInitialisationStrategy populationCenter) {
        this.populationCenter = populationCenter;
    }
}
