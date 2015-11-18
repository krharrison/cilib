/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.type.types.container;

import java.util.Collection;
import cilib.util.Visitor;
import cilib.type.types.Randomisable;
import cilib.type.types.Type;

/**
 * Description for all objects that maintain a structure or collection of objects.
 *
 * @param <E> the type of object the structure may contain.
 */
public interface StructuredType<E> extends Collection<E>, Type, Randomisable {

    @Override
    StructuredType<E> getClone();

    /**
     * Accept the {@linkplain Visitor} instance and perform the actions within the
     * {@linkplain Visitor} on the objects contained within this structure.
     * @param visitor The {@linkplain Visitor} instance to execute.
     */
    void accept(Visitor<E> visitor);

}
