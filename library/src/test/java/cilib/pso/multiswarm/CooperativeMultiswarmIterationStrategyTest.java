/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.multiswarm;

import junit.framework.Assert;
import cilib.algorithm.initialisation.DataDependantPopulationInitialisationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.clustering.CooperativePSO;
import cilib.clustering.DataClusteringPSO;
import cilib.clustering.SlidingWindow;
import cilib.clustering.entity.ClusterParticle;
import cilib.clustering.iterationstrategies.CooperativeDataClusteringPSOIterationStrategy;
import cilib.measurement.generic.Iterations;
import cilib.problem.QuantisationErrorMinimisationProblem;
import cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import cilib.problem.boundaryconstraint.RandomBoundaryConstraint;
import cilib.stoppingcondition.Maximum;
import cilib.stoppingcondition.MeasuredStoppingCondition;
import org.junit.Test;

public class CooperativeMultiswarmIterationStrategyTest {

    /**
     * Test of performIteration method, of class CooperativeMultiswarmIterationStrategy.
     */
    @Test
    public void testPerformIteration() {
        DataClusteringPSO instance = new DataClusteringPSO();

        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setWindow(window);
        problem.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy = new CooperativeDataClusteringPSOIterationStrategy();
        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new RandomBoundaryConstraint());
        strategy.setBoundaryConstraint(constraint);
        instance.setOptimisationProblem(problem);
        DataDependantPopulationInitialisationStrategy init = new DataDependantPopulationInitialisationStrategy();

        init.setEntityType(new ClusterParticle());
        init.setEntityNumber(2);
        instance.setInitialisationStrategy(init);

        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition());

        CooperativePSO cooperative = new CooperativePSO();
        cooperative.setIterationStrategy(strategy);
        cooperative.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 30));
        cooperative.addPopulationBasedAlgorithm(instance);
        cooperative.setOptimisationProblem(problem);

        cooperative.performInitialisation();

        ClusterParticle particleBefore = instance.getTopology().head().getClone();

        cooperative.run();

        ClusterParticle particleAfter = instance.getTopology().head().getClone();

        Assert.assertFalse(particleAfter.getPosition().containsAll(particleBefore.getPosition()));

    }

    /**
     * Test of setDelegate method, of class CooperativeMultiswarmIterationStrategy.
     */
    @Test
    public void testSetDelegate() {
        IterationStrategy newDelegate = new CooperativeDataClusteringPSOIterationStrategy();
        CooperativeMultiswarmIterationStrategy instance = new CooperativeMultiswarmIterationStrategy();
        instance.setDelegate(newDelegate);

        Assert.assertEquals(newDelegate, instance.getDelegate());
    }

    /**
     * Test of getDelegate method, of class CooperativeMultiswarmIterationStrategy.
     */
    @Test
    public void testGetDelegate() {
        IterationStrategy newDelegate = new CooperativeDataClusteringPSOIterationStrategy();
        CooperativeMultiswarmIterationStrategy instance = new CooperativeMultiswarmIterationStrategy();
        instance.setDelegate(newDelegate);

        Assert.assertEquals(newDelegate, instance.getDelegate());
    }
}
