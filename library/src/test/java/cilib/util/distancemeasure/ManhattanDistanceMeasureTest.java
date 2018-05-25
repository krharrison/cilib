/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.util.distancemeasure;

import cilib.type.types.Real;
import cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ManhattanDistanceMeasureTest {

    @Test(expected = IllegalArgumentException.class)
    public void testVectorDistance() {
        DistanceMeasure distanceMeasure = new ManhattanDistanceMeasure();

        Vector v1 = Vector.of(4,3,2);
        Vector v2 = Vector.of(2,3,4);
        assertEquals(4.0, distanceMeasure.distance(v1, v2), Double.MIN_NORMAL);

        v1 = Vector.newBuilder().copyOf(v1).add(Real.valueOf(22.0)).build();

        distanceMeasure.distance(v1, v2);
    }


//    @Test(expected = IllegalArgumentException.class)
//    public void testCollectionDistance() {
//        DistanceMeasure distanceMeasure = new ManhattanDistanceMeasure();
//
//        List<Double> l1 = new ArrayList<Double>();
//        List<Double> l2 = new ArrayList<Double>();
//
//        l1.add(4.0);
//        l1.add(3.0);
//        l1.add(2.0);
//
//        l2.add(2.0);
//        l2.add(3.0);
//        l2.add(4.0);
//
//        assertEquals(4.0, distanceMeasure.distance(l1, l2), Double.MIN_NORMAL);
//
//        l1.add(11.0);
//
//        distanceMeasure.distance(l1, l2);
//    }
//
//    @Test
//    public void testSingleDimension() {
//        DistanceMeasure distanceMeasure = new ManhattanDistanceMeasure();
//
//        List<Double> list1 = new ArrayList<Double>(1);
//        List<Double> list2 = new ArrayList<Double>(1);
//
//        list1.add(0.0);
//        list2.add(1.0);
//
//        assertEquals(1.0, distanceMeasure.distance(list1, list2), Double.MIN_NORMAL);
//    }
}