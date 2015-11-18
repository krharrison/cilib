/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single.diversity.normalisation;

import cilib.algorithm.population.SinglePopulationBasedAlgorithm;

/**
 * A strategy to normalise a diversity measurement.
 * 
 */
public interface DiversityNormalisation {
    double getNormalisationParameter(SinglePopulationBasedAlgorithm algorithm);
}
