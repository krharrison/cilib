/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive.adaptationstrategies;

import java.util.List;

import net.sourceforge.cilib.algorithm.initialisation.BehaviourPoolPopulationInitializationStrategy;
import net.sourceforge.cilib.entity.behaviour.Behaviour;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

public class SelectFromPool implements AdaptationStrategy {

	private Selector<Behaviour> behaviourSelectionRecipe;

	//TODO: have the selector contain a pool of behaviours?
	
	public SelectFromPool(){
		this.behaviourSelectionRecipe = new RandomSelector<Behaviour>();
	}
	
	public SelectFromPool(SelectFromPool clone){
		this.behaviourSelectionRecipe = clone.behaviourSelectionRecipe;
	}
	
	/**
	 * Get the pool of behaviours generated on initialization and use
	 * the specified selector to select from this pool
	 */
	@Override
	public Behaviour adapt(Particle particle, PSO algorithm) {
		
		BehaviourPoolPopulationInitializationStrategy initialization = (BehaviourPoolPopulationInitializationStrategy) algorithm.getInitialisationStrategy();
		List<Behaviour> behaviourPool = initialization.getDelegate().getBehaviourPool();
		
		return behaviourSelectionRecipe.on(behaviourPool).select();
	}

	@Override
	public SelectFromPool getClone() {
		return new SelectFromPool(this);
	}
	
    /**
     * Get the currently defined {@linkplain Selector},
     * @return The current {@linkplain Selector}.
     */
    public Selector<Behaviour> getSelectionRecipe() {
        return behaviourSelectionRecipe;
    }

    /**
     * Set the current {@linkplain Selector} to use.
     * @param recipe The {@linkplain Selector} to set.
     */
    public void setSelectionRecipe(Selector<Behaviour> recipe) {
        this.behaviourSelectionRecipe = recipe;
    }

}
