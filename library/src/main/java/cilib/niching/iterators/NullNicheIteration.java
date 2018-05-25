/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.niching.iterators;

import cilib.algorithm.population.SinglePopulationBasedAlgorithm;

public class NullNicheIteration extends NicheIteration {
    @Override
    public SinglePopulationBasedAlgorithm f(SinglePopulationBasedAlgorithm a) {
        return a;
    }
}