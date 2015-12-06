/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 *//*

package cilib.pso.behaviour.generators;

import cilib.entity.behaviour.Behaviour;
import cilib.entity.behaviour.generator.BehaviourGenerator;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.problem.boundaryconstraint.UnconstrainedBoundary;
import cilib.pso.behaviour.StandardParticleBehaviour;
import cilib.pso.velocityprovider.generators.RandomConvergentVelocityProviderGenerator;
import cilib.pso.velocityprovider.generators.VelocityProviderGenerator;

*/
/**
 * Construct a {@link StandardParticleBehaviour} using the provided
 * {@link VelocityProviderGenerator} to generate the particles initial
 * behaviour.
 *//*

public class StandardVelocityProviderBehaviourGenerator implements BehaviourGenerator {

	//TODO: add position provider as a parameter
	private VelocityProviderGenerator generator;
	private BoundaryConstraint boundaryConstraint;

	public StandardVelocityProviderBehaviourGenerator(){
		generator = new RandomConvergentVelocityProviderGenerator();
		boundaryConstraint = new UnconstrainedBoundary();
	}

	public StandardVelocityProviderBehaviourGenerator(StandardVelocityProviderBehaviourGenerator copy){
		this.generator = copy.generator.getClone();
		this.boundaryConstraint = copy.boundaryConstraint.getClone();
	}

	public void setVelocityProviderGenerator(VelocityProviderGenerator generator){
		this.generator = generator;
	}

	public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint){
		this.boundaryConstraint = boundaryConstraint;
	}

	@Override
	public StandardVelocityProviderBehaviourGenerator getClone() {
		return new StandardVelocityProviderBehaviourGenerator(this);
	}

	@Override
	public Behaviour generate() {
		//generate a new behaviour using the provided velocity provider generator
		StandardParticleBehaviour behaviour = new StandardParticleBehaviour();
		behaviour.setVelocityProvider(generator.generate());
		behaviour.setBoundaryConstraint(boundaryConstraint);
		return behaviour;
	}

}*/
