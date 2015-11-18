/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.positionprovider.binary;

import cilib.type.types.Bounds;
import cilib.type.types.Real;
import cilib.entity.Property;
import cilib.type.types.container.Vector;
import cilib.pso.particle.StandardParticle;
import cilib.pso.particle.Particle;
import cilib.controlparameter.ConstantControlParameter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test for binary inertia position provider
 */
public class BinaryInertiaPositionProviderTest {

    @Test
    public void testGet() {
        BinaryInertiaPositionProvider positionProvider = new BinaryInertiaPositionProvider();
        positionProvider.setDelta(ConstantControlParameter.of(0.25));

        Bounds bBounds = new Bounds(0.0, 1.0);
        Bounds vBounds = new Bounds(-7.0, 7.0);

        Particle particle = new StandardParticle();
        Vector updatedPosition;

        // initialise position [1.0, 0.0]
        particle.put(Property.CANDIDATE_SOLUTION,
            Vector.of(Real.valueOf(1.0, bBounds), Real.valueOf(0.0, bBounds)));

        // zero velocity
        changeVelocity(particle, Vector.of(
            Real.valueOf(0.0, vBounds),
            Real.valueOf(0.0, vBounds)));

        updatedPosition = positionProvider.get(particle);
        assertEquals(updatedPosition, Vector.of(Real.valueOf(1.0, bBounds), Real.valueOf(0.0, bBounds)));

        // low velocity
        changeVelocity(particle, Vector.of(
            Real.valueOf(-4.0, vBounds),
            Real.valueOf(-4.0, vBounds)));

        updatedPosition = positionProvider.get(particle);
        assertEquals(updatedPosition, Vector.of(Real.valueOf(0.0, bBounds), Real.valueOf(0.0, bBounds)));

        // high velocity
        changeVelocity(particle, Vector.of(
            Real.valueOf(4.0, vBounds),
            Real.valueOf(4.0, vBounds)));

        updatedPosition = positionProvider.get(particle);
        assertEquals(updatedPosition, Vector.of(Real.valueOf(1.0, bBounds), Real.valueOf(1.0, bBounds)));

        // medium velocity
        changeVelocity(particle, Vector.of(
            Real.valueOf(1.0, vBounds),
            Real.valueOf(1.0, vBounds)));

        updatedPosition = positionProvider.get(particle);
        assertEquals(updatedPosition, Vector.of(Real.valueOf(1.0, bBounds), Real.valueOf(0.0, bBounds)));

        positionProvider.setDelta(ConstantControlParameter.of(0.0));
        updatedPosition = positionProvider.get(particle);
        assertEquals(updatedPosition, Vector.of(Real.valueOf(1.0, bBounds), Real.valueOf(1.0, bBounds)));
    }

    private void changeVelocity(Particle p, Vector velocity) {
        p.put(Property.VELOCITY, velocity);
    }
}
