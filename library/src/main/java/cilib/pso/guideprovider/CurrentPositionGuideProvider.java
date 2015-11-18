/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.guideprovider;

import cilib.pso.particle.Particle;
import cilib.type.types.container.StructuredType;

public class CurrentPositionGuideProvider implements GuideProvider {

    public GuideProvider getClone() {
        return this;
    }

    public StructuredType get(Particle particle) {
        return particle.getPosition();
    }

}
