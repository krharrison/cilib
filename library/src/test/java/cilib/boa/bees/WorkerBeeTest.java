/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.boa.bees;

import static org.junit.Assert.assertEquals;
import cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import cilib.boa.bee.WorkerBee;
import cilib.controlparameter.ConstantControlParameter;
import cilib.functions.ContinuousFunction;
import cilib.functions.continuous.unconstrained.Rastrigin;
import cilib.problem.FunctionOptimisationProblem;

import org.junit.Test;

public class WorkerBeeTest {

    @Test
    public void testSetForageLimit() {
        FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
        problem.setDomain("R(-10.048:10.048)^100");
        ContinuousFunction func = new Rastrigin();
        problem.setFunction(func);

        ClonedPopulationInitialisationStrategy initStrategy = new ClonedPopulationInitialisationStrategy();
        initStrategy.setEntityNumber(100);
        WorkerBee clone = new WorkerBee();
        clone.setForageLimit(ConstantControlParameter.of(680));
        initStrategy.setEntityType(clone);
        fj.data.List<WorkerBee> population = fj.data.List.iterableList(initStrategy.<WorkerBee>initialise(problem));

        for (WorkerBee bee : population) {
            assertEquals(bee.getForageLimit().getParameter(), 680.0, 0.0001);
        }
    }

}
