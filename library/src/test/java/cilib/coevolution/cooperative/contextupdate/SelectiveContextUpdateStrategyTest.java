/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.coevolution.cooperative.contextupdate;

import cilib.coevolution.cooperative.ContextEntity;
import cilib.coevolution.cooperative.problem.DimensionAllocation;
import cilib.coevolution.cooperative.problem.SequentialDimensionAllocation;
import cilib.entity.Entity;
import cilib.problem.solution.MinimisationFitness;
import cilib.type.types.container.Vector;
import cilib.util.calculator.FitnessCalculator;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class SelectiveContextUpdateStrategyTest {
     @SuppressWarnings("unchecked")
     @Test
     public void SelectiveUpdateTest(){
         final ContextEntity contextEntity = new ContextEntity();

         final FitnessCalculator<Entity> test = mock(FitnessCalculator.class);

         when(test.getFitness(any(ContextEntity.class))).thenReturn(new MinimisationFitness(1.0), new MinimisationFitness(3.0));
         when(test.getClone()).thenReturn(test);

         Vector testContext = Vector.of(1,1);

         contextEntity.setPosition(testContext);
         contextEntity.getBehaviour().setFitnessCalculator(test);
         Vector solution = Vector.of(0);
         DimensionAllocation allocation = new SequentialDimensionAllocation(0, 1);

         SelectiveContextUpdateStrategy strategy = new SelectiveContextUpdateStrategy();
         strategy.updateContext(contextEntity, solution, allocation);

         assertEquals(0.0, contextEntity.getPosition().get(0).doubleValue(), 0.0);
         assertEquals(1.0, contextEntity.getFitness().getValue(), 0.0);

         Vector otherSolution = Vector.of(3);
         strategy.updateContext(contextEntity, otherSolution, allocation);

         assertEquals(0.0, contextEntity.getPosition().get(0).doubleValue(), 0.0);
         assertEquals(1.0, contextEntity.getFitness().getValue(), 0.0);

         verify(test, atLeast(2)).getFitness(any(ContextEntity.class));
     }
}
