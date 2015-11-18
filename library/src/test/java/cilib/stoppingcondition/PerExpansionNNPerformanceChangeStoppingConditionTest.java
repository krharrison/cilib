/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.stoppingcondition;

import cilib.algorithm.Algorithm;
import cilib.functions.activation.Linear;
import cilib.io.pattern.StandardPattern;
import cilib.io.StandardPatternDataTable;
import cilib.nn.architecture.builder.LayerConfiguration;
import cilib.nn.domain.PresetNeuronDomain;
import cilib.nn.NeuralNetwork;
import cilib.problem.solution.MinimisationFitness;
import cilib.problem.nn.NNDataTrainingProblem;
import cilib.problem.solution.OptimisationSolution;
import cilib.type.types.container.Vector;
import cilib.type.types.Real;
import cilib.type.StringBasedDomainRegistry;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class PerExpansionNNPerformanceChangeStoppingConditionTest {

    /**
     * Test of getPercentageCompleted method, of class MaintainedStoppingCondition.
     */
    @Test
    public void test() {
        StandardPatternDataTable validationSet = new StandardPatternDataTable();
        validationSet.addRow(new StandardPattern(Vector.of(-0.9), Real.valueOf(0.81)));
        validationSet.addRow(new StandardPattern(Vector.of(-0.8), Real.valueOf(0.64)));
        validationSet.addRow(new StandardPattern(Vector.of(-0.7), Real.valueOf(0.49)));
        validationSet.addRow(new StandardPattern(Vector.of(-0.6), Real.valueOf(0.36)));
        validationSet.addRow(new StandardPattern(Vector.of(-0.5), Real.valueOf(0.25)));
        validationSet.addRow(new StandardPattern(Vector.of(-0.4), Real.valueOf(0.16)));
        validationSet.addRow(new StandardPattern(Vector.of(-0.3), Real.valueOf(0.09)));
        validationSet.addRow(new StandardPattern(Vector.of(-0.2), Real.valueOf(0.04)));
        validationSet.addRow(new StandardPattern(Vector.of(-0.1), Real.valueOf(0.01)));
        validationSet.addRow(new StandardPattern(Vector.of(0.0), Real.valueOf(0.0)));
        validationSet.addRow(new StandardPattern(Vector.of(0.1), Real.valueOf(0.01)));
        validationSet.addRow(new StandardPattern(Vector.of(0.2), Real.valueOf(0.04)));
        validationSet.addRow(new StandardPattern(Vector.of(0.3), Real.valueOf(0.09)));
        validationSet.addRow(new StandardPattern(Vector.of(0.4), Real.valueOf(0.16)));
        validationSet.addRow(new StandardPattern(Vector.of(0.5), Real.valueOf(0.25)));
        validationSet.addRow(new StandardPattern(Vector.of(0.6), Real.valueOf(0.36)));
        validationSet.addRow(new StandardPattern(Vector.of(0.7), Real.valueOf(0.49)));
        validationSet.addRow(new StandardPattern(Vector.of(0.8), Real.valueOf(0.64)));
        validationSet.addRow(new StandardPattern(Vector.of(0.9), Real.valueOf(0.81)));

        NNDataTrainingProblem problem = new NNDataTrainingProblem();
        problem.setValidationSet(validationSet);

        Algorithm algorithm = Mockito.mock(Algorithm.class);
        Mockito.when(algorithm.getOptimisationProblem()).thenReturn(problem);

        PerExpansionNNPerformanceChangeStoppingCondition instance = new PerExpansionNNPerformanceChangeStoppingCondition();

        NeuralNetwork badNetwork = new NeuralNetwork();
        badNetwork.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        badNetwork.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        badNetwork.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1, new Linear()));
        StringBasedDomainRegistry domain = new StringBasedDomainRegistry();
        domain.setDomainString("R(-3:3)");
        PresetNeuronDomain domainProvider = new PresetNeuronDomain();
        domainProvider.setWeightDomainPrototype(domain);
        badNetwork.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        badNetwork.initialise();

        NeuralNetwork goodNetwork = new NeuralNetwork();
        goodNetwork.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        goodNetwork.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        goodNetwork.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1, new Linear()));
        goodNetwork.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        goodNetwork.initialise();

        NeuralNetwork finalNetwork = new NeuralNetwork();
        finalNetwork.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        finalNetwork.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        finalNetwork.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1, new Linear()));
        finalNetwork.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        finalNetwork.initialise();

        //first evaluation
        problem.setNeuralNetwork(badNetwork);
        Mockito.when(algorithm.getOptimisationProblem()).thenReturn(problem);
        Mockito.when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(Vector.of(0.0, 0.0, 0.0, 5.0), new MinimisationFitness(1.0)));

        assertFalse(instance.f(algorithm));

        Mockito.when(algorithm.getOptimisationProblem()).thenReturn(problem);
        Mockito.when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(Vector.of(1.0, 0.5, 1.0, 0.0), new MinimisationFitness(1.0)));

        assertFalse(instance.f(algorithm));

        Mockito.when(algorithm.getOptimisationProblem()).thenReturn(problem);
        Mockito.when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(Vector.of(1.0, 0.5, 1.0, 0.0), new MinimisationFitness(1.0)));

        assertFalse(instance.f(algorithm));

        //good expansion
        problem.setNeuralNetwork(goodNetwork);
        Mockito.when(algorithm.getOptimisationProblem()).thenReturn(problem);
        Mockito.when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(Vector.of(-8.0, 5.5, 8.0, 5.5, 1.0, 1.0, 0.0), new MinimisationFitness(1.0)));

        assertFalse(instance.f(algorithm));

        Mockito.when(algorithm.getOptimisationProblem()).thenReturn(problem);
        Mockito.when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(Vector.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0), new MinimisationFitness(1.0)));

        assertFalse(instance.f(algorithm));

        Mockito.when(algorithm.getOptimisationProblem()).thenReturn(problem);
        Mockito.when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(Vector.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0), new MinimisationFitness(1.0)));

        assertFalse(instance.f(algorithm));

        //bad expansion
        problem.setNeuralNetwork(finalNetwork);
        Mockito.when(algorithm.getOptimisationProblem()).thenReturn(problem);
        Mockito.when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(Vector.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0), new MinimisationFitness(1.0)));

        assertTrue(instance.f(algorithm));
    }
}
