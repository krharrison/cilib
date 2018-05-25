/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.unconstrained;

import cilib.functions.ContinuousFunction;
import cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class BentCigarTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new BentCigar();
    }

    /**
     * Test of evaluate method, of class {@link BentCigar}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(1.0, 2.0, 3.0);

        assertEquals(1 + 13E6, function.f(x), 0.0);

        x.setReal(0, 0.0);
        x.setReal(1, 0.0);
        x.setReal(2, 0.0);
        assertEquals(0.0, function.f(x), 0.0);
    }
}