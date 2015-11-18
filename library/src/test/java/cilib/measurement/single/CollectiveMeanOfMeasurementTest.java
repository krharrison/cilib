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
public class CollectiveMeanOfMeasurementTest {

    @Test
    public void results() {
        final Algorithm algorithm = mock(Algorithm.class);
        final OptimisationSolution mockSolution1 = new OptimisationSolution(Vector.of(1.0), new MinimisationFitness(3.0));
        final OptimisationSolution mockSolution2 = new OptimisationSolution(Vector.of(1.0), new MinimisationFitness(1.0));
        final OptimisationSolution mockSolution3 = new OptimisationSolution(Vector.of(1.0), new MinimisationFitness(2.3));
        final OptimisationSolution mockSolution4 = new OptimisationSolution(Vector.of(1.0), new MinimisationFitness(1.0));

        Measurement m = new CollectiveMeanOfMeasurement();
		
		when(algorithm.getIterations()).thenReturn(0);
        when(algorithm.getBestSolution()).thenReturn(mockSolution1);
        Assert.assertEquals(3.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);

		when(algorithm.getIterations()).thenReturn(0);
        when(algorithm.getBestSolution()).thenReturn(mockSolution2);
        Assert.assertEquals(3.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
		
		when(algorithm.getIterations()).thenReturn(1);
        when(algorithm.getBestSolution()).thenReturn(mockSolution2);
        Assert.assertEquals(2.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
		
		when(algorithm.getIterations()).thenReturn(1);
        when(algorithm.getBestSolution()).thenReturn(mockSolution3);
        Assert.assertEquals(2.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);

		when(algorithm.getIterations()).thenReturn(2);
        when(algorithm.getBestSolution()).thenReturn(mockSolution3);
        Assert.assertEquals(2.1, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
		
		when(algorithm.getIterations()).thenReturn(2);
        when(algorithm.getBestSolution()).thenReturn(mockSolution4);
        Assert.assertEquals(2.1, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
		
		when(algorithm.getIterations()).thenReturn(3);
        when(algorithm.getBestSolution()).thenReturn(mockSolution4);
        Assert.assertEquals(1.825, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
    }

}
