/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.util.selection.weighting.behavior;

import cilib.entity.behaviour.Behaviour;

/**
 * Obtains the ratio of the ParticleBehavior based on how often it a Particle's personal best improves.
 */
public class SuccessRatio implements ParticleBehaviorRatio {
    @Override
    public double getRatio(Behaviour particleBehavior) {
        return particleBehavior.getSuccessCounter();
    }
}
