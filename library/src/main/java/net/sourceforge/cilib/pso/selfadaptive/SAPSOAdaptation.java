/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.behaviour.StandardParticleBehaviour;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.selfadaptive.adaptationstrategies.AlgorithmAdaptationStrategy;
import net.sourceforge.cilib.pso.selfadaptive.adaptationstrategies.PSOAdaptationStrategy;
import net.sourceforge.cilib.pso.velocityprovider.SelfAdaptiveVelocityProvider;

public class SAPSOAdaptation implements IterationStrategy<PSO>{
	
	protected IterationStrategy<PSO> iterationStrategy;
	protected AlgorithmAdaptationStrategy adaptationStrategy;
	//protected ParameterUpdateTriggerDetectionStrategy detectionStrategy;
	protected int period;
	
	public SAPSOAdaptation(){
		iterationStrategy = new SynchronousIterationStrategy();
		adaptationStrategy = new PSOAdaptationStrategy();
		period = 30;
	}
	
	@Override
	public IterationStrategy<PSO> getClone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void performIteration(PSO algorithm) {
		 int iters = AbstractAlgorithm.get().getIterations();
		 
		 //adapt and reset fitness if time to do so
		 if(iters > 0 && iters % period == 0){
			 adaptationStrategy.adapt(algorithm);
			 for(Particle p : algorithm.getTopology()){
				 StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
				 SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider)behaviour.getVelocityProvider();
				 provider.getParameterSet().resetFitness();
			 }
		 }
		
		 iterationStrategy.performIteration(algorithm);
		 
		 for(Particle p : algorithm.getTopology()){
			 //if the particle improved in fitness, increment the fitness of the parameters by 1
			 if(p.getFitness().compareTo((Fitness)p.get(Property.PREVIOUS_FITNESS)) > 0){
				 StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
				 SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider)behaviour.getVelocityProvider();
				 provider.getParameterSet().incrementFitness(1);
			 }
		 }
	}

	public void setAdaptationStrategy(AlgorithmAdaptationStrategy strategy){
		this.adaptationStrategy = strategy;
	}
	
	@Override
	public BoundaryConstraint getBoundaryConstraint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
		// TODO Auto-generated method stub
		
	}
	
	public void setPeriod(int period){
		this.period = period;
	}

}
