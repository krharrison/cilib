/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.detectionstrategies.swarm;

import cilib.pso.PSO;

public interface SwarmUpdateDetectionStrategy {
    /**
     * Detect whether some condition holds that should prompt the entire {@link PSO}
     * swarm to change its parameters.
     *
     * @param pso  The swarm to inspect.
     * @return True if the swarm should change its parameters. False otherwise.
     */
    boolean detect(PSO pso);

    /**
     * Clone the current {@link SwarmUpdateDetectionStrategy}.
     *
     * @return A clone of this {@link SwarmUpdateDetectionStrategy}.
     */
    SwarmUpdateDetectionStrategy getClone();
}