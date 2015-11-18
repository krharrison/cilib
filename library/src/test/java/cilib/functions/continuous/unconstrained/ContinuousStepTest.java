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

public class ContinuousStepTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new ContinuousStep();
    }

    /**
     * Test of evaluate method, of class {@link ContinuousStep}.
     */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(5.0, 10.0);

        assertEquals(140.5, function.f(x), Maths.EPSILON);

        x.setReal(0, -0.5);
        x.setReal(1, -0.5);
        assertEquals(0.0, function.f(x), Maths.EPSILON);
    }
}
