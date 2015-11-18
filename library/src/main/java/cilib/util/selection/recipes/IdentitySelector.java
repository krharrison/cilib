/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.util.selection.recipes;

import cilib.util.selection.PartialSelection;
import cilib.util.selection.Selection;

public class IdentitySelector<E extends Comparable> implements Selector<E> {
    
    @Override
    public PartialSelection<E> on(Iterable<E> iterable) {
        return Selection.copyOf(iterable);
    }
    
}
