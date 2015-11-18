/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.positionprovider;

import cilib.pso.particle.Particle;
import cilib.type.types.container.Vector;

/**
 * This is the normal position update as described by Kennedy and Eberhart.
 */
public class StandardPositionProvider implements PositionProvider {

    private static final long serialVersionUID = 5547754413670196513L;

    /**
     * Create an new instance of {@code StandardPositionProvider}.
     */
    public StandardPositionProvider() {
    }

    /**
     * Copy constructor. Copy the provided instance.
     * @param copy The instance to copy.
     */
    public StandardPositionProvider(StandardPositionProvider copy) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StandardPositionProvider getClone() {
        return new StandardPositionProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector get(Particle particle) {
        Vector position = (Vector) particle.getPosition();
        Vector velocity = (Vector) particle.getVelocity();
        return position.plus(velocity);
    }
}
