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

public class RotatedFunctionDecoratorTest {

    @Test
    public void testApply() {
        Spherical s = new Spherical();
        RotatedFunctionDecorator r = new RotatedFunctionDecorator();
        r.setFunction(s);

        Vector v = Vector.of(0.0, 0.0, 0.0);

        // rotation matrix should not change function minimum
        assertEquals(0.0, r.f(v), 0.0);

        // identity matrix should not change function
        v = Vector.of(1.0, 2.0, 3.0);
        r.setMatrixType("identity");
        r.setRotationMatrix(v.size());
        assertEquals(s.f(v), r.f(v), 0.0);
    }
}
