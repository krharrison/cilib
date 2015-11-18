/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.boa.bee;

import cilib.algorithm.AbstractAlgorithm;
import cilib.boa.ABC;


/**
 * Represents an onlooker bee in the hive.
 *
 */
public class OnlookerBee extends AbstractBee {
    private static final long serialVersionUID = -4714791530850285930L;

    /**
     * Default constructor.
     */
    public OnlookerBee() {
    }

    /**
     * Copy constructor.
     * @param bee the original bee to copy.
     */
    public OnlookerBee(AbstractBee bee) {
        super(bee);
    }

    /**
     * Copy constructor. Creates a copy of the provided instance.
     * @param copy reference that is deep copied.
     */
    public OnlookerBee(OnlookerBee copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OnlookerBee getClone() {
        return new OnlookerBee(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePosition() {
        ABC algorithm = (ABC) AbstractAlgorithm.get();
        HoneyBee target = targetSelectionStrategy.on(algorithm.getWorkerBees()).exclude(this).select();

//        while (target == this) {
//            target = targetSelectionStrategy.on(algorithm.getWorkerBees()).select(Samples.first()).performSingle();
//        }

        this.positionUpdateStrategy.updatePosition(this, target);
    }

}
