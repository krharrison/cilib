/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.entity.initialisation;

import junit.framework.Assert;
import cilib.clustering.SlidingWindow;
import cilib.clustering.entity.ClusterParticle;
import cilib.entity.Property;
import cilib.io.DataTable;
import cilib.type.types.container.CentroidHolder;
import cilib.type.types.container.ClusterCentroid;
import org.junit.Test;

public class DataPatternInitialisationStrategyTest {

    /**
     * Test of initialise method, of class DataPatternInitialisationStrategy.
     */
    @Test
    public void testInitialise() {
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(3);
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        DataTable table = window.getCurrentDataset();

        ClusterParticle particle  = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        holder.add(ClusterCentroid.of(0,0));
        particle.setPosition(holder);

        DataPatternInitialisationStrategy strategy = new DataPatternInitialisationStrategy();
        strategy.setDataset(table);
        strategy.initialise(Property.CANDIDATE_SOLUTION, particle);

        Assert.assertTrue((((CentroidHolder)particle.getPosition()).get(0).containsAll(ClusterCentroid.of(1.0,1.0,1.0,2.0)))
                || (((CentroidHolder)particle.getPosition()).get(0).containsAll(ClusterCentroid.of(2.0,3.0,4.0,2.0)))
                || (((CentroidHolder)particle.getPosition()).get(0).containsAll(ClusterCentroid.of(1.0,1.0,1.0,1.0))));
    }

    /**
     * Test of setDataset method, of class DataPatternInitialisationStrategy.
     */
    @Test
    public void testSetDataset() {
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(3);
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        DataTable table = window.getCurrentDataset();

        DataPatternInitialisationStrategy strategy = new DataPatternInitialisationStrategy();
        strategy.setDataset(table);

        Assert.assertEquals(table, strategy.getDataset());
    }

    /**
     * Test of getDataset method, of class DataPatternInitialisationStrategy.
     */
    @Test
    public void testGetDataset() {
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(3);
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        DataTable table = window.getCurrentDataset();

        DataPatternInitialisationStrategy strategy = new DataPatternInitialisationStrategy();
        strategy.setDataset(table);

        Assert.assertEquals(table, strategy.getDataset());
    }
}
