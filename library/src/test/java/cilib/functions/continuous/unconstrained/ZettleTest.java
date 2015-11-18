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

public class ZettleTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Zettle();
    }

    /**
     * Test of evaluate method, of class {@link Zettle}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(-0.0299, 0);
        assertEquals(-0.003791, function.f(x), 0.000009);
    }

    /**
     * Test argument with invalid dimension.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.f(Vector.of(1.0, 2.0, 3.0));
    }
}
