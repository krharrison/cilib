/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.activation;

import cilib.math.Maths;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the linear activation function;
 */
public class LinearTest {

    @Test
    public void evaluate() {
        Linear linear = new Linear();
        Assert.assertEquals(0.0, linear.f(0.0), Maths.EPSILON);
        Assert.assertEquals(0.0, linear.f(Real.valueOf(0.0)).doubleValue(), Maths.EPSILON);

        Assert.assertEquals(0.5, linear.f(0.5), Maths.EPSILON);
        Assert.assertEquals(0.5, linear.f(Real.valueOf(0.5)).doubleValue(), Maths.EPSILON);

        Assert.assertEquals(Double.MAX_VALUE, linear.f(Double.MAX_VALUE), Maths.EPSILON);
        Assert.assertEquals(Double.MIN_VALUE, linear.f(Real.valueOf(Double.MIN_VALUE)).doubleValue(), Maths.EPSILON);
        Assert.assertEquals(-Double.MIN_VALUE, linear.f(Real.valueOf(-Double.MIN_VALUE)).doubleValue(), Maths.EPSILON);
        Assert.assertEquals(-Double.MAX_VALUE, linear.f(Real.valueOf(-Double.MAX_VALUE)).doubleValue(), Maths.EPSILON);
    }

    @Test
    public void gradient() {
        Linear linear = new Linear();
        Assert.assertEquals(1.0, linear.getGradient(0.0), Maths.EPSILON);
        Assert.assertEquals(1.0, linear.getGradient(Vector.of(0.0)).get(0).doubleValue(), Maths.EPSILON);

        Assert.assertEquals(1.0, linear.getGradient(0.5), Maths.EPSILON);
        Assert.assertEquals(1.0, linear.getGradient(Vector.of(0.5)).get(0).doubleValue(), Maths.EPSILON);

        Assert.assertEquals(1.0, linear.getGradient(Double.MAX_VALUE), Maths.EPSILON);
        Assert.assertEquals(1.0, linear.getGradient(Double.MIN_VALUE), Maths.EPSILON);
        Assert.assertEquals(1.0, linear.getGradient(-Double.MIN_VALUE), Maths.EPSILON);
        Assert.assertEquals(1.0, linear.getGradient(-Double.MAX_VALUE), Maths.EPSILON);
    }

    @Test
    public void activeRange() {
        Linear linear = new Linear();
        Assert.assertEquals(Double.MAX_VALUE, linear.getUpperActiveRange(), Maths.EPSILON);
        Assert.assertEquals(-Double.MAX_VALUE, linear.getLowerActiveRange(), Maths.EPSILON);
    }
}
