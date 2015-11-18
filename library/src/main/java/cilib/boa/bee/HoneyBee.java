/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.boa.bee;

import cilib.boa.positionupdatestrategies.BeePositionUpdateStrategy;
import cilib.entity.Entity;
import cilib.problem.solution.Fitness;

/**
 * Super interface for all types of bees in the artificial bee algorithm.
 */
public interface HoneyBee extends Entity {

    /**
     * {@inheritDoc}
     */
    @Override
    HoneyBee getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    Fitness getFitness();

    /**
     * Updates the position of the bee based on the neighbouring nectar content.
     */
    void updatePosition();

//    /**
//     * Gets the bee's position (contents).
//     * @return the position.
//     */
//    Vector getPosition();
//
//    /**
//     * Sets the bee's position (contents).
//     * @param position The value to set.
//     */
//    void setPosition(Vector position);

    /**
     * Getter method for the position update strategy.
     * @return the position update strategy.
     */
    BeePositionUpdateStrategy getPositionUpdateStrategy();

}
