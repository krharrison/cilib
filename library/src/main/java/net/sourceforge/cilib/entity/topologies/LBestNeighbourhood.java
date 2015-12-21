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
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;

public class LBestNeighbourhood<E> extends Neighbourhood<E> {

    private ControlParameter n;

    public LBestNeighbourhood() {
        this.n = ConstantControlParameter.of(3);
    }

    public LBestNeighbourhood(int n) {
        this.n = ConstantControlParameter.of(n);
    }


    @Override
    public List<E> f(List<E> list, E element) {
        int size = (int)n.getParameter();
        java.util.List<E> inner = Lists.newArrayList(list);
        int ts = inner.size();
        int x = (inner.indexOf(element) - (size / 2) + ts) % ts;

        return Stream.cycle(list.toStream()).drop(x).take(size).toList();
    }

    public void setNeighbourhoodSize(int n) {
        this.n = ConstantControlParameter.of(n);
    }

    public void setNeighbourhoodSize(ControlParameter parameter){
        this.n = parameter;
    }
}
