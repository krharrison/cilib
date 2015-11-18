/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.entity;

import cilib.entity.behaviour.Behaviour;
import cilib.entity.behaviour.DoNothingBehaviour;
import cilib.problem.Problem;
import cilib.problem.solution.Fitness;
import cilib.type.types.Blackboard;
import cilib.type.types.Type;
import cilib.type.types.container.StructuredType;

/**
 * Abstract class definition for all concrete {@linkplain Entity} objects.
 * This class defines the {@linkplain Entity} main data structure for the
 * values stored within the {@linkplain Entity} itself.
 */
public abstract class AbstractEntity implements Entity {

    private static final long serialVersionUID = 3104817182593047611L;

    protected Behaviour behaviour;
    private final Blackboard<Property, Type> properties;

    /**
     * Initialise the candidate solution of the {@linkplain Entity}.
     */
    protected AbstractEntity() {
        this.behaviour = new DoNothingBehaviour();
        this.properties = new Blackboard();
    }

    /**
     * Copy constructor. Instantiate and copy the given instance.
     * @param copy The instance to copy.
     */
    protected AbstractEntity(AbstractEntity copy) {
        this.properties = copy.properties.getClone();

        this.behaviour = copy.behaviour;
    }

    /**
     * Get the properties associate with the <code>Entity</code>.
     * @return The properties within a {@linkplain Blackboard}.
     */
    @Override
    public final Blackboard<Property, Type> getProperties() {
        return properties;
    }
    
    @Override
    public final <T extends Type> T get(Property<T> p) {
        return (T) properties.get(p);
    }
    
    @Override
    public final <T extends Type> void put(Property<T> p, T v) {
        properties.put(p, v);
    }
    
    @Override
    public final <T extends Type> boolean has(Property<T> p) {
        return get(p) != null;
    }

    /**
     * Get the value of the candidate solution maintained by this
     * {@linkplain Entity}.
     * @return The candidate solution as a {@linkplain Type}.
     */
    @Override
    public StructuredType getPosition() {
        return get(Property.CANDIDATE_SOLUTION);
    }

    /**
     * Get the fitness of the candidate solution maintained by this
     * {@linkplain Entity}.
     * @return The {@linkplain Fitness} of the candidate solution.
     */
    @Override
    public Fitness getFitness() {
        return get(Property.FITNESS);
    }

    /**
     * Set the {@linkplain Type} maintained by this {@linkplain Entity}s
     * candidate solution
     * @param candidateSolution The {@linkplain Type} that will be the new value of the
     *        {@linkplain Entity} candidate solution.
     */
    @Override
    public void setPosition(StructuredType candidateSolution) {
        put(Property.CANDIDATE_SOLUTION, candidateSolution);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fitness getBestFitness() {
        return getFitness();
    }

    @Override
    public abstract AbstractEntity getClone();

    @Override
    public void updateFitness(Fitness newFitness) {
        properties.put(Property.PREVIOUS_FITNESS, getFitness().getClone());
        properties.put(Property.FITNESS, newFitness);
    }

    @Override
    public abstract void initialise(Problem problem);

    @Override
    public int getDimension() {
        return getPosition().size();
    }

    @Override
    public abstract void reinitialise();

    @Override
    public int compareTo(Entity o) {
        return getFitness().compareTo(o.getFitness());
    }

    @Override
    public void setBehaviour(Behaviour behaviour) {
        this.behaviour = behaviour;
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
