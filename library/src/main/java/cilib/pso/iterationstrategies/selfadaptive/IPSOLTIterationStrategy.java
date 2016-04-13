/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.pso.PSO;

import java.util.HashMap;
import java.util.Map;

public class IPSOLTIterationStrategy  extends AbstractIterationStrategy<PSO> {

    double[] previousPBest;

    @Override
    public AbstractIterationStrategy<PSO> getClone() {
        return null;
    }

    @Override
    public void performIteration(PSO algorithm) {

        if(algorithm.getIterations() == 0){
            previousPBest = new double[algorithm.getTopology().length()];
        }

        //get the personal best of each particle
        for(int i = 0; i < algorithm.getTopology().length(); i++){
            previousPBest[i] = algorithm.getTopology().index(i).getBestFitness().getValue();
        }


    }


   // private double
}
