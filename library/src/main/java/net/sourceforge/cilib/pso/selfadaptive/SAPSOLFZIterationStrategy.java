/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.measurement.single.IterationBestFitness;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.behaviour.StandardParticleBehaviour;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.velocityprovider.ClampingVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.SAPSOLFZVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;

public class SAPSOLFZIterationStrategy extends AbstractIterationStrategy<PSO> {

	protected IterationStrategy<PSO> delegate;
	protected ClampingVelocityProvider velocityProvider;
	protected double alpha;
	protected IterationBestFitness iterBestFitness;
	
	public SAPSOLFZIterationStrategy() {
		super();
		delegate = new SynchronousIterationStrategy();
		velocityProvider = new ClampingVelocityProvider();
		velocityProvider.setDelegate(new SAPSOLFZVelocityProvider());
		alpha = 0.15;
		iterBestFitness = new IterationBestFitness();
	}
	
	public SAPSOLFZIterationStrategy(SAPSOLFZIterationStrategy copy){
		super(copy);
		this.delegate = copy.delegate.getClone();
		this.velocityProvider = copy.velocityProvider.getClone();
		this.alpha = copy.alpha;
		this.iterBestFitness = copy.iterBestFitness.getClone();
	}
	
	@Override
	public SAPSOLFZIterationStrategy getClone() {
		return new SAPSOLFZIterationStrategy(this);
	}

	@Override
	public void performIteration(PSO algorithm) {
		//ensure each entity has their own behaviour/velocity provider
		if(algorithm.getIterations() == 0){
			for(Particle p : algorithm.getTopology()){
				StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour().getClone();
				behaviour.setVelocityProvider(velocityProvider.getClone());
				p.setBehaviour(behaviour);
			}
		}
		
		double avgFitness = calculateAverageBestFitness(algorithm);
		
		for(Particle p : algorithm.getTopology()){
			double inertia = alpha + 1 / (1 + Math.exp(avgFitness - p.getBestFitness().getValue()));
			
			StandardParticleBehaviour behaviour = (StandardParticleBehaviour) p.getBehaviour();
			SAPSOLFZVelocityProvider provider = (SAPSOLFZVelocityProvider)((ClampingVelocityProvider) behaviour.getVelocityProvider()).getDelegate();
			
			provider.setInertiaWeight(ConstantControlParameter.of(inertia));
		}
		
		delegate.performIteration(algorithm);
		
		//update pWorst if need be
		for(Particle p : algorithm.getTopology()){
			Fitness pWorst = p.get(Property.WORST_FITNESS);
			if(p.getFitness().compareTo(pWorst) < 0){
				p.put(Property.WORST_FITNESS, p.getFitness().getClone());
				p.put(Property.WORST_POSITION, p.getPosition().getClone());
			}
		}
	}
	
	private double calculateAverageBestFitness(PSO algorithm){
		int count = 0;
		double sum = 0;
		
		for(Particle p : algorithm.getTopology()){
			double fitness = p.getBestFitness().getValue();
			if(Double.isFinite(fitness)){
				sum += Math.abs(fitness);
				count++;
			}
		}
		
		return count == 0 ? Double.MAX_VALUE : sum / count;
	}
	
	public void setVelocityProvider(ClampingVelocityProvider velocityProvider){
		this.velocityProvider = velocityProvider;
	}
	
	public void setDelegate(IterationStrategy<PSO> delegate){
		this.delegate = delegate;
	}
	
	public void setAlpha(double alpha){
		this.alpha = alpha;
	}
}
