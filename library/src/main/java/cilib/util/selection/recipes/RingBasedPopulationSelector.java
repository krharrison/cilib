/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.util.selection.recipes;

import cilib.algorithm.AbstractAlgorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.util.selection.PartialSelection;
import cilib.util.selection.Selection;
import cilib.util.selection.arrangement.RingBasedArrangement;

/**
 */
public class RingBasedPopulationSelector implements Selector<SinglePopulationBasedAlgorithm> {

    private static final long serialVersionUID = 8899308548978334236L;

    /**
     * {@inheritDoc}
     */
    @Override
    public PartialSelection<SinglePopulationBasedAlgorithm> on(Iterable<SinglePopulationBasedAlgorithm> iterable) {
        return Selection.copyOf(iterable).orderBy(new RingBasedArrangement(AbstractAlgorithm.get()));
    }
}
