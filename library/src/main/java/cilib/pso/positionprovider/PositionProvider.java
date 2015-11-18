/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.positionprovider;

import cilib.pso.particle.Particle;
import cilib.type.types.container.StructuredType;
import cilib.util.Cloneable;


/**
 * TODO: Complete this javadoc.
 *
 */
public interface PositionProvider extends Cloneable {

    /**
     * Clone the strategy by creating a new object with the same contents and values
     * as the current object.
     *
     * @return A clone of the current <tt>PositionProvider</tt>
     */
    PositionProvider getClone();

    /**
     * Update the position of the <tt>Particle</tt>.
     *
     * @param particle The <tt>Particle</tt> to perform the position update on.
     */
    StructuredType get(Particle particle);

}
