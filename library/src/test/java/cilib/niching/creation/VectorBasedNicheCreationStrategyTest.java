/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.niching.creation;

import fj.Ord;
import fj.Ordering;
import fj.data.List;
import cilib.entity.Property;
import cilib.pso.particle.Particle;
import cilib.niching.VectorBasedFunctions;
import cilib.problem.solution.Fitness;
import cilib.problem.solution.MinimisationFitness;
import cilib.pso.particle.StandardParticle;
import cilib.type.types.container.Vector;
import cilib.util.distancemeasure.DistanceMeasure;
import cilib.util.distancemeasure.EuclideanDistanceMeasure;
import static org.junit.Assert.*;
import org.junit.Test;

public class VectorBasedNicheCreationStrategyTest {

    @Test
    public void testDot() {
        Particle nBest = createParticle(new MinimisationFitness(0.0), Vector.of(0.0, 0.0), Vector.of(1.0, 1.0));
        Particle p1 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 0.0), Vector.of(2.0, 0.0));
        Particle p2 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 0.0), Vector.of(2.0, 1.0));
        Particle p3 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 0.0), Vector.of(2.0, 0.5));
        Particle p4 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 0.0), Vector.of(2.0, -0.5));
        Particle p5 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 0.0), Vector.of(2.0, -1.0));
        Particle p6 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 0.0), Vector.of(2.0, -2.0));

        assertEquals(2.0, VectorBasedFunctions.dot(nBest).f(nBest), 0.0);
        assertEquals(1.0, VectorBasedFunctions.dot(nBest).f(p2), 0.0);
        assertEquals(0.5, VectorBasedFunctions.dot(nBest).f(p3), 0.0);
        assertEquals(0.0, VectorBasedFunctions.dot(nBest).f(p1), 0.0);
        assertEquals(-0.5, VectorBasedFunctions.dot(nBest).f(p4), 0.0);
        assertEquals(-1.0, VectorBasedFunctions.dot(nBest).f(p5), 0.0);
        assertEquals(-2.0, VectorBasedFunctions.dot(nBest).f(p6), 0.0);
    }

    @Test
    public void testFilter() {
        DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
        Particle nBest = createParticle(new MinimisationFitness(0.0), Vector.of(0.0, 0.0), Vector.of(1.0, 1.0));
        Particle p1 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 0.0), Vector.of(2.0, 1.0));
        Particle p6 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 0.0), Vector.of(2.0, -2.0));

        assertTrue(VectorBasedFunctions.filter(distanceMeasure, nBest, 10.0).f(p1));
        assertFalse(VectorBasedFunctions.filter(distanceMeasure, nBest, 10.0).f(p6));
        assertFalse(VectorBasedFunctions.filter(distanceMeasure, nBest, 0.5).f(p1));
        assertFalse(VectorBasedFunctions.filter(distanceMeasure, nBest, 0.5).f(p6));
    }

    @Test
    public void testSortByDistance() {
        DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
        Particle nBest = createParticle(new MinimisationFitness(0.0), Vector.of(0.0, 0.0), Vector.of(1.0, 0.0));
        Particle p1 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 1.0), Vector.of(2.0, 1.0));
        Particle p2 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, -1.0), Vector.of(2.0, -2.0));
        Particle p3 = createParticle(new MinimisationFitness(0.0), Vector.of(0.5, 1.0), Vector.of(2.0, 1.0));

        assertEquals(Ordering.EQ, VectorBasedFunctions.sortByDistance(nBest, distanceMeasure).f(p1).f(p2));
        assertEquals(Ordering.LT, VectorBasedFunctions.sortByDistance(nBest, distanceMeasure).f(p1).f(p3));
        assertEquals(Ordering.GT, VectorBasedFunctions.sortByDistance(nBest, distanceMeasure).f(p3).f(p1));
        assertEquals(List.list(p2, p3).minimum(Ord.ord(VectorBasedFunctions.sortByDistance(nBest, distanceMeasure))).getPosition(), Vector.of(1.0, -1.0));
    }

    @Test
    public void testEqualParticle() {
        Particle p1 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 1.0), Vector.of(2.0, 1.0));
        Particle p2 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 1.0), Vector.of(2.0, 1.0));
        Particle p3 = createParticle(new MinimisationFitness(0.0), Vector.of(0.5, 1.0), Vector.of(2.0, 1.0));
        Particle p4 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 1.0), Vector.of(2.0, 2.0));
        Particle p5 = createParticle(new MinimisationFitness(1.0), Vector.of(1.0, 1.0), Vector.of(2.0, 1.0));

        assertTrue(VectorBasedFunctions.equalParticle.f(p1, p2));
        assertFalse(VectorBasedFunctions.equalParticle.f(p1, p3));
        assertFalse(VectorBasedFunctions.equalParticle.f(p1, p4));
        assertFalse(VectorBasedFunctions.equalParticle.f(p1, p5));
    }

    public static Particle createParticle(Fitness fitness, Vector position, Vector pBest) {
        Particle particle = new StandardParticle();

        particle.setPosition(position);
        particle.put(Property.FITNESS, fitness);
        particle.put(Property.BEST_POSITION, pBest);
        particle.put(Property.BEST_FITNESS, fitness);

        return particle;
    }
}
