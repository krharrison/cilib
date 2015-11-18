/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.velocityprovider;

import cilib.controlparameter.ConstantControlParameter;
import cilib.entity.Property;
import cilib.pso.particle.Particle;
import cilib.pso.particle.StandardParticle;
import cilib.type.types.Numeric;
import cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ClampingVelocityProviderTest {

    private Particle createParticle(Vector vector) {
        Particle particle = new StandardParticle();
        particle.put(Property.CANDIDATE_SOLUTION, vector);
        particle.put(Property.VELOCITY, Vector.of(0.0));
        particle.put(Property.BEST_POSITION, Vector.copyOf(vector));
        return particle;
    }

    /**
     * Test velocity clamping.
     */
    @Test
    public void testClamping() {
        Particle particle = createParticle(Vector.of(0.0));
        Particle nBest = createParticle(Vector.of(1.0));
        particle.setNeighbourhoodBest(nBest);
        nBest.setNeighbourhoodBest(nBest);

        ClampingVelocityProvider velocityProvider = new ClampingVelocityProvider();
        velocityProvider.setVMax(ConstantControlParameter.of(0.5));
        Vector velocity = velocityProvider.get(particle);

        for (Numeric number : velocity) {
            Assert.assertTrue(Double.compare(number.doubleValue(), 0.5) <= 0);
            Assert.assertTrue(Double.compare(number.doubleValue(), -0.5) >= 0);
        }
    }
}
