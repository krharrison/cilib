/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.util.selection.weighting;

import cilib.entity.behaviour.Behaviour;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.util.selection.WeightedObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

public class ParameterSetFitnessWeighting implements Weighting{
    @Override
    public <T> Iterable<WeightedObject> weigh(Iterable<T> iterable) {
        Preconditions.checkArgument(Iterables.get(iterable, 0) instanceof ParameterSet, "Elements must be of type ParameterSet.");

        List<WeightedObject> result = Lists.newArrayList();

        for (T t : iterable) {
            ParameterSet p = (ParameterSet) t;
            result.add(new WeightedObject(t, p.getFitness().getValue()));
        }

        return result;
    }
}
