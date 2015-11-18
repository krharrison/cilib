/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.nn.architecture.visitors;

import cilib.nn.NeuralNetwork;
import cilib.nn.architecture.Layer;
import cilib.nn.architecture.builder.LayerConfiguration;
import cilib.nn.components.Neuron;
import cilib.nn.domain.PresetNeuronDomain;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;
import cilib.type.StringBasedDomainRegistry;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class WeightSettingVisitorTest {

    @Test
    public void testVisit() {
        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5, true));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3, true));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        StringBasedDomainRegistry domain = new StringBasedDomainRegistry();
        domain.setDomainString("R(-3:3)");
        PresetNeuronDomain domainProvider = new PresetNeuronDomain();
        domainProvider.setWeightDomainPrototype(domain);
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        network.initialise();

        Vector.Builder expectedWeightsBuilder = Vector.newBuilder();
        for (int i = 0; i < network.getWeights().size(); i++) {
            expectedWeightsBuilder.add(Real.valueOf(Math.random()));
        }
        Vector expectedWeights = expectedWeightsBuilder.build();

        WeightSettingVisitor visitor = new WeightSettingVisitor(expectedWeights);
        visitor.visit(network.getArchitecture());

        Vector.Builder actualWeightsBuilder = Vector.newBuilder();
        int numLayers = network.getArchitecture().getNumLayers();
        for (int i = 1; i < numLayers; i++) {
            Layer layer = network.getArchitecture().getLayers().get(i);
            for (Neuron neuron : layer) {
                actualWeightsBuilder.copyOf(neuron.getWeights());
            }
        }

        Assert.assertEquals(expectedWeights, actualWeightsBuilder.build());
    }
}
