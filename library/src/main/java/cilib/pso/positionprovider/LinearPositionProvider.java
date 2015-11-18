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
 * This sets the position to the velocity.
 *
 *
 */
public class LinearPositionProvider implements PositionProvider {

    private static final long serialVersionUID = -3328130784035172372L;

    public LinearPositionProvider() {
    }

    public LinearPositionProvider(LinearPositionProvider copy) {
    }

    @Override
    public LinearPositionProvider getClone() {
        return new LinearPositionProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector.Builder builder = Vector.newBuilder();

        for(int n = 0; n < position.size(); n++)
            builder.addWithin(velocity.doubleValueOf(n), position.boundsOf(n));

        return builder.build();
    }
}
