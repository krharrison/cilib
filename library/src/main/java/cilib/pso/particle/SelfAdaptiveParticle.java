/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.particle;

import cilib.controlparameter.ControlParameter;
import cilib.entity.Property;
import cilib.problem.Problem;
import cilib.problem.solution.InferiorFitness;
import cilib.pso.behaviour.StandardParticleBehaviour;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.pso.velocityprovider.SelfAdaptiveVelocityProvider;
import cilib.type.types.Int;
import cilib.type.types.container.Vector;

/**
 * A self-adaptive particle contains the three conventional control parameters.
 */
public class SelfAdaptiveParticle extends StandardParticle {
    protected ParameterSet parameters;
    private double charge;

    public SelfAdaptiveParticle(){
        parameters = new ParameterSet();
        ((StandardParticleBehaviour) this.behaviour).setVelocityProvider(new SelfAdaptiveVelocityProvider());
    }

    public SelfAdaptiveParticle(SelfAdaptiveParticle copy){
        super(copy);
        this.parameters = copy.parameters.getClone();
    }

    @Override
    public SelfAdaptiveParticle getClone() {
        return new SelfAdaptiveParticle(this);
    }

    public void setParameterSet(ParameterSet parameters){
        this.parameters = parameters;
    }

    public ParameterSet getParameterSet(){
        return this.parameters;
    }

    /**
     * Get the {@linkplain ControlParameter} representing the inertia weight.
     * @return the inertia component {@linkplain ControlParameter}.
     */
    public ControlParameter getInertiaWeight() {
        return parameters.getInertiaWeight();
    }

    /**
     * Set the {@linkplain ControlParameter} for the inertia weight of the
     * velocity update equation.
     * @param inertiaWeight The inertiaWeight to set.
     */
    public void setInertiaWeight(ControlParameter inertiaWeight) {
        this.parameters.setInertiaWeight(inertiaWeight);
    }

    /**
     * Gets the {@linkplain ControlParameter} representing the cognitive
     * component.
     * @return the cognitiveComponent.
     */
    public ControlParameter getCognitiveAcceleration() {
        return this.parameters.getCognitiveAcceleration();
    }

    /**
     * Set the cognitive component {@linkplain ControlParameter}.
     * @param cognitiveComponent The cognitiveComponent to set.
     */
    public void setCognitiveAcceleration(ControlParameter cognitiveComponent) {
        this.parameters.setCognitiveAcceleration(cognitiveComponent);
    }

    /**
     * Get the {@linkplain ControlParameter} representing the social component
     * of the velocity update equation.
     * @return the socialComponent.
     */
    public ControlParameter getSocialAcceleration() {
        return this.parameters.getSocialAcceleration();
    }

    /**
     * Set the {@linkplain ControlParameter} for the social component.
     * @param socialComponent The socialComponent to set.
     */
    public void setSocialAcceleration(ControlParameter socialComponent) {
        this.parameters.setSocialAcceleration(socialComponent);
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
}
