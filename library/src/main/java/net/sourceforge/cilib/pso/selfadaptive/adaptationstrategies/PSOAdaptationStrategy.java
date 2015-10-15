/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive.adaptationstrategies;

import fj.F;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.behaviour.StandardParticleBehaviour;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.velocityprovider.SelfAdaptiveVelocityProvider;
import net.sourceforge.cilib.type.types.container.Vector;

public class PSOAdaptationStrategy implements AlgorithmAdaptationStrategy {

	protected PSO adaptorPSO;
	
	public PSOAdaptationStrategy(){
		adaptorPSO = new PSO(); //TODO clamping boundary constraint?
	}

	
	@Override
	public void adapt(PSO algorithm) {
		final F<Particle, Particle> create = new F<Particle, Particle>() {
			@Override
			public Particle f(Particle p) {
				StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
				SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider)behaviour.getVelocityProvider();
				
				//create a new vector as [w, c1, c2] from the current particles parameter set
				Vector.Builder builder = Vector.newBuilder();
				builder.add(provider.getInertiaWeight().getParameter());
				builder.add(provider.getCognitiveAcceleration().getParameter());
				builder.add(provider.getSocialAcceleration().getParameter());
				
				Particle newParticle = new StandardParticle();
				//TODO need to initialize the particle
				newParticle.setPosition(builder.build());
				newParticle.updateFitness(provider.getParameterSet().getFitness());
				return newParticle;
			}
        };

		adaptorPSO.setTopology(algorithm.getTopology().map(create));
		adaptorPSO.performIteration();

		//iterate through particles of the adaptorPSO and update the parameters of the original PSO accordingly
		for(int i = 0; i < algorithm.getTopology().length(); i++){
			Particle p = algorithm.getTopology().index(i);
			StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
			SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider)behaviour.getVelocityProvider();
			
			Particle parameterParticle = adaptorPSO.getTopology().index(i);
			Vector parameterPosition = (Vector)parameterParticle.getPosition();
			provider.setInertiaWeight(ConstantControlParameter.of(parameterPosition.doubleValueOf(0)));
			provider.setCognitiveAcceleration(ConstantControlParameter.of(parameterPosition.doubleValueOf(1)));
			provider.setSocialAcceleration(ConstantControlParameter.of(parameterPosition.doubleValueOf(2)));

		}
	}

}
