/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.discrete;

import cilib.math.Maths;
import cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class OnemaxTest {

    private final Onemax func = new Onemax();

    @Test
    public void testFunction() {
        assertEquals(0.0, func.f(Vector.of(0,0,0)), Maths.EPSILON);
        assertEquals(1.0, func.f(Vector.of(0,0,1)), Maths.EPSILON);
        assertEquals(2.0, func.f(Vector.of(0,1,1)), Maths.EPSILON);
        assertEquals(3.0, func.f(Vector.of(1,1,1)), Maths.EPSILON);

        assertEquals(1.0, func.f(Vector.of(0,1,0)), Maths.EPSILON);
        assertEquals(2.0, func.f(Vector.of(1,0,1)), Maths.EPSILON);
        assertEquals(1.0, func.f(Vector.of(1,0,0)), Maths.EPSILON);
        assertEquals(2.0, func.f(Vector.of(0,1,1)), Maths.EPSILON);
    }

}
