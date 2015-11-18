/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.niching.creation;

import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Entity;

public class AlwaysTrueNicheDetection extends NicheDetection {
    @Override
    public Boolean f(SinglePopulationBasedAlgorithm a, Entity b) {
	if (b != null && a.getTopology().isNotEmpty())
               return true;
       return false;
    }
}
