/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.clustering;

import java.util.ArrayList;
import junit.framework.Assert;
import cilib.algorithm.initialisation.DataDependantPopulationInitialisationStrategy;
import cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.clustering.entity.ClusterParticle;
import cilib.clustering.iterationstrategies.StandardDataClusteringIterationStrategy;
import cilib.entity.Property;
import cilib.measurement.generic.Iterations;
import cilib.problem.ClusteringProblem;
import cilib.problem.QuantisationErrorMinimisationProblem;
import cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import cilib.problem.boundaryconstraint.RandomBoundaryConstraint;
import cilib.problem.solution.MinimisationFitness;
import cilib.problem.solution.OptimisationSolution;
import cilib.stoppingcondition.Maximum;
import cilib.stoppingcondition.MeasuredStoppingCondition;
import cilib.type.types.container.CentroidHolder;
import cilib.type.types.container.ClusterCentroid;
import org.junit.Test;

public class CooperativePSOTest {

    /**
     * Test of algorithmIteration method, of class CooperativePSO.
     */
    @Test
    public void testAlgorithmIteration() {
        DataClusteringPSO instance = new DataClusteringPSO();

        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setWindow(window);
        problem.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy = new StandardDataClusteringIterationStrategy();
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

        DataClusteringPSO instance2 = new DataClusteringPSO();

        QuantisationErrorMinimisationProblem problem2 = new QuantisationErrorMinimisationProblem();
        problem.setWindow(window);
        problem2.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy2 = new StandardDataClusteringIterationStrategy();
        CentroidBoundaryConstraint constraint2 = new CentroidBoundaryConstraint();
        constraint2.setDelegate(new RandomBoundaryConstraint());
        strategy2.setBoundaryConstraint(constraint2);
        instance2.setOptimisationProblem(problem2);
        DataDependantPopulationInitialisationStrategy init2 = new DataDependantPopulationInitialisationStrategy();

        init2.setEntityType(new ClusterParticle());
        init2.setEntityNumber(2);
        instance2.setInitialisationStrategy(init2);

        instance2.setOptimisationProblem(problem2);
        instance2.addStoppingCondition(new MeasuredStoppingCondition());

        DataClusteringPSO instance3 = new DataClusteringPSO();

        QuantisationErrorMinimisationProblem problem3 = new QuantisationErrorMinimisationProblem();
        problem.setWindow(window);
        problem3.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy3 = new StandardDataClusteringIterationStrategy();
        CentroidBoundaryConstraint constraint3 = new CentroidBoundaryConstraint();
        constraint3.setDelegate(new RandomBoundaryConstraint());
        strategy3.setBoundaryConstraint(constraint3);
        instance3.setOptimisationProblem(problem3);
        DataDependantPopulationInitialisationStrategy init3 = new DataDependantPopulationInitialisationStrategy();

        init3.setEntityType(new ClusterParticle());
        init3.setEntityNumber(2);
        instance3.setInitialisationStrategy(init3);

        instance3.setOptimisationProblem(problem3);
        instance3.addStoppingCondition(new MeasuredStoppingCondition());

        CooperativePSO cooperative = new CooperativePSO();
        cooperative.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 30));
        cooperative.addPopulationBasedAlgorithm(instance);
        cooperative.addPopulationBasedAlgorithm(instance2);
        cooperative.addPopulationBasedAlgorithm(instance3);
        cooperative.setOptimisationProblem(problem);

        cooperative.performInitialisation();

        ClusterParticle particleBefore = instance.getTopology().head().getClone();

        cooperative.run();

        ClusterParticle particleAfter = instance.getTopology().head().getClone();

        Assert.assertFalse(particleAfter.getPosition().containsAll(particleBefore.getPosition()));
    }

    /**
     * Test of getSolutions method, of class CooperativePSO.
     */
    @Test
    public void testGetSolutions() {
        DataClusteringPSO standard  = new DataClusteringPSO();
        ClusterParticle particle = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        holder.add(ClusterCentroid.of(1,2,3,4,5));
        holder.add(ClusterCentroid.of(5,4,3,2,1));
        particle.setPosition(holder);
        particle.put(Property.FITNESS, new MinimisationFitness(2.0));
        particle.put(Property.BEST_FITNESS, new MinimisationFitness(2.0));
        particle.put(Property.BEST_POSITION, particle.getPosition());
        particle.setNeighbourhoodBest(particle);
//        standard.getTopology().add(particle);

        ClusterParticle otherParticle = new ClusterParticle();
        CentroidHolder otherHolder = new CentroidHolder();
        otherHolder.add(ClusterCentroid.of(6,7,8,9,10));
        otherHolder.add(ClusterCentroid.of(10,9,8,7,6));
        otherParticle.setPosition(otherHolder);
        otherParticle.put(Property.FITNESS, new MinimisationFitness(1.0));
        otherParticle.put(Property.BEST_FITNESS, new MinimisationFitness(1.0));
        otherParticle.put(Property.BEST_POSITION, otherParticle.getPosition());
        otherParticle.setNeighbourhoodBest(otherParticle);
//        standard.getTopology().add(otherParticle);

        standard.setTopology(fj.data.List.list(particle, otherParticle));

        DataClusteringPSO standard2  = new DataClusteringPSO();
        ClusterParticle particle2 = new ClusterParticle();
        CentroidHolder otherHolder2 = new CentroidHolder();
        otherHolder2.add(ClusterCentroid.of(3,2,3,4,5));
        otherHolder2.add(ClusterCentroid.of(5,10,3,7,1));
        particle2.setPosition(holder);
        particle2.put(Property.FITNESS, new MinimisationFitness(2.1));
        particle2.put(Property.BEST_FITNESS, new MinimisationFitness(2.1));
        particle2.put(Property.BEST_POSITION, particle2.getPosition());
        particle2.setNeighbourhoodBest(particle2);
//        standard2.getTopology().add(particle2);

        ClusterParticle otherParticle2 = new ClusterParticle();
        CentroidHolder holder2 = new CentroidHolder();
        holder2.add(ClusterCentroid.of(9,7,2,9,10));
        holder2.add(ClusterCentroid.of(11,9,5,7,6));
        otherParticle2.setPosition(holder2);
        otherParticle2.put(Property.FITNESS, new MinimisationFitness(3.0));
        otherParticle2.put(Property.BEST_FITNESS, new MinimisationFitness(3.0));
        otherParticle2.put(Property.BEST_POSITION, otherParticle.getPosition());
        otherParticle2.setNeighbourhoodBest(otherParticle2);
//        standard2.getTopology().add(otherParticle2);

        standard2.setTopology(fj.data.List.list(particle2, otherParticle2));

        CooperativePSO cooperative = new CooperativePSO();
        cooperative.addPopulationBasedAlgorithm(standard);
        cooperative.addPopulationBasedAlgorithm(standard2);

        ArrayList<OptimisationSolution> list = (ArrayList<OptimisationSolution>) cooperative.getSolutions();
        ArrayList<CentroidHolder> holders = new ArrayList<CentroidHolder>();
        for(OptimisationSolution solution : list) {
            holders.add((CentroidHolder) solution.getPosition());
        }

        Assert.assertTrue(!list.isEmpty());
        boolean contains = false;
        for(CentroidHolder centroidHolder : holders) {
            if(centroidHolder.containsAll(otherParticle.getPosition())){
                contains = true;
            }
        }

        Assert.assertTrue(contains);

        contains = false;
        for(CentroidHolder centroidHolder : holders) {
            if(centroidHolder.containsAll(particle2.getPosition())){
                contains = true;
            }
        }

        Assert.assertTrue(contains);

    }


    /**
     * Test of performInitialisation method, of class CooperativePSO.
     */
    @Test
    public void testPerformInitialisation() {
        DataClusteringPSO standard = new DataClusteringPSO();
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setWindow(window);
        problem.setDomain("R(-5.12:5.12)");
        standard.setOptimisationProblem(problem);
        standard.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 1));
        PopulationInitialisationStrategy init = new DataDependantPopulationInitialisationStrategy();
        init.setEntityType(new ClusterParticle());
        standard.setInitialisationStrategy(init);
        standard.performInitialisation();

        CooperativePSO instance = new CooperativePSO();
        instance.addPopulationBasedAlgorithm(standard);

        Assert.assertTrue(((ClusteringProblem)((DataClusteringPSO) instance.getPopulations().get(0)).getOptimisationProblem()).getWindow().getCurrentDataset().size() > 0);
        Assert.assertTrue(!instance.getPopulations().isEmpty());
        Assert.assertTrue(!instance.getPopulations().get(0).getTopology().isEmpty());
    }

    /**
     * Test of setIterationStrategy method, of class CooperativePSO.
     */
    @Test
    public void testSetIterationStrategy() {
       CooperativePSO cooperative = new CooperativePSO();
       IterationStrategy strategy = new StandardDataClusteringIterationStrategy();

       cooperative.setIterationStrategy(strategy);

       Assert.assertEquals(strategy, cooperative.getIterationStrategy());
    }

    /**
     * Test of getIterationStrategy method, of class CooperativePSO.
     */
    @Test
    public void testGetIterationStrategy() {
       CooperativePSO cooperative = new CooperativePSO();
       IterationStrategy strategy = new StandardDataClusteringIterationStrategy();

       cooperative.setIterationStrategy(strategy);

       Assert.assertEquals(strategy, cooperative.getIterationStrategy());
    }
}
