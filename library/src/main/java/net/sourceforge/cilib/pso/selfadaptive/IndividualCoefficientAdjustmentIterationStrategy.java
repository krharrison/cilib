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
 * PSO-SAIC
 *
 */
public class IndividualCoefficientAdjustmentIterationStrategy extends AbstractIterationStrategy<PSO> {

	protected double inertiaA;
	protected double inertiaB;
	protected double socialA;
	protected double socialB;
	protected IterationStrategy<PSO> delegate;
	protected ClampingVelocityProvider velocityProvider;
	protected double offset; //offset added to fitness, as this strategy requires non-negative fitnesses
	
	public IndividualCoefficientAdjustmentIterationStrategy(){
		super();
		inertiaA = 0.9;
		inertiaB = 0.45;
		socialA = 0.5;
		socialB = 2.5;
		delegate = new SynchronousIterationStrategy();
		velocityProvider = new ClampingVelocityProvider();
		offset = 0;
	}
	
	public IndividualCoefficientAdjustmentIterationStrategy(IndividualCoefficientAdjustmentIterationStrategy copy){
		super(copy);
		this.inertiaA = copy.inertiaA;
		this.inertiaB = copy.inertiaB;
		this.socialA = copy.socialA;
		this.socialB = copy.socialB;
		this.delegate = copy.delegate.getClone();
		this.velocityProvider = copy.velocityProvider.getClone();
		this.offset = copy.offset;
	}
	
	@Override
	public IndividualCoefficientAdjustmentIterationStrategy getClone() {
		return new IndividualCoefficientAdjustmentIterationStrategy(this);
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
			double fitness = p.getFitness().getValue() + offset;
			
			double adaptiveCoefficient;
			
			if(!Double.isFinite(fitness)){ 	//set to 1 if invalid fitness 
				adaptiveCoefficient = 1;
			}
			else if(fitness == 0.0){		//set to 0 if fitness is 0 to prevent divide by 0
				adaptiveCoefficient = 0;
			}
			else {
				adaptiveCoefficient = (fitness - globalBest) / fitness;
			}
			
			
			double f = calculateF(adaptiveCoefficient);
			double g = calculateG(adaptiveCoefficient);
			
			double inertia = inertiaA * f + inertiaB;
			double social = socialA * g + socialB;
			
			StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
			StandardVelocityProvider provider = (StandardVelocityProvider)((ClampingVelocityProvider) behaviour.getVelocityProvider()).getDelegate();

			provider.setInertiaWeight(ConstantControlParameter.of(inertia));
			provider.setSocialAcceleration(ConstantControlParameter.of(social));
		}
		
		delegate.performIteration(algorithm);
		
	}
	
	private double calculateF(double adaptiveCoefficient){
		return 2 * (1 - Math.cos((Math.PI * adaptiveCoefficient) / 2));
	}

	private double calculateG(double adaptiveCoefficient){
		return 2.5 * (1 - Math.cos((Math.PI * adaptiveCoefficient) / 2));
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
