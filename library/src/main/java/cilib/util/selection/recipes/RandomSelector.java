/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.util.selection.recipes;

import cilib.util.selection.PartialSelection;
import cilib.util.selection.Selection;
import cilib.util.selection.arrangement.RandomArrangement;

/**
 * Perform a random selection from the provided list of elements.
 * <p>
 * Random selection is performed by:
 * <ol>
 *   <li>A random element is selected from the provided list.</li>
 *   <li>Return the result.</li>
 * </ol>
 * @param <E>
 */
public class RandomSelector<E> implements Selector<E> {
    private static final long serialVersionUID = -5099663528040315048L;

    @Override
    public PartialSelection<E> on(Iterable<E> iterable) {
        return (Selection<E>) Selection.copyOf(iterable).orderBy(new RandomArrangement());
    }
}
