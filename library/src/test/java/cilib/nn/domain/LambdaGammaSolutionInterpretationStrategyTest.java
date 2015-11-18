/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.nn.domain;

import cilib.functions.activation.Sigmoid;
import cilib.math.Maths;
import cilib.nn.NeuralNetwork;
import cilib.nn.NeuralNetworksTestHelper;
import cilib.nn.architecture.Layer;
import cilib.nn.architecture.visitors.ArchitectureVisitor;
import cilib.nn.components.BiasNeuron;
import cilib.nn.components.Neuron;
import cilib.type.types.container.Vector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LambdaGammaSolutionInterpretationStrategyTest {

    private NeuralNetwork neuralNetwork;

    @Before
    public void setup() {
        neuralNetwork = NeuralNetworksTestHelper.createFFNN(3, 2, 1);
    }

    @Test
    public void shouldCreateVisitor() {
        Vector solution = Vector.of(1.1, 1.2, 1.3, 1.4, 0.1, 0.4, 1.5, 1.6, 1.7, 1.8, 0.2, 0.5, 1.9, 2.0, 2.1, 0.3, 0.6);

        LambdaGammaSolutionConversionStrategy lambdaGammaSolutionInterpretationStrategy = new LambdaGammaSolutionConversionStrategy();
        ArchitectureVisitor lambdaGammaVisitor = lambdaGammaSolutionInterpretationStrategy.interpretSolution(solution);
        Vector weights = Vector.of(1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0, 2.1);
        Vector lambdas = Vector.of(0.1, 0.2, 0.3);
        Vector gammas = Vector.of(0.4, 0.5, 0.6);

        lambdaGammaVisitor.visit(neuralNetwork.getArchitecture());

        int lambdaIdx = 0;
        int gammaIdx = 0;
        int weightIdx = 0;

        for (Layer layer : neuralNetwork.getArchitecture().getActivationLayers()) {
            for (Neuron neuron : layer) {
                if (!(neuron instanceof BiasNeuron)) {
                    assertEquals(lambdas.get(lambdaIdx++).doubleValue(), ((Sigmoid) neuron.getActivationFunction()).getLambda().getParameter(), Maths.EPSILON);
                    assertEquals(gammas.get(gammaIdx++).doubleValue(), ((Sigmoid) neuron.getActivationFunction()).getGamma().getParameter(), Maths.EPSILON);
                }
                Vector neuronWeights = neuron.getWeights();
                int size = neuronWeights.size();
                for (int j = 0; j < size; j++) {
                    assertEquals(weights.get(weightIdx++), neuronWeights.get(j));
                }
            }
        }
    }
}
