/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.problem.solution.InferiorFitness;
import cilib.problem.solution.OptimisationSolution;
import cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BestEntityPositionTest {

    @Test
    public void testBestParticlePositionDomain() {
        Vector expectedPosition = Vector.of(4.0);
        final Algorithm algorithm = mock(Algorithm.class);
        final OptimisationSolution mockSolution = new OptimisationSolution(expectedPosition, InferiorFitness.instance());

        when(algorithm.getBestSolution()).thenReturn(mockSolution);

        Measurement measurement = new BestEntityPosition();
        Assert.assertEquals(expectedPosition.toString(), measurement.getValue(algorithm).toString());
    }

}


