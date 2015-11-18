/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.guideprovider;

import cilib.pso.particle.Particle;
import cilib.type.types.container.StructuredType;
import cilib.util.Cloneable;

/**
 */
public interface GuideProvider extends Cloneable {

    @Override
    GuideProvider getClone();

    /**
     * Selects a guide for {@code particle}.
     * @param particle The particle who's guide will be selected.
     * @return The selected guide.
     */
    StructuredType get(Particle particle);
}
