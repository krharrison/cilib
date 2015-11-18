/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.ec;

import org.junit.Assert;
import org.junit.Test;
import cilib.controlparameter.adaptation.SaDEParameterAdaptationStrategy;
import cilib.controlparameter.initialisation.RandomParameterInitialisationStrategy;
import cilib.problem.FunctionOptimisationProblem;
import cilib.functions.continuous.unconstrained.Spherical;
import cilib.controlparameter.AdaptableControlParameter;
import cilib.entity.operators.creation.RandCreationStrategy;
import cilib.entity.operators.crossover.de.DifferentialEvolutionBinomialCrossover;

public class SaDEIndividualTest {

    @Test
     public void updateParametersTest() {
        SaDEIndividual individual = new SaDEIndividual();
        RandCreationStrategy creationStrategy = new RandCreationStrategy();
        DifferentialEvolutionBinomialCrossover crossoverStrategy = new DifferentialEvolutionBinomialCrossover();
        AdaptableControlParameter param = new AdaptableControlParameter();
        param.setParameter(5.0);
        creationStrategy.setScaleControlParameter(param);
        crossoverStrategy.setCrossoverPointProbability(param.getClone());
        individual.setTrialVectorCreationStrategy(creationStrategy);
        individual.setCrossoverStrategy(crossoverStrategy);

        FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
            problem.setDomain("R(-5.12:5.12)^30");
            problem.setFunction(new Spherical());
        individual.initialise(problem);
        ((AdaptableControlParameter) individual.getTrialVectorCreationStrategy().getScaleParameter()).setParameter(5.0);
        ((AdaptableControlParameter) individual.getCrossoverStrategy().getCrossoverPointProbability()).setParameter(5.0);

        individual.updateParameters();

        Assert.assertFalse(individual.getTrialVectorCreationStrategy().getScaleParameter().getParameter() == 5.0);
        Assert.assertFalse(individual.getCrossoverStrategy().getCrossoverPointProbability().getParameter() == 5.0);
     }

    @Test
    public void acceptParametersTest() {
       SaDEIndividual individual = new SaDEIndividual();
       individual.getTrialVectorCreationStrategy().setScaleParameter(2.0);
       individual.getCrossoverStrategy().setCrossoverPointProbability(new AdaptableControlParameter(3.0));
       individual.acceptParameters(true, individual);

       double scalingFactorExperience = ((SaDEParameterAdaptationStrategy) individual.getScalingFactorParameterAdaptationStrategy()).getLearningExperience().get(0);
       double crossoverExperience = ((SaDEParameterAdaptationStrategy) individual.getCrossoverProbabilityParameterAdaptationStrategy()).getLearningExperience().get(0);

       Assert.assertTrue(2.0 == scalingFactorExperience);
       Assert.assertTrue(3.0 == crossoverExperience);

    }

    @Test
    public void getTrialVectorCreationStrategyTest() {
        SaDEIndividual individual = new SaDEIndividual();
        RandCreationStrategy strategy = new RandCreationStrategy();
        individual.setTrialVectorCreationStrategy(strategy);

        Assert.assertEquals(strategy, individual.getTrialVectorCreationStrategy());
    }

    @Test
    public void setTrialVectorCreationStrategyTest() {
        SaDEIndividual individual = new SaDEIndividual();
        RandCreationStrategy strategy = new RandCreationStrategy();
        individual.setTrialVectorCreationStrategy(strategy);

        Assert.assertEquals(strategy, individual.getTrialVectorCreationStrategy());
    }

    @Test
    public void getCrossoverStrategyTest() {
        SaDEIndividual individual = new SaDEIndividual();
        DifferentialEvolutionBinomialCrossover strategy = new DifferentialEvolutionBinomialCrossover();
        individual.setCrossoverStrategy(strategy);

        Assert.assertEquals(strategy, individual.getCrossoverStrategy());
    }

    @Test
    public void setCrossoverStrategyTest() {
        SaDEIndividual individual = new SaDEIndividual();
        DifferentialEvolutionBinomialCrossover strategy = new DifferentialEvolutionBinomialCrossover();
        individual.setCrossoverStrategy(strategy);

        Assert.assertEquals(strategy, individual.getCrossoverStrategy());
    }

    @Test
    public void getScalingFactorParameterAdaptationStrategyTest() {
        SaDEIndividual individual = new SaDEIndividual();
        SaDEParameterAdaptationStrategy strategy = new SaDEParameterAdaptationStrategy();
        individual.setScalingFactorParameterAdaptationStrategy(strategy);

        Assert.assertEquals(strategy, individual.getScalingFactorParameterAdaptationStrategy());
    }

    @Test
    public void setScalingFactorParameterAdaptationStrategyTest() {
        SaDEIndividual individual = new SaDEIndividual();
        SaDEParameterAdaptationStrategy strategy = new SaDEParameterAdaptationStrategy();
        individual.setScalingFactorParameterAdaptationStrategy(strategy);

        Assert.assertEquals(strategy, individual.getScalingFactorParameterAdaptationStrategy());
    }

    @Test
    public void getCrossoverProbabilityParameterAdaptationStrategyTest() {
       SaDEIndividual individual = new SaDEIndividual();
        SaDEParameterAdaptationStrategy strategy = new SaDEParameterAdaptationStrategy();
        individual.setCrossoverProbabilityParameterAdaptationStrategy(strategy);

        Assert.assertEquals(strategy, individual.getCrossoverProbabilityParameterAdaptationStrategy());
    }

    @Test
    public void setCrossoverProbabilityParameterAdaptationStrategyTest() {
        SaDEIndividual individual = new SaDEIndividual();
        SaDEParameterAdaptationStrategy strategy = new SaDEParameterAdaptationStrategy();
        individual.setCrossoverProbabilityParameterAdaptationStrategy(strategy);

        Assert.assertEquals(strategy, individual.getCrossoverProbabilityParameterAdaptationStrategy());
    }

    @Test
    public void getScalingFactorInitialisationStrategyTest() {
        SaDEIndividual individual = new SaDEIndividual();
        RandomParameterInitialisationStrategy strategy = new RandomParameterInitialisationStrategy();
        individual.setScalingFactorInitialisationStrategy(strategy);

        Assert.assertEquals(strategy, individual.getScalingFactorInitialisationStrategy());
    }

    @Test
    public void setScalingFactorInitialisationStrategyTest() {
        SaDEIndividual individual = new SaDEIndividual();
        RandomParameterInitialisationStrategy strategy = new RandomParameterInitialisationStrategy();
        individual.setScalingFactorInitialisationStrategy(strategy);

        Assert.assertEquals(strategy, individual.getScalingFactorInitialisationStrategy());
    }

    @Test
    public void getCrossoverProbabilityInitialisationStrategyTest() {
        SaDEIndividual individual = new SaDEIndividual();
        RandomParameterInitialisationStrategy strategy = new RandomParameterInitialisationStrategy();
        individual.setCrossoverProbabilityInitialisationStrategy(strategy);

        Assert.assertEquals(strategy, individual.getCrossoverProbabilityInitialisationStrategy());
    }

    @Test
    public void setCrossoverProbabilityInitialisationStrategyTest() {
        SaDEIndividual individual = new SaDEIndividual();
        RandomParameterInitialisationStrategy strategy = new RandomParameterInitialisationStrategy();
        individual.setCrossoverProbabilityInitialisationStrategy(strategy);

        Assert.assertEquals(strategy, individual.getCrossoverProbabilityInitialisationStrategy());
    }
}
