/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.type.parser;

import static org.junit.Assert.assertTrue;
import cilib.type.types.Bit;
import cilib.type.types.Bounds;
import cilib.type.types.Type;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class BTest {

    private static B creator = null;

    @BeforeClass
    public static void setUp() {
        creator = new B();
    }

    @Test
    public void testCreateNoBounds() {
        Type b = creator.create();

        assertTrue(b instanceof Bit);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testCreateBounds() {
        Type b = creator.create(new Bounds(0, 3));
    }

}
