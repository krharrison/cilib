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
public class IPSOCLLIterationStrategy extends AbstractIterationStrategy<PSO> {

	protected IterationStrategy<PSO> delegate;
	protected ClampingVelocityProvider velocityProvider;
	protected double previousAlpha;
	
	public IPSOCLLIterationStrategy(){
		delegate = new SynchronousIterationStrategy();
		velocityProvider = new ClampingVelocityProvider();
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
			
			previousAlpha = calculateAlpha(algorithm);
		}

		delegate.performIteration(algorithm);
		
		double alpha = calculateAlpha(algorithm);
		double lambda = previousAlpha == 0 ? 1 : alpha / previousAlpha;
		double inertia = Math.exp(-lambda);
		
		for(Particle p : algorithm.getTopology()){
			StandardParticleBehaviour behaviour = (StandardParticleBehaviour) p.getBehaviour();
			StandardVelocityProvider provider = (StandardVelocityProvider)((ClampingVelocityProvider) behaviour.getVelocityProvider()).getDelegate();
			
			provider.setInertiaWeight(ConstantControlParameter.of(inertia));
		}

		previousAlpha = alpha;
		
	}
	
	private double calculateAlpha(PSO algorithm){
		double sum = 0;
		int count = 0;
		double globalBest = algorithm.getBestSolution().getFitness().getValue();
		for(Particle p : algorithm.getTopology()){
			//TODO: this might not actually be global best, but rather best at this iteration
			double fitness = p.getFitness().getValue();
			if(Double.isFinite(fitness)){
				sum += Math.abs(p.getFitness().getValue() - globalBest);
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
}
