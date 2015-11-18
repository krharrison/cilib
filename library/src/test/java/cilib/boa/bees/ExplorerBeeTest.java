/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.boa.bees;

import cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import cilib.boa.ABC;
import cilib.boa.bee.ExplorerBee;
import cilib.boa.bee.WorkerBee;
import cilib.controlparameter.ConstantControlParameter;
import cilib.functions.continuous.unconstrained.Rastrigin;
import cilib.measurement.generic.Iterations;
import cilib.problem.FunctionOptimisationProblem;
import cilib.stoppingcondition.Maximum;
import cilib.stoppingcondition.MeasuredStoppingCondition;
import cilib.stoppingcondition.StoppingCondition;
import cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExplorerBeeTest {

    private ABC abc;

    @Before
    public void setUp() throws Exception {
        FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
        problem.setDomain("R(-5.0:5.0)^5");
        problem.setFunction(new Rastrigin());

        StoppingCondition condition = new MeasuredStoppingCondition(new Iterations(), new Maximum(), 2);

        abc = new ABC();
        ClonedPopulationInitialisationStrategy initStrategy = new ClonedPopulationInitialisationStrategy();
        initStrategy.setEntityNumber(10);
        WorkerBee bee = new WorkerBee();
        initStrategy.setEntityType(bee);
        abc.setForageLimit(ConstantControlParameter.of(-1));
        abc.setInitialisationStrategy(initStrategy);
        abc.setWorkerBeePercentage(ConstantControlParameter.of(0.5));
        abc.addStoppingCondition(condition);
        abc.setOptimisationProblem(problem);
        abc.performInitialisation();
    }

    @Test
    public void testSearchAllowed() {
        //get up a position with bounds
        Vector oldPosition = Vector.copyOf((Vector) abc.getWorkerBees().head().getPosition());

        //update position with explorer bee
        ExplorerBee explorerBee = abc.getExplorerBee();
        Assert.assertTrue(explorerBee.searchAllowed(1));
        explorerBee.getNewPosition(1, oldPosition);
        //only one update is allowed for the same iteration, this must therefore be false...
        Assert.assertTrue(!explorerBee.searchAllowed(1));
        //and this true.
        Assert.assertTrue(explorerBee.searchAllowed(2));
    }

    @Test
    public void testGetNewPosition() {
        //get up a position with bounds
        Vector oldPosition = Vector.copyOf((Vector) abc.getWorkerBees().head().getPosition());
        //update position with explorer bee
        ExplorerBee explorerBee = abc.getExplorerBee();
        Vector newPosition = explorerBee.getNewPosition(1, oldPosition);
        Assert.assertTrue(!oldPosition.equals(newPosition));
    }
}
