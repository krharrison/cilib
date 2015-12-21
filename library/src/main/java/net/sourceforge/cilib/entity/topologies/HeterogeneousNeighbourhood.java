/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import com.google.common.collect.Lists;
import fj.data.List;
import fj.data.Stream;
import net.sourceforge.cilib.entity.IndividualizedNeighbourhood;

/**
 * A neighbourhood strategy such that each entity has its own neighbourhood size.
 */
public class HeterogeneousNeighbourhood<E extends IndividualizedNeighbourhood> extends Neighbourhood<E> {

    @Override
    public List<E> f(List<E> list, E element) {
        int size = element.getNeighbourhoodSize();
        java.util.List<E> inner = Lists.newArrayList(list);
        int ts = inner.size();
        int x = (inner.indexOf(element) - (size / 2) + ts) % ts;

        return Stream.cycle(list.toStream()).drop(x).take(size).toList();
    }
}
