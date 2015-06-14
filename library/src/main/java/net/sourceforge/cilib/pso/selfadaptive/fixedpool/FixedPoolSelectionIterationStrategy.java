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
import net.sourceforge.cilib.algorithm.initialisation.BehaviourGeneratorPopulationInitializationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.behaviour.Behaviour;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.selfadaptive.detectionstrategies.ParameterUpdateTriggerDetectionStrategy;
import net.sourceforge.cilib.pso.selfadaptive.detectionstrategies.PeriodicDetectionStrategy;
import net.sourceforge.cilib.util.selection.recipes.Selector;
import net.sourceforge.cilib.util.selection.recipes.TournamentSelector;
/**
 * Use a behaviour change detection, borrowed from HPSO, to determine whether a particle
 * should update its parameters. If so, probabilistically select new parameters.
 */
public class FixedPoolSelectionIterationStrategy implements IterationStrategy<PSO> {
	 private IterationStrategy<PSO> iterationStrategy;
	    private ParameterUpdateTriggerDetectionStrategy detectionStrategy;
	    private Selector<Behaviour> behaviourSelectionRecipe;
	    private List<Behaviour> behaviourPool;
	    private Map<Behaviour, List<Integer>> successCounters;
	    private ControlParameter windowSize;

	    /**
	     * Default constructor.
	     */
	    public FixedPoolSelectionIterationStrategy() {
	        this.iterationStrategy = new SynchronousIterationStrategy();
	        this.detectionStrategy = new PeriodicDetectionStrategy();
	        this.behaviourSelectionRecipe = new TournamentSelector<Behaviour>();
	        this.behaviourPool = new ArrayList<Behaviour>();
	        this.windowSize = ConstantControlParameter.of(10);
	        this.successCounters = new HashMap<Behaviour, List<Integer>>();
	        ((TournamentSelector<Behaviour>) this.behaviourSelectionRecipe).setTournamentSize(ConstantControlParameter.of(0.2));
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
	        this.successCounters = new HashMap<Behaviour, List<Integer>>(copy.successCounters);
	        this.windowSize = copy.windowSize;
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
	        //checkState(behaviorPool.size() > 0, "You must add particle behaviors to the behavior pool first.");
	        checkState((int) windowSize.getParameter() > 0, "N must be bigger than 0.");

	    	//reset for algorithm
	        if (AbstractAlgorithm.get().getIterations() == 0) {
	        	initializeBehaviours(algorithm);
	        }
	        checkState(behaviourPool.size() > 0, "Behaviour pool is empty.");
	        behaviourSelection(algorithm);

	        iterationStrategy.performIteration(algorithm);

	        //update success counters
	        for(Behaviour pb : behaviourPool) {
	            successCounters.get(pb).set(AbstractAlgorithm.get().getIterations()%(int)windowSize.getParameter(), pb.getSuccessCounter());
	        }
	    }
	    
	    private void initializeBehaviours(PSO algorithm){

	            //get the behaviour pool from the initialization strategy
	            BehaviourGeneratorPopulationInitializationStrategy initialization = (BehaviourGeneratorPopulationInitializationStrategy) algorithm.getInitialisationStrategy();
	            this.behaviourPool = initialization.getDelegate().getBehaviourPool();
	        	
	        	for(Behaviour pb : behaviourPool) {
	                addToSuccessCounters(pb);
	            }
	        
	    }
	    
	    private void behaviourSelection(PSO algorithm){
	        
	        for(Behaviour b : behaviourPool) {
	            int sum = 0;
	            for(int i = 0; i < (int) windowSize.getParameter(); i++) {
	                sum += successCounters.get(b).get(i);
	            }

	            b.setSuccessCounter(sum);
	        }

	        //select a behaviour from the pool using the provided selector if necessary
	        Behaviour behavior;
	        for(Particle p : algorithm.getTopology()) {
	            if (detectionStrategy.detect(p)) {
	            	behavior = behaviourSelectionRecipe.on(behaviourPool).select();
	                behavior.incrementSelectedCounter();
	                p.setBehaviour(behavior);
	            }
	        }

	        for(Behaviour pb : behaviourPool) {
	            pb.resetSuccessCounter();
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

	    private void addToSuccessCounters(Behaviour behavior) {
	        ArrayList<Integer> zeroList = new ArrayList<Integer>((int)windowSize.getParameter());
	        for(int i = 0; i < (int) windowSize.getParameter(); i++) {
	            zeroList.add(0);
	        }

	        successCounters.put(behavior, zeroList);
	    }
	    
	    /**
	     * Sets the number of iterations for which to keep success counters.
	     * @param n The number of iterations
	     */
	    public void setWindowSize(ControlParameter n) {
	        this.windowSize = n;
	    }

	    @Override
	    public BoundaryConstraint getBoundaryConstraint() {
	        return this.iterationStrategy.getBoundaryConstraint();
	    }

	    @Override
	    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
	        this.iterationStrategy.setBoundaryConstraint(boundaryConstraint);
	    }
}
