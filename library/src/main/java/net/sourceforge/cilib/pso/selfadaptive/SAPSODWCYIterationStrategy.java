/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive;

import java.util.Comparator;
import java.util.Iterator;

import com.google.common.collect.Ordering;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.behaviour.StandardParticleBehaviour;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.velocityprovider.ClampingVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.util.selection.arrangement.Arrangement;
import net.sourceforge.cilib.util.selection.arrangement.SortedArrangement;

public class SAPSODWCYIterationStrategy extends AbstractIterationStrategy<PSO>{

	protected ClampingVelocityProvider velocityProvider;
	protected IterationStrategy<PSO> delegate;
	protected Arrangement<Particle> arrangement;
	
	protected double alpha;
	protected double beta;
	protected double gamma;
	
	public SAPSODWCYIterationStrategy(){
		super();
		arrangement = new SortedArrangement<Particle>();
		delegate = new SynchronousIterationStrategy();
		velocityProvider = new ClampingVelocityProvider();
		alpha = 3;
		beta = 200;
		gamma = 8;
	}
	
	public SAPSODWCYIterationStrategy(SAPSODWCYIterationStrategy copy){
		super(copy);
		this.arrangement = copy.arrangement;
		this.delegate = copy.delegate.getClone();
		this.velocityProvider = copy.velocityProvider.getClone();
		this.alpha = copy.alpha;
		this.beta = copy.beta;
		this.gamma = copy.gamma;
	}
	
	@Override
	public SAPSODWCYIterationStrategy getClone() {
		return new SAPSODWCYIterationStrategy(this);
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
		
		Iterable<Particle> ordering = orderParticles(algorithm);
		Iterator<Particle> iterator = ordering.iterator();
		int rank = 1;
		int particles = algorithm.getTopology().length();
		int dimensions = algorithm.getOptimisationProblem().getDomain().getDimension();
		
		Particle p;
		while(iterator.hasNext()){
			p = iterator.next();
			
			double expTerm = Math.exp(-particles / beta);
			double rankTerm = (dimensions * rank) / gamma;
			double inertia = 1 / (alpha - expTerm + (rankTerm * rankTerm));
			
			StandardParticleBehaviour behaviour = (StandardParticleBehaviour) p.getBehaviour();
			StandardVelocityProvider provider = (StandardVelocityProvider)((ClampingVelocityProvider) behaviour.getVelocityProvider()).getDelegate();
			provider.setInertiaWeight(ConstantControlParameter.of(inertia));
			
			rank++; //decrement rank for next particle
		}
		
		delegate.performIteration(algorithm);
	}
	
	private Iterable<Particle> orderParticles(PSO algorithm){
		return arrangement.arrange(algorithm.getTopology());
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
	
	public void setBeta(double beta){
		this.beta = beta;
	}
	
	public void setGamma(double gamma){
		this.gamma = gamma;
	}
}
