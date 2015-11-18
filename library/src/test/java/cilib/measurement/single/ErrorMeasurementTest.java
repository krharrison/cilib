/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.problem.solution.MinimisationFitness;
import cilib.problem.solution.OptimisationSolution;
import cilib.problem.Problem;
import cilib.type.types.Real;
import cilib.type.types.Type;
import cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class ErrorMeasurementTest {

    @Test
    public void results() {
        final OptimisationSolution mockSolution = new OptimisationSolution(Vector.of(1.0), new MinimisationFitness(100.0));
        final Algorithm algorithm = mock(Algorithm.class);
        final Problem mockProblem = mock(Problem.class);

        when(algorithm.getBestSolution()).thenReturn(mockSolution);
        when(algorithm.getOptimisationProblem()).thenReturn(mockProblem);
        when(mockProblem.getFitness(Matchers.<Type>anyObject())).thenReturn(new MinimisationFitness(10.0));

        ErrorMeasurement m = new ErrorMeasurement();
        m.setTarget(10.0);

        Assert.assertEquals(0.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
    }

}
