/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.unconstrained;

import cilib.functions.ContinuousFunction;
import cilib.type.types.container.Vector;
import cilib.math.Maths;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class BirdTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Bird();
    }

    /**
     * Test of evaluate method, of class {@link Bird}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(1,2);
        assertEquals(6.8250541015507, function.f(x), Maths.EPSILON);
    }

    /**
     * Test argument with invalid dimension.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidEvaluate() {
        function.f(Vector.of(1.0, 2.0, 3.0));
    }
}
