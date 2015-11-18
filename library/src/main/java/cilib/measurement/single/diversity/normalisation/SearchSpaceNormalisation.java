/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package cilib.measurement.single.diversity.normalisation;

import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.type.types.container.Vector;

/**
 *
 * @author filipe
 */
public class SearchSpaceNormalisation implements DiversityNormalisation {

    @Override
    public double getNormalisationParameter(SinglePopulationBasedAlgorithm algorithm) {
        return ((Vector)algorithm.getOptimisationProblem().getDomain().getBuiltRepresentation()).length();
    }
    
}
