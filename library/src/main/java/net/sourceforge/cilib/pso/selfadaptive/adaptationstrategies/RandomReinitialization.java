/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive.adaptationstrategies;

import net.sourceforge.cilib.algorithm.initialisation.BehaviourGeneratorPopulationInitializationStrategy;
import net.sourceforge.cilib.entity.behaviour.Behaviour;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * Randomly reinitialize the control parameters (i.e., the behaviour) using the
 * behaviour generator used on initialization.
 */
public class RandomReinitialization implements AdaptationStrategy {

	/**
	 * Get the behaviour generator used on algorithm initialization and use 
	 * it to randomly generate a new behaviour.
	 */
	@Override
	public Behaviour adapt(Particle particle, PSO algorithm) {

		BehaviourGeneratorPopulationInitializationStrategy initialization = (BehaviourGeneratorPopulationInitializationStrategy) algorithm.getInitialisationStrategy();
        
		//remove this behaviour from the pool and regenerate a new one, adding it to the pool
		//what if other particles are also using this behaviour?
		//initialization.getDelegate().getBehaviourPool().remove(particle.getBehaviour());
		return initialization.getBehaviourGenerator().generate();
		//initialization.getDelegate().getBehaviourPool().add(newBehaviour);
		
		//particle.setBehaviour(newBehaviour);
	}

	@Override
	public RandomReinitialization getClone() {
		return new RandomReinitialization();
	}

}
