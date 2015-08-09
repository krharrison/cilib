/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive.fixedpool;

import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.initialisation.BehaviourPoolPopulationInitializationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ProportionalControlParameter;
import net.sourceforge.cilib.entity.behaviour.Behaviour;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.selfadaptive.SelfAdaptivePSOIterationStrategy;
import net.sourceforge.cilib.pso.selfadaptive.detectionstrategies.ParameterUpdateTriggerDetectionStrategy;
import net.sourceforge.cilib.pso.selfadaptive.detectionstrategies.PeriodicDetectionStrategy;
import net.sourceforge.cilib.util.selection.recipes.Selector;
import net.sourceforge.cilib.util.selection.recipes.TournamentSelector;
/**
 * Use a behaviour change detection, borrowed from HPSO, to determine whether a particle
 * should update its parameters. If so, probabilistically select new parameters.
 */
public class FixedPoolSelectionIterationStrategy implements IterationStrategy<PSO>, SelfAdaptivePSOIterationStrategy {
	protected IterationStrategy<PSO> iterationStrategy;
	protected ParameterUpdateTriggerDetectionStrategy detectionStrategy;
	protected Selector<Behaviour> behaviourSelectionRecipe;
	protected List<Behaviour> behaviourPool;

    /**
     * Default constructor.
     */
    public FixedPoolSelectionIterationStrategy() {
        this.iterationStrategy = new SynchronousIterationStrategy();
        this.detectionStrategy = new PeriodicDetectionStrategy();
        this.behaviourSelectionRecipe = new TournamentSelector<Behaviour>();
        this.behaviourPool = new ArrayList<Behaviour>();
        ((TournamentSelector<Behaviour>) this.behaviourSelectionRecipe).setTournamentSize(new ProportionalControlParameter(0.1));
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public FixedPoolSelectionIterationStrategy(FixedPoolSelectionIterationStrategy copy) {
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.detectionStrategy = copy.detectionStrategy.getClone();
        this.behaviourSelectionRecipe = copy.behaviourSelectionRecipe;
        this.behaviourPool = new ArrayList<Behaviour>(copy.behaviourPool);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FixedPoolSelectionIterationStrategy getClone() {
        return new FixedPoolSelectionIterationStrategy(this);
    }

    /**
     * <ol>
     *   <li>For each particle:</li>
     *   <li>Check if particle must change its behavior</li>
     *   <li>If particle must change its behavior:</li>
     *   <ol>
     *     <li>Assign a new behavior to the particle from the behavior pool</li>
     *   </ol>
     *   <li>Perform normal iteration</li>
     *   <li>Update success counters</li>
     * </ol>
     */
    @Override
    public void performIteration(PSO algorithm) {
    	//gather the behaviour pool from the algorithm initialization
        if (AbstractAlgorithm.get().getIterations() == 0) {
        	initializeBehaviours(algorithm);
            checkState(behaviourPool.size() > 0, "Behaviour pool is empty.");
        }

        behaviourSelection(algorithm);

        iterationStrategy.performIteration(algorithm);
    }
    
    private void initializeBehaviours(PSO algorithm){
        //get the behaviour pool from the initialization strategy
        BehaviourPoolPopulationInitializationStrategy initialization = (BehaviourPoolPopulationInitializationStrategy) algorithm.getInitialisationStrategy();
        this.behaviourPool = initialization.getDelegate().getBehaviourPool();

    }
    
    private void behaviourSelection(PSO algorithm){
        //select a behaviour from the pool using the provided selector if necessary
        Behaviour behavior;
        for(Particle p : algorithm.getTopology()) {
            if (detectionStrategy.detect(p)) {
            	behavior = behaviourSelectionRecipe.on(behaviourPool).select();
                behavior.incrementSelectedCounter();
                p.setBehaviour(behavior);
            }
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
     * {@linkplain BehaviorChangeTriggerDetectionStrategy stagnation detection strategy}.
     *
     * @return  The current
     *          {@linkplain BehaviorChangeTriggerDetectionStrategy stagnation detection strategy}.
     */
    public ParameterUpdateTriggerDetectionStrategy getDetectionStrategy() {
        return detectionStrategy;
    }

    /**
     * Set the {@linkplain BehaviorChangeTriggerDetectionStrategy stagnation detection strategy}
     * to be used.
     *
     * @param strategy  The {@linkplain BehaviorChangeTriggerDetectionStrategy stagnation detection strategy}
     *                  to set.
     */
    public void setParameterUpdateTriggerDetectionStrategy(ParameterUpdateTriggerDetectionStrategy strategy) {
        this.detectionStrategy = strategy;
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

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return this.iterationStrategy.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        this.iterationStrategy.setBoundaryConstraint(boundaryConstraint);
    }
    
    public List<Behaviour> getBehaviourPool(){
    	return this.behaviourPool;
    }
}
