/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.Real;


/**
 * Return the fitness of the best {@link Entity} in the current iteration.
 */
public class IterationBestFitness implements Measurement<Real>{

	@Override
	public IterationBestFitness getClone() {
		return this;
	}

	@Override
	public Real getValue(Algorithm algorithm) {
		SinglePopulationBasedAlgorithm populationBasedAlgorithm = (SinglePopulationBasedAlgorithm) algorithm;
		
		Iterator<Entity> populationIterator = populationBasedAlgorithm.getTopology().iterator();
		
		net.sourceforge.cilib.problem.solution.Fitness bestFitness = InferiorFitness.instance();
		
		while (populationIterator.hasNext()) {
            Entity entity = populationIterator.next();
            net.sourceforge.cilib.problem.solution.Fitness entityFitness = entity.getFitness();
            
            if(entityFitness.compareTo(bestFitness) > 0) {
            	bestFitness = entityFitness;
            }
		}       
	     
		return Real.valueOf(bestFitness.getValue());
	}

}
