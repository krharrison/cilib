/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.dynamic;

import cilib.entity.Property;
import cilib.problem.Problem;
import cilib.problem.solution.InferiorFitness;
import cilib.pso.behaviour.StandardParticleBehaviour;
import cilib.type.types.Int;
import cilib.type.types.container.Vector;

/**
 * Charged Particle used by charged PSO (ChargedVelocityProvider). The only
 * difference from DynamicParticle is that a charged particle stores the charge
 * magnitude and the initialisation strategy for charge.
 *
 *
 */
public class ChargedParticle extends DynamicParticle {

    private static final long serialVersionUID = 7872499872488908368L;

    private double charge;

    public ChargedParticle() {
        ((StandardParticleBehaviour) this.behaviour).setVelocityProvider(new ChargedVelocityProvider());
    }

    public ChargedParticle(ChargedParticle copy) {
        super(copy);
        this.charge = copy.charge;
    }

    @Override
    public ChargedParticle getClone() {
        return new ChargedParticle(this);
    }

    /**
     * @return the charge
     */
    public double getCharge() {
        return charge;
    }

    /**
     * @param charge the charge to set
     */
    public void setCharge(double charge) {
        this.charge = charge;
    }

    @Override
    public void initialise(Problem problem) {
        put(Property.CANDIDATE_SOLUTION, problem.getDomain().getBuiltRepresentation().getClone());
        put(Property.BEST_POSITION, Vector.copyOf((Vector) getPosition()));
        put(Property.VELOCITY, Vector.copyOf((Vector) getPosition()));

        this.positionInitialisationStrategy.initialise(Property.CANDIDATE_SOLUTION, this);
        this.personalBestInitialisationStrategy.initialise(Property.BEST_POSITION, this);
        this.velocityInitialisationStrategy.initialise(Property.VELOCITY, this);

        put(Property.FITNESS, InferiorFitness.instance());
        put(Property.BEST_FITNESS, InferiorFitness.instance());
        this.neighbourhoodBest = this;
        put(Property.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
    }
}
