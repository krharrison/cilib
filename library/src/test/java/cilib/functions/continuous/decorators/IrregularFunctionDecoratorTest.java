/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.decorators;

import cilib.functions.continuous.unconstrained.Spherical;
import cilib.type.types.container.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

public class IrregularFunctionDecoratorTest {

    @Test
    public void testApply() {
        Spherical s = new Spherical();
        IrregularFunctionDecorator i = new IrregularFunctionDecorator();
        i.setFunction(s);

        Vector v = Vector.of(1.0, 2.0);
        assertEquals(4.953, i.f(v), 0.001);

        v.setReal(1, 0.0);
        assertEquals(1.0, i.f(v), 0.0);
    }
}
