/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Property;
import cilib.measurement.Measurement;
import cilib.pso.particle.Particle;
import cilib.pso.particle.StandardParticle;
import cilib.type.types.Bounds;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;

import org.junit.Assert;
import org.junit.Test;

public class ParticleBoundViolationsTest {

    @Test
    public void testParticleBoundViolations() {
        Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();
        Particle p3 = new StandardParticle();
        Particle p4 = new StandardParticle();

        Bounds bounds = new Bounds(0.0, 2.0);
        p1.put(Property.CANDIDATE_SOLUTION, vectorOf(bounds, 0.0, -1.0, 0.0));
        p2.put(Property.CANDIDATE_SOLUTION, vectorOf(bounds, 1.0, 2.0, 2.0));
        p3.put(Property.CANDIDATE_SOLUTION, vectorOf(bounds, -1.0,0.0,1.0));
        p4.put(Property.CANDIDATE_SOLUTION, vectorOf(bounds, 1.0,2.0,-1.0));

        final fj.data.List<Particle> topology = fj.data.List.list(p1, p2, p3, p4);

        final SinglePopulationBasedAlgorithm pba = mock(SinglePopulationBasedAlgorithm.class);

        when(pba.getTopology()).thenReturn((fj.data.List) topology);

        Measurement m = new ParticleBoundViolations();
        Assert.assertEquals(Real.valueOf(3.0/topology.length()), m.getValue(pba));

        verify(pba, atLeast(1)).getTopology();
    }

    private Vector vectorOf(Bounds bounds, double... values) {
        Vector.Builder vector = Vector.newBuilder();
        for (int i = 0; i < values.length; i++) {
            vector.add(Real.valueOf(values[i], bounds));
        }
        return vector.build();
    }

}
