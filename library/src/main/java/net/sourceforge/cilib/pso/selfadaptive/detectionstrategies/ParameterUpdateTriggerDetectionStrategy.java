/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive.detectionstrategies;

import net.sourceforge.cilib.pso.particle.Particle;

public interface ParameterUpdateTriggerDetectionStrategy {
    /**
     * Detect whether some condition holds that should prompt a {@link Particle}
     * to change its parameters.
     *
     * @param particle  The particle to inspect.
     * @return True if the entity should change its parameters. False otherwise.
     */
    boolean detect(Particle particle);

    /**
     * Clone the current {@link ParameterUpdateTriggerDetectionStrategy}.
     *
     * @return A clone of this {@link ParameterUpdateTriggerDetectionStrategy}.
     */
    ParameterUpdateTriggerDetectionStrategy getClone();
}
