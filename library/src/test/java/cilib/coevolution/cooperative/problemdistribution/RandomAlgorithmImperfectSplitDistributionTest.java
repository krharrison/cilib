/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.coevolution.cooperative.problemdistribution;

import java.util.Arrays;
import java.util.List;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import cilib.math.random.generator.Rand;
import cilib.problem.Problem;
import cilib.pso.PSO;
import cilib.type.DomainRegistry;
import cilib.type.StringBasedDomainRegistry;
import cilib.type.types.Bounds;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RandomAlgorithmImperfectSplitDistributionTest {
    @Test
    public void RandomAlgorithmImperfectSplitTest(){
        Rand.setSeed(0);
        final DomainRegistry problemDomain = new StringBasedDomainRegistry();
        problemDomain.setDomainString("R(0.0:4.0)^5");
        Bounds bounds = new Bounds(0.0, 4.0);
        Vector data = Vector.of(Real.valueOf(0.0, bounds),
            Real.valueOf(0.0, bounds),
            Real.valueOf(0.0, bounds),
            Real.valueOf(0.0, bounds),
            Real.valueOf(0.0, bounds));

        List<SinglePopulationBasedAlgorithm> populations = Arrays.asList((SinglePopulationBasedAlgorithm)new PSO(), (SinglePopulationBasedAlgorithm)new PSO());

        final Problem problem = mock(Problem.class);

        when(problem.getDomain()).thenReturn(problemDomain);

        RandomAlgorithmImperfectSplitDistribution test = new RandomAlgorithmImperfectSplitDistribution();
        test.performDistribution(populations, problem, data);

        CooperativeCoevolutionProblemAdapter p1 = (CooperativeCoevolutionProblemAdapter)populations.get(0).getOptimisationProblem();
        CooperativeCoevolutionProblemAdapter p2 = (CooperativeCoevolutionProblemAdapter)populations.get(1).getOptimisationProblem();

        assertEquals(2, p1.getDomain().getDimension(), 0.0);
        assertEquals(3, p2.getDomain().getDimension(), 0.0);

        assertEquals(3, p1.getProblemAllocation().getProblemIndex(0), 0.0);
        assertEquals(4, p1.getProblemAllocation().getProblemIndex(1), 0.0);

        assertEquals(0, p2.getProblemAllocation().getProblemIndex(0), 0.0);
        assertEquals(1, p2.getProblemAllocation().getProblemIndex(1), 0.0);
        assertEquals(2, p2.getProblemAllocation().getProblemIndex(2), 0.0);
    }
}
