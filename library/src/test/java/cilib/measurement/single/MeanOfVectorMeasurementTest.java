/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.math.Maths;
import cilib.measurement.Measurement;
import cilib.type.types.container.Vector;
import cilib.type.types.Real;

import org.junit.Test;
import static org.mockito.Mockito.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MeanOfVectorMeasurementTest {

    @Test
    public void testMean() {

        Algorithm algorithm = mock(Algorithm.class);

        Measurement innerMeasurement = mock(Measurement.class);
        when(innerMeasurement.getValue(any(Algorithm.class))).thenReturn(Vector.of(1.0, 2.0, 3.0, 4.0, 5.0));

        MeanOfVectorMeasurement measurement = new MeanOfVectorMeasurement();
        measurement.setMeasurement(innerMeasurement);
        assertEquals(3.0, ((Real) measurement.getValue(algorithm)).doubleValue(), Maths.EPSILON);
    }
}
