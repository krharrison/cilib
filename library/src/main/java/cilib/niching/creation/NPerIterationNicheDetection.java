/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.niching.creation;

import cilib.algorithm.AbstractAlgorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Entity;

public class NPerIterationNicheDetection extends NicheDetection {

    private int n;
    private int current;
    private int iteration;

    public NPerIterationNicheDetection() {
        this.n = 1;
        this.current = 0;
        this.iteration = 0;
    }

    @Override
    public Boolean f(SinglePopulationBasedAlgorithm a, Entity b) {
        if (AbstractAlgorithm.get().getIterations() != iteration) {
            iteration = AbstractAlgorithm.get().getIterations();
            current = 0;
            return true;
        } else {
            if (++current < n) {
                return true;
            }
        }

        return false;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }
}
