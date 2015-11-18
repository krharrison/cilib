/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.nn.architecture.builder.CascadeArchitectureBuilder;
import cilib.nn.architecture.builder.LayerConfiguration;
import cilib.nn.domain.PresetNeuronDomain;
import cilib.nn.NeuralNetwork;
import cilib.problem.nn.NNDataTrainingProblem;
import cilib.type.StringBasedDomainRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NNWeightCountTest {

    @Test
    public void theTest() {
        NNWeightCount measurement1 = new NNWeightCount();
        NNWeightCount measurement2 = new NNWeightCount();
        measurement2.setPenalty(6);

        NNDataTrainingProblem problem = new NNDataTrainingProblem();
        final Algorithm algorithm = mock(Algorithm.class);
        when(algorithm.getOptimisationProblem()).thenReturn(problem);

        NeuralNetwork network1 = new NeuralNetwork();
        network1.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5));
        network1.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network1.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network1.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(6));
        StringBasedDomainRegistry domain = new StringBasedDomainRegistry();
        domain.setDomainString("R(-3:3)");
        PresetNeuronDomain domainProvider = new PresetNeuronDomain();
        domainProvider.setWeightDomainPrototype(domain);
        network1.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        network1.initialise();
        problem.setNeuralNetwork(network1);

        assertEquals(54, measurement1.getValue(algorithm).intValue());
        assertEquals(126, measurement2.getValue(algorithm).intValue());

        NeuralNetwork network2 = new NeuralNetwork();
        network2.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network2.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network2.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network2.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network2.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        network2.initialise();
        problem.setNeuralNetwork(network2);

        assertEquals(21, measurement1.getValue(algorithm).intValue());
        assertEquals(51, measurement2.getValue(algorithm).intValue());
    }
}
