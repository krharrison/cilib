/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.decorators;

import cilib.functions.continuous.unconstrained.Spherical;
import cilib.math.Maths;
import cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ExpandedFunctionDecoratorTest {

    @Test
    public void testEvaluate() {
        ExpandedFunctionDecorator expander = new ExpandedFunctionDecorator();
        Spherical function = new Spherical();

        expander.setFunction(function);

        assertEquals(0.0, expander.f(Vector.of(0.0, 0.0)), Maths.EPSILON);
        assertEquals(4.0, expander.f(Vector.of(1.0, 1.0)), Maths.EPSILON);
        assertEquals(28.0, expander.f(Vector.of(1.0, 2.0, 3.0)), Maths.EPSILON);
    }

    @Test(expected = IllegalStateException.class)
    public void testError() {
        ExpandedFunctionDecorator expander = new ExpandedFunctionDecorator();
        Spherical function = new Spherical();

        expander.setFunction(function);
        expander.setSplitSize(3);

        assertEquals(0.0, expander.f(Vector.of(0.0, 0.0)), Maths.EPSILON);
    }
}
