/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive;

import com.google.common.base.Predicate;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.behaviour.StandardParticleBehaviour;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.velocityprovider.ClampingVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.IPSOLTVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.util.selection.PartialSelection;
import net.sourceforge.cilib.util.selection.recipes.ElitistSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 * Improved Particle Swarm Optimization by Li and Tan
 */
public class IPSOLTIterationStrategy extends AbstractIterationStrategy<PSO> {

	protected Selector<Particle> elitistSelector;
	protected double prevIterationBestFitness;
	protected IterationStrategy<PSO> delegate;
	protected double alpha;
	protected double beta;
	protected ClampingVelocityProvider velocityProvider;
	
	public IPSOLTIterationStrategy(){
		elitistSelector = new ElitistSelector<Particle>();
		delegate = new SynchronousIterationStrategy();
		alpha = 0.5;
		beta = 0.5;
		velocityProvider = new ClampingVelocityProvider();
		velocityProvider.setDelegate(new IPSOLTVelocityProvider());
	}
	
	public IPSOLTIterationStrategy(IPSOLTIterationStrategy copy){
		this.elitistSelector = copy.elitistSelector;
		this.delegate = copy.delegate.getClone();
		this.alpha = copy.alpha;
		this.beta = copy.beta;
	}
	
	@Override
	public IPSOLTIterationStrategy getClone() {
		return new IPSOLTIterationStrategy(this);
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
			
			prevIterationBestFitness = getIterationBestFitness(algorithm);
		}
		
		delegate.performIteration(algorithm);
		
		double bestIterationFitness = getIterationBestFitness(algorithm);
		double convergenceFactor = calculateConvergenceFactor(algorithm, bestIterationFitness);
		double diffusionFactor = calculateDiffusionFactor(algorithm, bestIterationFitness);
		double newInertia = 0;
		
		//if the convergence and diffusion are both finite, update inertia
		//otherwise, leave it as 0 to allow particles to move back towards good locations
		if(Double.isFinite(convergenceFactor) && Double.isFinite(diffusionFactor)){
			newInertia = 1 - (alpha * (1 - convergenceFactor)) / ((1 + diffusionFactor) * (1 + beta));
		}
		
		for(Particle p : algorithm.getTopology()){
			StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
			IPSOLTVelocityProvider provider = (IPSOLTVelocityProvider)((ClampingVelocityProvider) behaviour.getVelocityProvider()).getDelegate();

			provider.setInertiaWeight(ConstantControlParameter.of(newInertia));
		}
		
		prevIterationBestFitness = bestIterationFitness;
	}
	
	/**
	 * Return the best fitness of this iteration
	 * @param algorithm
	 * @return
	 */
	public double getIterationBestFitness(PSO algorithm){
		return elitistSelector.on(algorithm.getTopology()).select().getFitness().getValue();
	}
	
	
	public double calculateConvergenceFactor(PSO algorithm, double bestIterationFitness){
		return Math.abs(prevIterationBestFitness - bestIterationFitness) / (prevIterationBestFitness + bestIterationFitness);
	}
	
	public double calculateDiffusionFactor(PSO algorithm, double bestIterationFitness){
		double globalBestFitness = algorithm.getBestSolution().getFitness().getValue();
		return Math.abs(bestIterationFitness - globalBestFitness) / (bestIterationFitness + globalBestFitness);
	}

	public void setVelocityProvider(ClampingVelocityProvider velocityProvider){
		this.velocityProvider = velocityProvider;
	}
	
	public void setDelegate(IterationStrategy<PSO> delegate){
		this.delegate = delegate;
	}
	
}
