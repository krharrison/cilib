/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.gd;

import cilib.io.ARFFFileReader;
import cilib.measurement.generic.Iterations;
import cilib.nn.architecture.builder.LayerConfiguration;
import cilib.nn.domain.PresetNeuronDomain;
import cilib.problem.nn.NNDataTrainingProblem;
import cilib.stoppingcondition.Maximum;
import cilib.stoppingcondition.MeasuredStoppingCondition;
import cilib.stoppingcondition.StoppingCondition;
import cilib.type.types.container.Vector;
import cilib.type.StringBasedDomainRegistry;
import org.junit.Before;
import org.junit.Test;

/**
 * This test does not compare against anything (the same problem as with unit
 * testing a PSO) but simply executes to see whether there is exceptions.
 */
public class GradientDecentBackpropagationTrainingTest {

    NNDataTrainingProblem problem;

    @Before
    public void setup() {
        problem = new NNDataTrainingProblem();
        problem.getDataTableBuilder().setDataReader(new ARFFFileReader());
        problem.getDataTableBuilder().setSourceURL("library/src/test/resources/datasets/iris.arff");

        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(4));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        StringBasedDomainRegistry domain = new StringBasedDomainRegistry();
        domain.setDomainString("R(-3:3)");
        PresetNeuronDomain domainProvider = new PresetNeuronDomain();
        domainProvider.setWeightDomainPrototype(domain);
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
    }

    @Test
    public void testGradientDecent() {
        GradientDescentBackpropagationTraining training = new GradientDescentBackpropagationTraining();
        training.setOptimisationProblem(problem);
        StoppingCondition stoppingCondition = new MeasuredStoppingCondition(new Iterations(), new Maximum(), 10);
        training.addStoppingCondition(stoppingCondition);
        training.performInitialisation();

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < 10; i++) {
            training.algorithmIteration();
            builder.add(training.getBestSolution().getFitness().getValue());
        }
        
        Vector errors = builder.build();
        // asserts?
    }
}
