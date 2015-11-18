/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.entity.initialisation;

import cilib.entity.Entity;
import cilib.entity.Property;
import cilib.type.types.Randomisable;
import cilib.type.types.Type;

/**
 *
 * @param <E>
 */
public class RandomInitialisationStrategy<E extends Entity> implements InitialisationStrategy<E> {
    private static final long serialVersionUID = 5630272366805104400L;

    @Override
    public RandomInitialisationStrategy getClone() {
        return this;
    }

    @Override
    public void initialise(Property key, E entity) {
        Type type = entity.get(key);

        if (type instanceof Randomisable) {
            Randomisable randomisable = (Randomisable) type;
            randomisable.randomise();
            return;
        }

        throw new UnsupportedOperationException("Cannot initialise a non Randomisable instance.");
    }

}
