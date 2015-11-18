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

public class Shekel2Test {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Shekel2();
    }

    /**
     * Test of evaluate method, of class {@link Shekel2}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(0.0, 0.0);

        assertEquals(12.6705058, function.f(x), 0.0000001);

        x.setReal(0, -32.0);
        x.setReal(1, -32.0);
        assertEquals(0.99800, function.f(x), 0.00001);
    }

    /**
     * Test argument with invalid dimension.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.f(Vector.of(1.0, 2.0, 3.0));
    }
}