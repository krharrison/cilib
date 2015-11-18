/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.util.selection.weighting.behavior;

import static com.google.common.base.Preconditions.checkState;
import java.util.List;
import cilib.entity.behaviour.Behaviour;

public class SpecialisedRatio implements ParticleBehaviorRatio {
    private List<Behaviour> behaviors;
    private List<Double> weights;

    @Override
    public double getRatio(Behaviour particleBehavior) {
        checkState(behaviors.size() > 0, "You must add particle behaviors to the behavior pool first.");
        checkState(weights.size() == behaviors.size(), "Make sure the behavior pool is the same size as the weights list.");

        return weights.get(behaviors.indexOf(particleBehavior));
    }

    public void setBehaviors(List<Behaviour> behaviors) {
        this.behaviors = behaviors;
    }

    public void setWeights(List<Double> weights) {
        this.weights = weights;
    }
}
