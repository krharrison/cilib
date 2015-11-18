/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.problem.solution.MaximisationFitness;
import cilib.problem.solution.OptimisationSolution;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class MaximumOfMeasurementTest {

    @Test
    public void results() {
        Algorithm algorithm = mock(Algorithm.class);
        OptimisationSolution mockSolution1 = new OptimisationSolution(Vector.of(1.0), new MaximisationFitness(-Double.MAX_VALUE));
        OptimisationSolution mockSolution2 = new OptimisationSolution(Vector.of(1.0), new MaximisationFitness(1.0));
        OptimisationSolution mockSolution3 = new OptimisationSolution(Vector.of(1.0), new MaximisationFitness(0.3));
        OptimisationSolution mockSolution4 = new OptimisationSolution(Vector.of(1.0), new MaximisationFitness(4.0));

        MaximumOfMeasurement m = new MaximumOfMeasurement();
        m.setMeasurement(new Fitness());
		
		when(algorithm.getIterations()).thenReturn(0);
        when(algorithm.getBestSolution()).thenReturn(mockSolution1);
        Assert.assertEquals(-Double.MAX_VALUE, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);

		when(algorithm.getIterations()).thenReturn(1);
        when(algorithm.getBestSolution()).thenReturn(mockSolution2);
        Assert.assertEquals(1.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
		
		when(algorithm.getIterations()).thenReturn(2);
        when(algorithm.getBestSolution()).thenReturn(mockSolution3);
        Assert.assertEquals(1.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
		
		when(algorithm.getIterations()).thenReturn(3);
        when(algorithm.getBestSolution()).thenReturn(mockSolution4);
        Assert.assertEquals(4.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
    }

}
