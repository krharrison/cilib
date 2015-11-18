/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.problem.nn;

import cilib.io.ARFFFileReader;
import cilib.nn.architecture.builder.LayerConfiguration;
import cilib.nn.domain.PresetNeuronDomain;
import cilib.pso.PSO;
import cilib.stoppingcondition.MeasuredStoppingCondition;
import cilib.type.DomainRegistry;
import cilib.type.StringBasedDomainRegistry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test does not compare against anything (the same problem as with unit testing
 * a PSO) but simply executes to see whether there is exceptions.
 */
public class NNDataTrainingProblemTest {

    NNDataTrainingProblem problem;

    @Before
    public void setup() {
        problem = new NNDataTrainingProblem();
        problem.getDataTableBuilder().setDataReader(new ARFFFileReader());
        problem.getDataTableBuilder().setSourceURL("library/src/test/resources/datasets/iris.arff");
        problem.setTrainingSetPercentage(0.5);
        problem.setValidationSetPercentage(0.2);
        problem.setGeneralisationSetPercentage(0.3);

        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(4));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        StringBasedDomainRegistry domain = new StringBasedDomainRegistry();
        domain.setDomainString("R(-3:3)");
        PresetNeuronDomain domainProvider = new PresetNeuronDomain();
        domainProvider.setWeightDomainPrototype(domain);
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        problem.initialise();
    }

    @Test
    public void testPercentages() {
        int refTraining = (int) (150 * 0.5); // 150 pattern in iris
        int refValidation = (int) (150 * 0.2);
        int refGeneralisation = (int) (150 * 0.3); // 150 pattern in iris
        assertEquals(refTraining, problem.getTrainingSet().size());
        assertEquals(refGeneralisation, problem.getGeneralisationSet().size());
    }

    @Test
    public void testCalculateFitness() {
        PSO pso = new PSO();
        pso.addStoppingCondition(new MeasuredStoppingCondition());
        pso.setOptimisationProblem(problem);
        pso.performInitialisation();
        pso.performIteration();
    }

    @Test
    public void testDomain() {
        DomainRegistry domainRegistry = problem.getDomain();
        assertEquals(19, domainRegistry.getBuiltRepresentation().size());
    }
}
