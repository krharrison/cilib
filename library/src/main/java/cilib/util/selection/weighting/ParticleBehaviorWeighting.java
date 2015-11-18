/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.util.selection.weighting;

import cilib.util.selection.weighting.behavior.ParticleBehaviorRatio;
import cilib.util.selection.weighting.behavior.SuccessRatio;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;
import cilib.entity.behaviour.Behaviour;
import cilib.util.selection.WeightedObject;

/**
 * Weighs ParticleBehaviors mainly for HPSO.
 */
public class ParticleBehaviorWeighting implements Weighting {

    private ParticleBehaviorRatio ratio;

    public ParticleBehaviorWeighting() {
        this.ratio = new SuccessRatio();
    }

    public ParticleBehaviorWeighting(ParticleBehaviorRatio ratio) {
        this.ratio = ratio;
    }

    @Override
    public <T> Iterable<WeightedObject> weigh(Iterable<T> iterable) {
        Preconditions.checkArgument(Iterables.get(iterable, 0) instanceof Behaviour);
        List<WeightedObject> result = Lists.newArrayList();

        for (T t : iterable) {
            double weight = ratio.getRatio((Behaviour) t);
            result.add(new WeightedObject(t, weight));
        }

        return result;
    }

    public void setRatio(ParticleBehaviorRatio ratio) {
        this.ratio = ratio;
    }
}
