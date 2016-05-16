/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.math.random.generator.Rand;
import cilib.problem.solution.Fitness;
import cilib.problem.solution.MaximisationFitness;
import cilib.pso.velocityprovider.VelocityProvider;
import cilib.type.types.Bounds;
import cilib.type.types.Type;
import cilib.type.types.container.Vector;
import cilib.util.Cloneable;
import fj.test.Bool;


public class ParameterSet implements Cloneable, Comparable<ParameterSet> {

    protected Bounds inertiaBounds;
    protected Bounds socialBounds;
    protected Bounds cognitiveBounds;

    protected ControlParameter inertiaWeight;
    protected ControlParameter socialAcceleration;
    protected ControlParameter cognitiveAcceleration;

    protected Fitness fitness;

    public ParameterSet() {

        inertiaWeight = ConstantControlParameter.of(0.729844);
        socialAcceleration = ConstantControlParameter.of(1.496180);
        cognitiveAcceleration = ConstantControlParameter.of(1.496180);

        inertiaBounds = new Bounds(0, 1);
        socialBounds = new Bounds(0, 4);
        cognitiveBounds = new Bounds(0, 4);

        this.fitness = new MaximisationFitness(0.0);
    }

    public ParameterSet(ParameterSet copy){
        this.inertiaWeight = copy.inertiaWeight.getClone();
        this.socialAcceleration = copy.socialAcceleration.getClone();
        this.cognitiveAcceleration = copy.cognitiveAcceleration.getClone();

        this.inertiaBounds = copy.inertiaBounds;
        this.socialBounds = copy.socialBounds;
        this.cognitiveBounds = copy.cognitiveBounds;

        this.fitness = copy.fitness.getClone();
    }

    @Override
    public ParameterSet getClone() {
        return new ParameterSet(this);
    }

    public Boolean isConvergent(){
        double inertia = inertiaWeight.getParameter();
        double check = (24 * (1 - inertia * inertia)) / (7 - 5 * inertia);

        return cognitiveAcceleration.getParameter() + socialAcceleration.getParameter() < check;
    }

    @Override
    public int compareTo(ParameterSet other) {
        return this.fitness.compareTo(other.fitness);
    }


    public void incrementFitness(double value){
        this.fitness = this.fitness.newInstance(this.fitness.getValue() + value);
    }

    public void decrementFitness(double value){
        this.fitness = this.fitness.newInstance(this.fitness.getValue() - value);
    }


    /**
     * Resets the {@link Fitness} value to 0.0.
     */
    public void resetFitness(){
        this.fitness = this.fitness.newInstance(0.0);
    }

    public Vector asVector(){
        Vector.Builder builder =  Vector.newBuilder();
        builder.add(inertiaWeight.getParameter());
        builder.add(cognitiveAcceleration.getParameter());
        builder.add(socialAcceleration.getParameter());

        return builder.build();
    }

    public void fromVector(Vector vector){
        inertiaWeight = ConstantControlParameter.of(vector.get(0).doubleValue());
        cognitiveAcceleration = ConstantControlParameter.of(vector.get(1).doubleValue());
        socialAcceleration = ConstantControlParameter.of(vector.get(2).doubleValue());
    }

    public void setFitness(Fitness value){
        this.fitness = value;
    }

    /**
     * Sets the @{link Fitness} to the specified value.
     * @param value The new fitness value.
     */
    public void setFitness(double value){
        this.fitness = this.fitness.newInstance(value);
    }

    public Fitness getFitness(){
        return this.fitness;
    }
    /**
     * Sets the range for the inertia coefficient.
     * @param inertiaBounds The bounds for the inertia coefficient.
     */
    public void setInertiaBounds(Bounds inertiaBounds){
        this.inertiaBounds = inertiaBounds;
    }

    public Bounds getInertiaBounds(){
        return this.inertiaBounds;
    }

    /**
     * Sets the range for the social coefficient.
     * @param socialBounds The bounds for the social coefficient.
     */
    public void setSocialBounds(Bounds socialBounds){
        this.socialBounds = socialBounds;
    }

    public Bounds getSocialBounds(){
        return this.socialBounds;
    }

    /**
     * Sets the range for the cognitive coefficient.
     * @param cognitiveBounds The bounds for the cognitive coefficient.
     */
    public void setCognitiveBounds(Bounds cognitiveBounds){
        this.cognitiveBounds = cognitiveBounds;
    }

    public Bounds getCognitiveBounds(){
        return this.cognitiveBounds;
    }

    /**
     * Get the {@linkplain ControlParameter} representing the inertia weight of
     * the {@linkplain VelocityProvider}.
     * @return the inertia component {@linkplain ControlParameter}.
     */
    public ControlParameter getInertiaWeight() {
        return inertiaWeight;
    }

    /**
     * Set the {@linkplain ControlParameter} for the inertia weight of the
     * velocity update equation.
     * @param inertiaWeight The inertiaWeight to set.
     */
    public void setInertiaWeight(ControlParameter inertiaWeight) {
        this.inertiaWeight = inertiaWeight;
    }

    /**
     * Gets the {@linkplain ControlParameter} representing the cognitive
     * component within this {@link VelocityProvider}.
     * @return the cognitiveComponent.
     */
    public ControlParameter getCognitiveAcceleration() {
        return cognitiveAcceleration;
    }

    /**
     * Set the cognitive component {@linkplain ControlParameter}.
     * @param cognitiveComponent The cognitiveComponent to set.
     */
    public void setCognitiveAcceleration(ControlParameter cognitiveComponent) {
        this.cognitiveAcceleration = cognitiveComponent;
    }

    /**
     * Get the {@linkplain ControlParameter} representing the social component
     * of the velocity update equation.
     * @return the socialComponent.
     */
    public ControlParameter getSocialAcceleration() {
        return socialAcceleration;
    }

    /**
     * Set the {@linkplain ControlParameter} for the social component.
     * @param socialComponent The socialComponent to set.
     */
    public void setSocialAcceleration(ControlParameter socialComponent) {
        this.socialAcceleration = socialComponent;
    }
}