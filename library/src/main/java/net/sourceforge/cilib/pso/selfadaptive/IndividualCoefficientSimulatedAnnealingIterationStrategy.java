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
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.behaviour.StandardParticleBehaviour;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.velocityprovider.ClampingVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;

/**
 * PSO-ICSA
 *
 */
public class IndividualCoefficientSimulatedAnnealingIterationStrategy extends AbstractIterationStrategy<PSO> {

	protected double inertiaA;
	protected double inertiaB;
	protected double socialA;
	protected double socialB;
	protected IterationStrategy<PSO> delegate;
	protected ClampingVelocityProvider velocityProvider;
	protected double offset;
	
	public IndividualCoefficientSimulatedAnnealingIterationStrategy(){
		inertiaA = 0.9;
		inertiaB = 0.45;
		socialA = 0.5;
		socialB = 2.5;
		delegate = new SynchronousIterationStrategy();
		velocityProvider = new ClampingVelocityProvider();
		offset = 0;
	}
	
	@Override
	public AbstractIterationStrategy<PSO> getClone() {
		// TODO Auto-generated method stub
		return null;
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
		
		double globalBest = algorithm.getBestSolution().getFitness().getValue() + offset;
		for(Particle p : algorithm.getTopology()){
			double adaptiveCoefficient = globalBest / (p.getFitness().getValue() + offset);
			
			double f = calculateF(adaptiveCoefficient);
			double g = calculateG(adaptiveCoefficient);
			
			double inertia = inertiaA * f + inertiaB;
			double social = socialA * g + socialB;
			
			//clone the behaviour to address shared objects
			StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
			//TODO: make this a bit less hacky
			StandardVelocityProvider provider = (StandardVelocityProvider)((ClampingVelocityProvider) behaviour.getVelocityProvider()).getDelegate();

			provider.setInertiaWeight(ConstantControlParameter.of(inertia));
			provider.setSocialAcceleration(ConstantControlParameter.of(social));
		}
		
		delegate.performIteration(algorithm);
	}
	
	private double calculateF(double adaptiveCoefficient){
		if(adaptiveCoefficient < 0.0001) return 2;
		else if(adaptiveCoefficient < 0.01) return 1;
		else if(adaptiveCoefficient < 0.1) return 0.3;
		else if(adaptiveCoefficient < 0.9) return -0.8;
		else return -5.5;
	}

	private double calculateG(double adaptiveCoefficient){
		if(adaptiveCoefficient < 0.0001) return 2.5;
		else if(adaptiveCoefficient < 0.01) return 1.2;
		else if(adaptiveCoefficient < 0.1) return 0.5;
		else if(adaptiveCoefficient < 0.9) return 0.2;
		else return 0.1;
	}
	
	public void setVelocityProvider(ClampingVelocityProvider velocityProvider){
		this.velocityProvider = velocityProvider;
	}
	
	public void setDelegate(IterationStrategy<PSO> delegate){
		this.delegate = delegate;
	}
	
	public void setOffset(double offset){
		this.offset = offset;
	}
	
	public void setInertiaA(double inertiaA){
		this.inertiaA = inertiaA;
	}
	
	public void setInertiaB(double inertiaB){
		this.inertiaB = inertiaB;
	}
	
	public void setSocialA(double socialA){
		this.socialA = socialA;
	}
	
	public void setSocialB(double socialB){
		this.socialB = socialB;
	}
}
