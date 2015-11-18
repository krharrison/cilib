/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.niching.merging.detection;

import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.measurement.single.diversity.Diversity;
import cilib.measurement.single.diversity.centerinitialisationstrategies.GBestCenterInitialisationStrategy;

public class DiversityBasedMergeDetection extends MergeDetection {
    private Diversity diversityMeasure;
    private ControlParameter threshold;

    /**
     * Default constructor.
     */
    public DiversityBasedMergeDetection() {
        this.threshold = ConstantControlParameter.of(0.1);
        this.diversityMeasure = new Diversity();
        this.diversityMeasure.setPopulationCenter(new GBestCenterInitialisationStrategy());
    }

    /**
     * Determines if two swarms should be merged based on whether they overlap and
     * if the diversity of the first swarm is below a threshold.
     * @param a a {@linkplain PopulationBasedAlgorithm}.
     * @param b a {@linkplain PopulationBasedAlgorithm}.
     * @return whether the two swarms should be merged.
     */
    @Override
    public Boolean f(SinglePopulationBasedAlgorithm a, SinglePopulationBasedAlgorithm b) {
        return diversityMeasure.getValue(a).doubleValue() < threshold.getParameter();
    }

    public Diversity getDiversityMeasure() {
        return diversityMeasure;
    }

    public void setDiversityMeasure(Diversity diversityMeasure) {
        this.diversityMeasure = diversityMeasure;
    }

    public ControlParameter getThreshold() {
        return threshold;
    }

    public void setThreshold(ControlParameter threshold) {
        this.threshold = threshold;
    }
}
