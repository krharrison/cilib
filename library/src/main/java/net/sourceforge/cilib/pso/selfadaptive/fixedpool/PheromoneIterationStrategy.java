/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive.fixedpool;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.initialisation.BehaviourGeneratorPopulationInitializationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.behaviour.Behaviour;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.hpso.pheromoneupdate.ConstantPheromoneUpdateStrategy;
import net.sourceforge.cilib.pso.hpso.pheromoneupdate.PheromoneUpdateStrategy;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.selfadaptive.detectionstrategies.ParameterUpdateTriggerDetectionStrategy;
import net.sourceforge.cilib.pso.selfadaptive.detectionstrategies.PeriodicDetectionStrategy;
import net.sourceforge.cilib.util.selection.recipes.RouletteWheelSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;
import net.sourceforge.cilib.util.selection.weighting.ParticleBehaviorWeighting;
import net.sourceforge.cilib.util.selection.weighting.behavior.SpecialisedRatio;

public class PheromoneIterationStrategy implements IterationStrategy<PSO> {
	
	protected List<Double> pheromoneConcentration;
	protected PheromoneUpdateStrategy pheromoneUpdateStrategy;
	protected List<Behaviour> behaviourPool;
    protected Selector<Behaviour> behaviourSelectionRecipe;
    protected IterationStrategy<PSO> iterationStrategy;
    protected ParameterUpdateTriggerDetectionStrategy detectionStrategy;
    protected ControlParameter minPheromone;
	
    /**
     * Create a new instance of {@linkplain PheromoneIterationStrategy}.
     */
    public PheromoneIterationStrategy() {
        this.minPheromone = ConstantControlParameter.of(0.01);
        this.behaviourPool = new ArrayList<Behaviour>();
        this.pheromoneConcentration = new ArrayList<Double>();
        this.pheromoneUpdateStrategy = new ConstantPheromoneUpdateStrategy();

        this.iterationStrategy = new SynchronousIterationStrategy();
        this.detectionStrategy = new PeriodicDetectionStrategy();
    }
    
    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public PheromoneIterationStrategy(PheromoneIterationStrategy copy) {
        this.minPheromone = copy.minPheromone.getClone();
        this.pheromoneConcentration = new ArrayList<Double>(copy.pheromoneConcentration);
        this.pheromoneUpdateStrategy = copy.pheromoneUpdateStrategy;

        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.detectionStrategy = copy.detectionStrategy.getClone();
        this.behaviourSelectionRecipe = copy.behaviourSelectionRecipe;
        this.behaviourPool = new ArrayList<Behaviour>(copy.behaviourPool);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PheromoneIterationStrategy getClone() {
        return new PheromoneIterationStrategy(this);
    }
	@Override
	public void performIteration(PSO algorithm) {
		
		//reset for each algorithm
        if (AbstractAlgorithm.get().getIterations() == 0) {
            initializeBehaviours(algorithm);
        }
        
        behaviourSelection(algorithm);
        
        iterationStrategy.performIteration(algorithm);
        
        updatePheromone(algorithm);
        evaporatePheromone();
	}
	
	private void initializeBehaviours(PSO algorithm){
		//get the behaviour pool from the initialization strategy
        BehaviourGeneratorPopulationInitializationStrategy initialization = (BehaviourGeneratorPopulationInitializationStrategy) algorithm.getInitialisationStrategy();
        behaviourPool = initialization.getDelegate().getBehaviourPool();
    	//pheromoneConcentration = new ArrayList<Double>(behaviourPool.size());
        //initialize all pheromones to be equal
    	for(int i = 0; i < behaviourPool.size(); i++) {
    		pheromoneConcentration.add(new Double(1.0 / behaviourPool.size()));
        }
    	

        SpecialisedRatio weighting = new SpecialisedRatio();
        weighting.setBehaviors(behaviourPool);
        weighting.setWeights(pheromoneConcentration);

        this.behaviourSelectionRecipe = new RouletteWheelSelector<Behaviour>(new ParticleBehaviorWeighting(weighting));
    	
	}
	
	private void behaviourSelection(PSO algorithm){
		Behaviour behavior;
        for(Particle p : algorithm.getTopology()) {
            if (detectionStrategy.detect(p)) {
                behavior = behaviourSelectionRecipe.on(behaviourPool).select();
                behavior.incrementSelectedCounter();
                p.setBehaviour(behavior);
            }
        }
	}
	
	private void updatePheromone(PSO algorithm){
        for(Particle p : algorithm.getTopology()){
        	double deltaP = pheromoneUpdateStrategy.updatePheromone(p);
        	int index = behaviourPool.indexOf(p.getBehaviour());
        	pheromoneConcentration.set(index, Math.max(pheromoneConcentration.get(index) + deltaP, minPheromone.getParameter()));
        }
	}
	
	private void evaporatePheromone(){
        double sumPheromone = 0;
        for(Double d : pheromoneConcentration) {
            sumPheromone += d;
        }

        for(Behaviour b : behaviourPool) {
        	int index = behaviourPool.indexOf(b);
        	double pheromone = pheromoneConcentration.get(index);
            pheromoneConcentration.set(index, Math.max((sumPheromone - pheromone) * pheromone / sumPheromone, minPheromone.getParameter()));
        }
	}
	
	 /**
     * Get the current {@linkplain IterationStrategy}.
     * @return The current {@linkplain IterationStrategy}.
     */
    public IterationStrategy<PSO> getIterationStrategy() {
        return iterationStrategy;
    }

    /**
     * Set the {@linkplain IterationStrategy} to be used.
     * @param iterationStrategy The value to set.
     */
    public void setIterationStrategy(IterationStrategy<PSO> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    /**
     * Get the currently defined
     * {@link ParameterUpdateTriggerDetectionStrategy}.
     *
     * @return  The current
     *          {@link ParameterUpdateTriggerDetectionStrategy}.
     */
    public ParameterUpdateTriggerDetectionStrategy getDetectionStrategy() {
        return detectionStrategy;
    }

    /**
     * Set the {@link ParameterUpdateTriggerDetectionStrategy}
     * to be used.
     *
     * @param strategy  The {@link ParameterUpdateTriggerDetectionStrategy}
     *                  to set.
     */
    public void setParameterUpdateTriggerDetectionStrategy(ParameterUpdateTriggerDetectionStrategy strategy) {
        this.detectionStrategy = strategy;
    }

    /**
     * Get the currently defined {@linkplain Selector},
     * @return The current {@linkplain Selector}.
     */
   // public Selector<Behaviour> getSelectionRecipe() {
    //    return behaviourSelectionRecipe;
    //}

    /**
     * Set the current {@linkplain Selector} to use.
     * @param recipe The {@linkplain Selector} to set.
     */
    //public void setSelectionRecipe(Selector<Behaviour> recipe) {
    //    this.behaviourSelectionRecipe = recipe;
    //}
    
    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return this.iterationStrategy.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        this.iterationStrategy.setBoundaryConstraint(boundaryConstraint);
    }
    
    public void setMinPheromone(ControlParameter minPeromone) {
        this.minPheromone = minPeromone;
    }

    public ControlParameter getMinPheromone() {
        return minPheromone;
    }
    
    public PheromoneUpdateStrategy getPheromoneUpdateStrategy() {
        return pheromoneUpdateStrategy;
    }
    
    public void setPheromoneUpdateStrategy(PheromoneUpdateStrategy strategy){
    	this.pheromoneUpdateStrategy = strategy;
    }
}
