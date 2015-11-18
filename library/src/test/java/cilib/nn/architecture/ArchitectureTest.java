/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.nn.architecture;

import cilib.nn.NeuralNetwork;
import cilib.nn.architecture.builder.LayerConfiguration;
import cilib.nn.architecture.visitors.WeightRetrievalVisitor;
import cilib.nn.domain.PresetNeuronDomain;
import cilib.type.StringBasedDomainRegistry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArchitectureTest {

    private NeuralNetwork network;

    @Before
    public void setup() {
        network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        StringBasedDomainRegistry domain = new StringBasedDomainRegistry();
        domain.setDomainString("R(-3:3)");
        PresetNeuronDomain domainProvider = new PresetNeuronDomain();
        domainProvider.setWeightDomainPrototype(domain);
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        network.initialise();
    }

    @Test
    public void testInitialise() {
        // assert number of layers, it is not necessary to test constructed components
        // as they have their own unit tests.
        Assert.assertEquals(3, network.getArchitecture().getNumLayers());
    }

    @Test
    public void testAccept() {
        // this is mostly to test whether the visit actually occurs and not whether
        // the visitor works.
        WeightRetrievalVisitor visitor = new WeightRetrievalVisitor();
        network.getArchitecture().accept(visitor);
        Assert.assertEquals(22, visitor.getOutput().size());
    }      
}
