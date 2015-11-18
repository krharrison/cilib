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

public class Bukin4Test {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Bukin4();
    }

    /**
     * Test of evaluate method, of class {@link Bukin4}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(1,2);
        assertEquals(400.11, function.f(x), 0.0);

        x.setReal(0, -10.0);
        x.setReal(1, 0.0);
        assertEquals(0.0, function.f(x), 0.0);
    }

    /**
     * Test argument with invalid dimension.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.f(Vector.of(1.0, 2.0, 3.0));
    }
}
