/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.decorators;

import cilib.functions.continuous.unconstrained.Beale;
import cilib.type.types.container.Vector;
import cilib.math.random.generator.Rand;
import org.junit.Test;
import static org.junit.Assert.*;

public class PermutationFunctionDecoratorTest {

    @Test
    public void testApply() {
        Beale s = new Beale();
        PermutationFunctionDecorator p = new PermutationFunctionDecorator();
        p.setFunction(s);

        Vector v = Vector.of(1.0, 2.0);
        assertEquals(s.f(v), 126.453125, 0.0);

        // permute input vector, i.e (1.0, 2.0) -> (2.0, 1.0)
        Rand.setSeed(3);
        assertEquals(p.f(v), 14.203125, 0.0);
    }
}
