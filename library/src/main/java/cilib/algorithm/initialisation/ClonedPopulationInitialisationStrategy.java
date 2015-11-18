/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.algorithm.initialisation;

import cilib.entity.Entity;
import cilib.problem.Problem;

import com.google.common.base.Preconditions;

import fj.data.List;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.util.functions.Entities;

/**
 * Create a collection of {@linkplain cilib.entity.Entity entities}
 * by cloning the given prototype {@link cilib.entity.Entity}.
 *
 * @param <E> The {@code Entity} type.
 */
public class ClonedPopulationInitialisationStrategy implements PopulationInitialisationStrategy {

    private static final long serialVersionUID = -7354579791235878648L;
    private Entity prototypeEntity;
    private ControlParameter entityNumber;

    /**
     * Create an instance of the {@code ClonedPopulationInitialisationStrategy}.
     */
    public ClonedPopulationInitialisationStrategy() {
        entityNumber = ConstantControlParameter.of(20);
        prototypeEntity = null; // This has to be manually set as Individuals are used in GAs etc...
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public ClonedPopulationInitialisationStrategy(ClonedPopulationInitialisationStrategy copy) {
        this.entityNumber = copy.entityNumber.getClone();

        if (copy.prototypeEntity != null) {
            this.prototypeEntity = copy.prototypeEntity.getClone();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClonedPopulationInitialisationStrategy getClone() {
        return new ClonedPopulationInitialisationStrategy(this);
    }

    /**
     * Perform the required initialisation, using the provided <tt>Topology</tt> and
     * <tt>Problem</tt>.
     * @param problem The <tt>Problem</tt> to use in the initialisation of the topology.
     * @return An {@code Iterable<E>} of cloned instances.
     * @throws InitialisationException if the initialisation cannot take place.
     */
    @Override
    public <E extends Entity> Iterable<E> initialise(final Problem problem) {
        Preconditions.checkNotNull(problem, "No problem has been specified");
        Preconditions.checkNotNull(prototypeEntity, "No prototype Entity object has been defined for the clone operation in the entity construction process.");

        return List.<E>replicate((int) entityNumber.getParameter(), (E) prototypeEntity)
                .map(Entities.<E>clone_().andThen(Entities.<E>initialise().f(problem)));
    }

    /**
     * Set the prototype {@linkplain cilib.entity.Entity entity} for the copy process.
     * @param entityType The {@code Entity} to use for the cloning process.
     */
    @Override
    public void setEntityType(Entity entityType) {
        this.prototypeEntity = entityType;
    }

    /**
     * Gets the {@link Entity} that has been defined as to copy.
     *
     * @return The prototype {@code Entity}.
     */
    @Override
    public Entity getEntityType() {
        return this.prototypeEntity;
    }

    /**
     * Get the defined number of {@code Entity} instances to create.
     * @return The number of {@code Entity} instances.
     */
    @Override
    public int getEntityNumber() {
        return (int) this.entityNumber.getParameter();
    }

    /**
     * Set the number of {@code Entity} instances to clone.
     * @param entityNumber The number to clone.
     */
    @Override
    public void setEntityNumber(int entityNumber) {
        this.entityNumber = ConstantControlParameter.of(entityNumber);
    }

    public void setEntityNumberControlParameter(ControlParameter entityNumber) {
        this.entityNumber = entityNumber;
    }
}
