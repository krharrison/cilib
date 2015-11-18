/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.velocityprovider;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.entity.Property;
import cilib.pso.particle.Particle;
import cilib.math.Maths;
import cilib.math.random.generator.Rand;
import cilib.pso.behaviour.StandardParticleBehaviour;
import cilib.pso.particle.StandardParticle;
import cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * Unit test for the constriction velocity update.
 */
public class ConstrictionVelocityProviderTest {

    /**
     * Test cloning and implicitly the copy constructor.
     */
    @Test
    public void getClone() {
        ConstrictionVelocityProvider original = new ConstrictionVelocityProvider();
        ConstrictionVelocityProvider copy = original.getClone();

        Assert.assertEquals(original.getKappa().getParameter(), copy.getKappa().getParameter(), Maths.EPSILON);
        //Assert.assertEquals(original.getVMax().getParameter(), copy.getVMax().getParameter(), Maths.EPSILON);
        Assert.assertEquals(original.getCognitiveAcceleration().getParameter(), copy.getCognitiveAcceleration().getParameter(), Maths.EPSILON);
        Assert.assertEquals(original.getSocialAcceleration().getParameter(), copy.getSocialAcceleration().getParameter(), Maths.EPSILON);

        copy.setKappa(ConstantControlParameter.of(0.7));
        //copy.setVMax(ConstantControlParameter.of(0.7));
        ControlParameter controlParameter = ConstantControlParameter.of(4.0);
        copy.setSocialAcceleration(controlParameter.getClone());
        copy.setCognitiveAcceleration(controlParameter.getClone());

        Assert.assertFalse(Double.compare(original.getKappa().getParameter(), copy.getKappa().getParameter()) == 0);
        //Assert.assertFalse(Double.compare(original.getVMax().getParameter(), copy.getVMax().getParameter()) == 0);
        Assert.assertFalse(Double.compare(original.getCognitiveAcceleration().getParameter(),
                (copy.getCognitiveAcceleration()).getParameter()) == 0);
        Assert.assertFalse(Double.compare(original.getSocialAcceleration().getParameter(),
                (copy.getSocialAcceleration()).getParameter()) == 0);
    }

    /**
     * Test the velocity provider as well as the constraint assertion. This
     * sadly needs to use an annoying try..finally to ensure that the type
     * of random numbers is expected to reproduce values always.
     */
    @Test
    public void velocityProvision() {
        Rand.setSeed(0);
        Particle particle = createParticle(Vector.of(0.0));
        Particle nBest = createParticle(Vector.of(1.0));
        particle.setNeighbourhoodBest(nBest);
        nBest.setNeighbourhoodBest(nBest);

        ConstrictionVelocityProvider velocityProvider = new ConstrictionVelocityProvider();
        Vector velocity = velocityProvider.get(particle);

        Assert.assertEquals(0.20269795364089954, velocity.doubleValueOf(0), Maths.EPSILON);
    }

    @Test
    public void testConstrictionCalculation() {
        Rand.setSeed(0);
        ConstrictionVelocityProvider velocityProvider = new ConstrictionVelocityProvider();
        Particle particle = createParticle(Vector.of(0.0));
        ((StandardParticleBehaviour) particle.getBehaviour()).setVelocityProvider(velocityProvider);
        Particle nBest = createParticle(Vector.of(1.0));
        particle.setNeighbourhoodBest(nBest);
        nBest.setNeighbourhoodBest(nBest);
        Particle clone = particle.getClone();

        ((StandardParticleBehaviour) particle.getBehaviour()).getVelocityProvider().get(particle);
        ((StandardParticleBehaviour) clone.getBehaviour()).getVelocityProvider().get(particle);

        double kappa = 1.0;
        double c1 = 2.05;
        double c2 = 2.05;
        double phi = c1 + c2;
        double chi = (2 * kappa) / Math.abs(2 - phi - Math.sqrt(phi * phi - 4.0 * phi)); //this was not copied from the implementation.

        //verify implementation maths is correct.
        Assert.assertEquals(chi, velocityProvider.getConstrictionCoefficient().getParameter(), Maths.EPSILON);
        //verify it is the same for two particles.

        Assert.assertEquals(((ConstrictionVelocityProvider) ((StandardParticleBehaviour) particle.getBehaviour()).getVelocityProvider()).getConstrictionCoefficient().getParameter(),
                ((ConstrictionVelocityProvider) ((StandardParticleBehaviour) clone.getBehaviour()).getVelocityProvider()).getConstrictionCoefficient().getParameter(), Maths.EPSILON);
    }

    private Particle createParticle(Vector vector) {
        Particle particle = new StandardParticle();
        particle.put(Property.CANDIDATE_SOLUTION, vector);
        particle.put(Property.VELOCITY, Vector.of(0.0));
        particle.put(Property.BEST_POSITION, Vector.copyOf(vector));

        return particle;
    }

    @Test(expected = UnsupportedOperationException.class)
    public void illegalVelocityProvision() {
        final Particle particle = mock(Particle.class);

        ControlParameter controlParameter = ConstantControlParameter.of(0.0);
        ConstrictionVelocityProvider velocityProvider = new ConstrictionVelocityProvider();
        velocityProvider.setCognitiveAcceleration(controlParameter);
        velocityProvider.setSocialAcceleration(controlParameter);

        velocityProvider.get(particle);
    }
}
