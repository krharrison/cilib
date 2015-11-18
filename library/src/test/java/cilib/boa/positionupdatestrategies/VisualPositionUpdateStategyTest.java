/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.boa.positionupdatestrategies;

import cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import cilib.boa.ABC;
import cilib.boa.bee.HoneyBee;
import cilib.boa.bee.WorkerBee;
import cilib.controlparameter.ConstantControlParameter;
import cilib.functions.continuous.unconstrained.Rastrigin;
import cilib.measurement.generic.Iterations;
import cilib.problem.solution.Fitness;
import cilib.problem.FunctionOptimisationProblem;
import cilib.stoppingcondition.Maximum;
import cilib.stoppingcondition.MeasuredStoppingCondition;
import cilib.stoppingcondition.StoppingCondition;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VisualPositionUpdateStategyTest {

    private ABC abc;

    @Before
    public void setUp() throws Exception {
        FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
        problem.setFunction(new Rastrigin());
        problem.setDomain("R(-5.0:5.0)^10");

        StoppingCondition condition = new MeasuredStoppingCondition(new Iterations(), new Maximum(), 2);

        abc = new ABC();
        ClonedPopulationInitialisationStrategy initStrategy = new ClonedPopulationInitialisationStrategy();
        initStrategy.setEntityNumber(10);
        WorkerBee bee = new WorkerBee();
        initStrategy.setEntityType(bee);
        abc.setInitialisationStrategy(initStrategy);
        abc.setWorkerBeePercentage(ConstantControlParameter.of(0.5));
        abc.setForageLimit(ConstantControlParameter.of(Integer.MAX_VALUE));
        abc.addStoppingCondition(condition);
        abc.setOptimisationProblem(problem);
        abc.performInitialisation();
    }

    @Test
    public void testUpdatePosition() {
        HoneyBee bee = abc.getWorkerBees().head();
        abc.performIteration();
        Fitness oldFitness = bee.getFitness().getClone();
        abc.performIteration();
        Vector currentPosition = (Vector) bee.getPosition();
        Assert.assertEquals(10, currentPosition.size());
        for (int i = 0; i < currentPosition.size(); i++) {
            Assert.assertTrue(((Real) currentPosition.get(i)).doubleValue() != Double.NaN);
            Assert.assertTrue(!Double.isInfinite(((Real) currentPosition.get(i)).doubleValue()));
        }
        Fitness newFitness = bee.getFitness();
        Assert.assertTrue(newFitness.compareTo(oldFitness) >= 0);
    }
}
