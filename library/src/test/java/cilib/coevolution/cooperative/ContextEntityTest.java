/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.coevolution.cooperative;

import cilib.coevolution.cooperative.problem.SequentialDimensionAllocation;
import cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ContextEntityTest {
    @Test
    public void CopyFromTest(){
        ContextEntity testEntity = new ContextEntity();
        Vector context = Vector.of(0.0, 0.0, 0.0, 0.0);
        testEntity.setPosition(context);
        Vector data = Vector.of(1, 2);

        SequentialDimensionAllocation allocation = new SequentialDimensionAllocation(1, 2);

        testEntity.copyFrom(data, allocation);
        context = testEntity.getPosition();

        assertEquals(0.0, context.get(0).doubleValue(),0);
        assertEquals(1.0, context.get(1).doubleValue(),0);
        assertEquals(2.0, context.get(2).doubleValue(),0);
        assertEquals(0.0, context.get(3).doubleValue(),0);
    }
}
