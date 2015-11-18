/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.discrete;

import cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class KnapSackTest {

    @Test
    public void testSimpleKnapSack() {
        KnapSack k = new KnapSack();
        k.setCapacity(5);
        k.setNumberOfObjects(5);
        k.setWeight("1,1,1,1,1");
        k.setValue("1,1,1,1,1");

        Vector.Builder x = Vector.newBuilder();
        x.add(true);
        x.add(true);
        x.add(true);
        x.add(true);
        x.add(true);
        assertEquals(5, Double.valueOf(k.f(x.build())).intValue());
    }
}
