/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.nn.components;

import cilib.io.pattern.StandardPattern;

import cilib.math.Maths;
import cilib.nn.architecture.NeuralInputSource;
import cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class PatternInputSourceTest {

    private StandardPattern standardPattern;

    @Before
    public void setup() {
        Vector vector = Vector.of(0.1, 0.2, 0.3, 0.4, 0.5);
        standardPattern = new StandardPattern(vector, vector);
    }

    @Test
    public void testGetNeuralInput() {
        NeuralInputSource source = new PatternInputSource(standardPattern);
        for (int i = 0; i < standardPattern.getVector().size(); i++) {
            Assert.assertEquals(standardPattern.getVector().doubleValueOf(i), source.getNeuralInput(i), Maths.EPSILON);
        }
    }
}
