/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.nn.architecture.visitors;

import cilib.io.pattern.StandardPattern;
import cilib.math.Maths;
import cilib.nn.FFNNTopology;
import cilib.nn.NeuralNetwork;
import cilib.nn.architecture.builder.LayerConfiguration;
import cilib.nn.components.PatternInputSource;
import cilib.nn.domain.PresetNeuronDomain;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;
import cilib.type.StringBasedDomainRegistry;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class FeedForwardVisitorTest {

    @Test
    public void testVisit() {
        Vector vector = Vector.of(0.1, 0.2, 0.3, 0.4, 0.5);
        StandardPattern pattern = new StandardPattern(vector,vector);
        
        FFNNTopology topology = new FFNNTopology(5, 3, 1, 0.0, 0.0);

        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5,true));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3,true));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        StringBasedDomainRegistry domain = new StringBasedDomainRegistry();
        domain.setDomainString("R(-3:3)");
        PresetNeuronDomain domainProvider = new PresetNeuronDomain();
        domainProvider.setWeightDomainPrototype(domain);
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        network.initialise();

        network.setWeights(topology.getWeights());

        FeedForwardVisitor feedForwardVisitor = new FeedForwardVisitor();
        feedForwardVisitor.setInput(pattern);
        feedForwardVisitor.visit(network.getArchitecture());

        Assert.assertEquals(((Real)topology.evaluate(new PatternInputSource(pattern)).get(0)).doubleValue(),
                            feedForwardVisitor.getOutput().get(0).doubleValue(),
                            Maths.EPSILON);
    }

}
