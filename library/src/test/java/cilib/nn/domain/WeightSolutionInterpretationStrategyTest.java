/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.nn.domain;

import cilib.nn.architecture.visitors.ArchitectureVisitor;
import cilib.nn.architecture.visitors.WeightSettingVisitor;
import cilib.type.types.container.Vector;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WeightSolutionInterpretationStrategyTest {

    @Test
    public void shouldInterpretSolution() {
        Vector vector = Vector.of(2.0, -2.0, 5.0, 1.0, 5.0);
        final ArchitectureVisitor architectureVisitor = new WeightSolutionConversionStrategy().interpretSolution(vector);
        final Vector weights = ((WeightSettingVisitor) architectureVisitor).getWeights();
        assertEquals(vector, weights);
    }
}
