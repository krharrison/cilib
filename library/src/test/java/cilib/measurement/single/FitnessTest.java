/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;

import cilib.problem.solution.MinimisationFitness;
import cilib.problem.solution.OptimisationSolution;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 */
public class FitnessTest {

    @Test
    public void results() {
        final Algorithm algorithm = mock(Algorithm.class);
        final OptimisationSolution mockSolution = new OptimisationSolution(Vector.of(1.0), new MinimisationFitness(0.0));

        when(algorithm.getBestSolution()).thenReturn(mockSolution);

        Measurement m = new Fitness();
        Assert.assertEquals(0.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
    }

}
