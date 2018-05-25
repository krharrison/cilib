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

public class GriewankTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Griewank();
    }

    /**
     * Test of evaluate method, of class {@link Griewank}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(0,0);
        assertEquals(0.0, function.f(x), 0.0);

        x.setReal(0, Math.PI / 2);
        x.setReal(1, Math.PI / 2);
        assertEquals(1.0012337, function.f(x), 0.0000001);
    }
}