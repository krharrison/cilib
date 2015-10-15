/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive.adaptationstrategies;

import fj.F;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.ff.FFA;
import net.sourceforge.cilib.ff.firefly.Firefly;
import net.sourceforge.cilib.ff.firefly.StandardFirefly;
import net.sourceforge.cilib.ff.iterationstrategies.NonEvaluatingFireflyIterationStrategy;
import net.sourceforge.cilib.problem.boundaryconstraint.ClampingBoundaryConstraint;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.behaviour.StandardParticleBehaviour;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.velocityprovider.SelfAdaptiveVelocityProvider;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

public class FFAdaptationStrategy implements AlgorithmAdaptationStrategy {

	protected FFA adaptorFFA;
	
	public FFAdaptationStrategy(){
		adaptorFFA = new FFA(); //TODO clamping boundary constraint?
		NonEvaluatingFireflyIterationStrategy iterationStrategy = new NonEvaluatingFireflyIterationStrategy();
		iterationStrategy.setBoundaryConstraint(new ClampingBoundaryConstraint());
		adaptorFFA.setIterationStrategy(new NonEvaluatingFireflyIterationStrategy());
	}

	
	@Override
	public void adapt(PSO algorithm) {

		final F<Particle, Firefly> create = new F<Particle, Firefly>() {
			@Override
			public Firefly f(Particle p) {
				StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
				SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider)behaviour.getVelocityProvider();
				
				//create a new vector as [w, c1, c2] from the current particles parameter set
				Vector.Builder builder = Vector.newBuilder();
				builder.add(Real.valueOf(provider.getInertiaWeight().getParameter(), provider.getParameterSet().getInertiaBounds()));
				builder.add(Real.valueOf(provider.getCognitiveAcceleration().getParameter(), provider.getParameterSet().getCognitiveBounds()));
				builder.add(Real.valueOf(provider.getSocialAcceleration().getParameter(), provider.getParameterSet().getSocialBounds()));
				
				Firefly newFirefly = new StandardFirefly();
				newFirefly.setPosition(builder.build());
				newFirefly.put(Property.FITNESS, provider.getParameterSet().getFitness());
				return newFirefly;
			}
        };

		adaptorFFA.setTopology(algorithm.getTopology().map(create));
		adaptorFFA.performIteration();

		//iterate through fireflies of the adaptorFFA and update the parameters of the original PSO accordingly
		for(int i = 0; i < algorithm.getTopology().length(); i++){
			Particle p = algorithm.getTopology().index(i);
			StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
			SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider)behaviour.getVelocityProvider();
			
			Firefly parameterFirefly = adaptorFFA.getTopology().index(i);
			Vector parameterPosition = (Vector)parameterFirefly.getPosition();
			provider.setInertiaWeight(ConstantControlParameter.of(parameterPosition.doubleValueOf(0)));
			provider.setCognitiveAcceleration(ConstantControlParameter.of(parameterPosition.doubleValueOf(1)));
			provider.setSocialAcceleration(ConstantControlParameter.of(parameterPosition.doubleValueOf(2)));
		}
	}

}
