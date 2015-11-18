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

public class NeuronCountTest {

    @Test
    public void theTest() {
        NeuronCount measurement1 = new NeuronCount();
        NeuronCount measurement2 = new NeuronCount();
        measurement2.setIncludeBias(false);

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

        assertEquals(20, measurement1.getValue(algorithm).intValue());
        assertEquals(17, measurement2.getValue(algorithm).intValue());

        NeuralNetwork network2 = new NeuralNetwork();
        network2.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network2.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network2.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network2.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network2.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        network2.initialise();
        problem.setNeuralNetwork(network2);

        assertEquals(8, measurement1.getValue(algorithm).intValue());
        assertEquals(7, measurement2.getValue(algorithm).intValue());
    }
}
