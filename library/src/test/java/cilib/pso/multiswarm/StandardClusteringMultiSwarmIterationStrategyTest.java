/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.multiswarm;

import cilib.algorithm.initialisation.DataDependantPopulationInitialisationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.clustering.DataClusteringPSO;
import cilib.clustering.SlidingWindow;
import cilib.clustering.entity.ClusterParticle;
import cilib.measurement.generic.Iterations;
import cilib.problem.QuantisationErrorMinimisationProblem;
import cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import cilib.problem.boundaryconstraint.RandomBoundaryConstraint;
import cilib.pso.behaviour.StandardParticleBehaviour;
import cilib.pso.particle.Particle;
import cilib.pso.velocityprovider.VelocityProvider;
import cilib.stoppingcondition.Maximum;
import cilib.stoppingcondition.MeasuredStoppingCondition;
import cilib.type.types.container.CentroidHolder;
import cilib.type.types.container.ClusterCentroid;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StandardClusteringMultiSwarmIterationStrategyTest {

    /**
     * Test of getExclusionRadius method, of class StandardClusteringMultiSwarmIterationStrategy.
     */
    @Test
    public void testGetExclusionRadius() {
        StandardClusteringMultiSwarmIterationStrategy instance = new StandardClusteringMultiSwarmIterationStrategy();
        instance.setExclusionRadius(5.2);

        assertEquals(5.2, instance.getExclusionRadius(), 0.0000001);
    }

    /**
     * Test of setExclusionRadius method, of class StandardClusteringMultiSwarmIterationStrategy.
     */
    @Test
    public void testSetExclusionRadius() {
        StandardClusteringMultiSwarmIterationStrategy instance = new StandardClusteringMultiSwarmIterationStrategy();
        instance.setExclusionRadius(5.2);

        assertEquals(5.2, instance.getExclusionRadius(), 0.0000001);
    }

    /**
     * Test of calculateRadius method, of class StandardClusteringMultiSwarmIterationStrategy.
     */
    @Test
    public void testCalculateRadius() {
        MultiSwarm multiswarm = new MultiSwarm();
        multiswarm.addPopulationBasedAlgorithm(new DataClusteringPSO());
        multiswarm.addPopulationBasedAlgorithm(new DataClusteringPSO());

        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setWindow(window);
        problem.setDomain("R(-10:10)");

        multiswarm.setOptimisationProblem(problem);

        StandardClusteringMultiSwarmIterationStrategy msStrategy = new StandardClusteringMultiSwarmIterationStrategy();
        double result = msStrategy.calculateRadius(multiswarm);

        assertEquals(8.4089642, result, 0.00001);
    }

    /**
     * Test of isConverged method, of class StandardClusteringMultiSwarmIterationStrategy.
     */
    @Test
    public void testIsConverged() {
        MultiSwarm multiswarm = new MultiSwarm();
        DataClusteringPSO pso = new DataClusteringPSO();


        CentroidHolder candidateSolution = new CentroidHolder();
        candidateSolution.add(ClusterCentroid.of(1,2,3,4));
        CentroidHolder candidateSolution2 = new CentroidHolder();
        candidateSolution2.add(ClusterCentroid.of(1,2,5,2));

        ClusterParticle particle = new ClusterParticle();
        particle.setPosition(candidateSolution);
        ClusterParticle particle2 = new ClusterParticle();
        particle2.setPosition(candidateSolution2);

        pso.setTopology(fj.data.List.list(particle, particle2));

        DataClusteringPSO pso2 = new DataClusteringPSO();

        CentroidHolder candidateSolution12 = new CentroidHolder();
        candidateSolution12.add(ClusterCentroid.of(5,8,7,9));
        CentroidHolder candidateSolution22 = new CentroidHolder();
        candidateSolution22.add(ClusterCentroid.of(20,12,4,6));

        ClusterParticle particle12 = new ClusterParticle();
        particle12.setPosition(candidateSolution12);
        ClusterParticle particle22 = new ClusterParticle();
        particle22.setPosition(candidateSolution22);

        pso2.setTopology(fj.data.List.list(particle12, particle22));

        multiswarm.addPopulationBasedAlgorithm(pso);
        multiswarm.addPopulationBasedAlgorithm(pso2);

        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setWindow(window);
        problem.setDomain("R(-10:10)");

        multiswarm.setOptimisationProblem(problem);

        StandardClusteringMultiSwarmIterationStrategy msStrategy = new StandardClusteringMultiSwarmIterationStrategy();
        boolean firstPSO = msStrategy.isConverged(pso, multiswarm);
        boolean secondPSO = msStrategy.isConverged(pso2, multiswarm);

        assertTrue(firstPSO);
        assertFalse(secondPSO);
    }

    /**
     * Test of performIteration method, of class StandardClusteringMultiSwarmIterationStrategy.
     */
    @Test
    public void testPerformIteration() {
        DataClusteringPSO instance = new DataClusteringPSO();

        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setWindow(window);
        problem.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy = new StandardClusteringMultiSwarmIterationStrategy();
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

        MultiSwarm ms = new MultiSwarm();
        ms.setMultiSwarmIterationStrategy(strategy);
        ms.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 30));
        ms.addPopulationBasedAlgorithm(instance);
        ms.setOptimisationProblem(problem);

        ms.performInitialisation();

        ClusterParticle particleBefore = instance.getTopology().head().getClone();

        ms.run();

        ClusterParticle particleAfter = instance.getTopology().head().getClone();

        Assert.assertFalse(particleAfter.getPosition().containsAll(particleBefore.getPosition()));
    }

    /**
     * Test of reInitialise method, of class StandardClusteringMultiSwarmIterationStrategy.
     */
    @Test
    public void testReInitialise() {
        DataClusteringPSO instance = new DataClusteringPSO();

        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setWindow(window);
        problem.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy = new StandardClusteringMultiSwarmIterationStrategy();
        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new RandomBoundaryConstraint());
        strategy.setBoundaryConstraint(constraint);
        DataDependantPopulationInitialisationStrategy init = new DataDependantPopulationInitialisationStrategy();

        CentroidHolder holder = new CentroidHolder();
        holder.add(ClusterCentroid.of(0.0, 0.0, 0.0, 0.0));

        VelocityProvider vProvider = mock(VelocityProvider.class);
        when(vProvider.get(any(Particle.class))).thenReturn(holder);
        when(vProvider.getClone()).thenReturn(vProvider);

        ClusterParticle particle = new ClusterParticle();
        ((StandardParticleBehaviour) particle.getBehaviour()).setVelocityProvider(vProvider);
        init.setEntityType(particle);
        init.setEntityNumber(2);
        instance.setInitialisationStrategy(init);

        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition());

        MultiSwarm ms = new MultiSwarm();
        ms.addPopulationBasedAlgorithm(instance);
        ms.setOptimisationProblem(problem);

        ms.performInitialisation();

        ClusterParticle particleBefore1 = instance.getTopology().index(0).getClone();
        ClusterParticle particleBefore2 = instance.getTopology().index(1).getClone();

        StandardClusteringMultiSwarmIterationStrategy msStrategy = new StandardClusteringMultiSwarmIterationStrategy();
        msStrategy.setExclusionRadius(Double.POSITIVE_INFINITY);
        ms.setMultiSwarmIterationStrategy((IterationStrategy) msStrategy);

        ms.performIteration();

        ClusterParticle particleAfter1 = instance.getTopology().index(0).getClone();
        ClusterParticle particleAfter2 = instance.getTopology().index(1).getClone();

        assertFalse(particleAfter1.getPosition().containsAll(particleBefore1.getPosition()));
        assertFalse(particleAfter2.getPosition().containsAll(particleBefore2.getPosition()));
    }

}
