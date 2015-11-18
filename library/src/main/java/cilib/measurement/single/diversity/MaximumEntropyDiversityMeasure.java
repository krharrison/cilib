/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single.diversity;

import cilib.algorithm.Algorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.measurement.entropy.DimensionalEntropyMeasurement;
import cilib.measurement.entropy.EntropyMeasurement;
import cilib.type.types.Real;
import cilib.type.types.container.TypeList;

/**
 * A diversity measurement that calculates the diversity of a population to be
 * the maximum of entropy measurements over all dimensions in the search space.
 *
 */
public class MaximumEntropyDiversityMeasure extends Diversity {

    private EntropyMeasurement entropyMeasure;

    public MaximumEntropyDiversityMeasure() {
        entropyMeasure = new DimensionalEntropyMeasurement();
    }

    public MaximumEntropyDiversityMeasure(int intervals) {
        entropyMeasure = new DimensionalEntropyMeasurement(intervals);
    }

    public MaximumEntropyDiversityMeasure(MaximumEntropyDiversityMeasure copy) {
        this.entropyMeasure = copy.entropyMeasure;
    }

    @Override
    public MaximumEntropyDiversityMeasure getClone() {
        return new MaximumEntropyDiversityMeasure(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        SinglePopulationBasedAlgorithm populationBasedAlgorithm = (SinglePopulationBasedAlgorithm) algorithm;
        int dimensions = populationBasedAlgorithm.getOptimisationProblem().getDomain().getDimension();

        double diversity = 0.0;
        TypeList entropyMeasurements = entropyMeasure.getValue(algorithm);

        for (int i = 0; i < dimensions; i++) {
            double value = ((Real) entropyMeasurements.get(i)).doubleValue();
            if (diversity < value) {
                diversity = value;
            }
        }

        diversity /= this.normalisationParameter.getNormalisationParameter(populationBasedAlgorithm);

        return Real.valueOf(diversity);
    }

    /**
     * Convenience method to get the number of intervals over which
     * entropy is measured.
     * @return The number of intervals over which entropy is measured.
     */
    public int getIntervals() {
        return entropyMeasure.getIntervals();
    }

    /**
     * Convenience method to set the number of intervals over which
     * entropy is measured.
     * @param intervals the number of intervals to set.
     */
    public void setIntervals(int intervals) {
        entropyMeasure.setIntervals(intervals);
    }

    public void setEntropyMeasure(EntropyMeasurement entropyMeasure) {
        this.entropyMeasure = entropyMeasure;
    }
}
