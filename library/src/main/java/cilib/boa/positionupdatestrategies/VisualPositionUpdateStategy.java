/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.boa.positionupdatestrategies;

import cilib.boa.bee.HoneyBee;
import cilib.entity.Property;
import cilib.math.random.generator.Rand;
import cilib.problem.solution.Fitness;
import cilib.type.types.container.Vector;

/**
 * Represents the visual exploration strategy used by bees to choose a their next
 * forage patch close by.
 *
 */
public class VisualPositionUpdateStategy implements BeePositionUpdateStrategy {

    private static final long serialVersionUID = 3782171955167557793L;

    /**
     * {@inheritDoc}
     */
    @Override
    public VisualPositionUpdateStategy getClone() {
        return new VisualPositionUpdateStategy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updatePosition(HoneyBee bee, HoneyBee otherBee) {
        int j = Rand.nextInt(bee.getDimension());

        Vector newPosition = (Vector) bee.getPosition();
        Vector oldPosition = Vector.copyOf((Vector) bee.getPosition());
        Vector otherPosition = (Vector) otherBee.getPosition();
        double value = newPosition.doubleValueOf(j);
        double other = otherPosition.doubleValueOf(j);
        newPosition.setReal(j, value + (Rand.nextDouble() * 2 - 1) * (value - other));

        //Determine if new position is better than old and update
        Fitness oldFitness = bee.getFitness().getClone();
        bee.updateFitness(bee.getBehaviour().getFitnessCalculator().getFitness(bee));
        Fitness newFitness = bee.getFitness();
        if (newFitness.compareTo(oldFitness) < 0) {
            bee.setPosition(oldPosition);
            bee.put(Property.FITNESS, oldFitness);
            return false;
        }

        return true;
    }
}
