/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.entity.initialisation;

import cilib.entity.Entity;
import cilib.entity.Property;
import cilib.type.types.Type;
import cilib.type.types.container.Vector;

/**
 * TODO: this class should be refactored to use the RandomInitialVelocityStrategy or to be a compound
 * operation where the velocity is first randomised and then scaled by a percentage.
 * @param <E> The entity type.
 */
public class DomainPercentageInitialisationStrategy<E extends Entity> implements InitialisationStrategy<E> {

    private static final long serialVersionUID = -7178323673738508287L;
    private InitialisationStrategy velocityInitialisationStrategy;
    private double percentage;

    public DomainPercentageInitialisationStrategy() {
        this.velocityInitialisationStrategy = new RandomInitialisationStrategy();
        this.percentage = 0.1;
    }

    public DomainPercentageInitialisationStrategy(DomainPercentageInitialisationStrategy copy) {
        this.velocityInitialisationStrategy = copy.velocityInitialisationStrategy.getClone();
        this.percentage = copy.percentage;
    }

    @Override
    public DomainPercentageInitialisationStrategy getClone() {
        return new DomainPercentageInitialisationStrategy(this);
    }

    @Override
    public void initialise(Property key, E entity) {
        this.velocityInitialisationStrategy.initialise(Property.VELOCITY, entity);
        Type type = entity.get(key);
        Vector vector = (Vector) type;

        for (int i = 0; i < vector.size(); ++i) {
            vector.setReal(i, vector.doubleValueOf(i) * percentage);
        }
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public InitialisationStrategy getVelocityInitialisationStrategy() {
        return velocityInitialisationStrategy;
    }

    public void setVelocityInitialisationStrategy(InitialisationStrategy velocityInitialisationStrategy) {
        this.velocityInitialisationStrategy = velocityInitialisationStrategy;
    }
}
