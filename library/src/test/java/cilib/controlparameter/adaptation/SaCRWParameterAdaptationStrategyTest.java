/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.controlparameter.adaptation;

import java.util.ArrayList;
import junit.framework.Assert;
import cilib.entity.Property;
import org.junit.Test;
import cilib.controlparameter.AdaptableControlParameter;
import cilib.ec.SaDEIndividual;
import cilib.problem.solution.MinimisationFitness;
import cilib.controlparameter.initialisation.RandomParameterInitialisationStrategy;
import cilib.math.random.GaussianDistribution;

public class SaCRWParameterAdaptationStrategyTest {
    @Test
    public void changeTest() {
        SaCRWParameterAdaptationStrategy strategy = new SaCRWParameterAdaptationStrategy();
        AdaptableControlParameter parameter = new AdaptableControlParameter();
        parameter.setParameter(5.0);
        strategy.change(parameter);
        Assert.assertTrue(parameter.getParameter() != 5.0);
    }

    @Test
    public void acceptedTest() {
        SaCRWParameterAdaptationStrategy strategy = new SaCRWParameterAdaptationStrategy();
        SaDEIndividual individual = new SaDEIndividual();
        individual.put(Property.FITNESS, new MinimisationFitness(2.0));
        individual.setPreviousFitness(new MinimisationFitness(3.0));
        AdaptableControlParameter parameter = new AdaptableControlParameter();
        parameter.setParameter(5.0);

        strategy.accepted(parameter, individual, true);

        Assert.assertEquals(5.0, strategy.getLearningExperience().get(0));
        Assert.assertEquals(1.0 , strategy.getFitnessDifferences().get(0));
    }

    @Test
    public void recalculateAdaptiveVariablesTest() {
        ArrayList<Double> learningExperience = new ArrayList<Double>();
        learningExperience.add(2.0);
        learningExperience.add(5.0);

        ArrayList<Double> fitnessDifferences = new ArrayList<Double>();
        fitnessDifferences.add(6.0);
        fitnessDifferences.add(1.0);

        SaCRWParameterAdaptationStrategy strategy = new SaCRWParameterAdaptationStrategy();
        strategy.setLearningExperience(learningExperience);
        strategy.setFitnessDifferences(fitnessDifferences);
        strategy.recalculateAdaptiveVariables();

        Assert.assertEquals(Math.round(2.4285714 * 5) / 5, Math.round(strategy.getCrossoverMean() * 5) / 5);

    }


    @Test
    public void getLearningExperienceTest() {
        SaCRWParameterAdaptationStrategy strategy = new SaCRWParameterAdaptationStrategy();
        ArrayList<Double> learningExperience = new ArrayList<Double>();
        strategy.setLearningExperience(learningExperience);

        Assert.assertEquals(learningExperience, strategy.getLearningExperience());
    }

    @Test
    public void setLearningExperienceTest() {
        SaCRWParameterAdaptationStrategy strategy = new SaCRWParameterAdaptationStrategy();
        ArrayList<Double> learningExperience = new ArrayList<Double>();
        strategy.setLearningExperience(learningExperience);

        Assert.assertEquals(learningExperience, strategy.getLearningExperience());
    }

    @Test
    public void getFitnessDifferencesTest() {
        SaCRWParameterAdaptationStrategy strategy = new SaCRWParameterAdaptationStrategy();
        ArrayList<Double> fitnessDifferences = new ArrayList<Double>();
        strategy.setFitnessDifferences(fitnessDifferences);

        Assert.assertEquals(fitnessDifferences, strategy.getFitnessDifferences());
    }

    @Test
    public void setFitnessDifferencesTest() {
        SaCRWParameterAdaptationStrategy strategy = new SaCRWParameterAdaptationStrategy();
        ArrayList<Double> fitnessDifferences = new ArrayList<Double>();
        strategy.setFitnessDifferences(fitnessDifferences);

        Assert.assertEquals(fitnessDifferences, strategy.getFitnessDifferences());
    }

    @Test
    public void getCrossoverMeanTest() {
        SaCRWParameterAdaptationStrategy strategy = new SaCRWParameterAdaptationStrategy();
        strategy.setCrossoverMean(2.0);

        Assert.assertEquals(2.0, strategy.getCrossoverMean());
    }

    @Test
    public void setCrossoverMeanTest() {
        SaCRWParameterAdaptationStrategy strategy = new SaCRWParameterAdaptationStrategy();
        strategy.setCrossoverMean(2.0);

        Assert.assertEquals(2.0, strategy.getCrossoverMean());
    }

    @Test
    public void getRandomTest() {
        SaCRWParameterAdaptationStrategy strategy = new SaCRWParameterAdaptationStrategy();
        GaussianDistribution random = new GaussianDistribution();
        strategy.setRandom(random);

        Assert.assertEquals(random, strategy.getRandom());
    }

    @Test
    public void setRandomTest() {
        SaCRWParameterAdaptationStrategy strategy = new SaCRWParameterAdaptationStrategy();
        GaussianDistribution random = new GaussianDistribution();
        strategy.setRandom(random);

        Assert.assertEquals(random, strategy.getRandom());
    }

    @Test
    public void getInitialisationStrategyTest() {
        SaCRWParameterAdaptationStrategy strategy = new SaCRWParameterAdaptationStrategy();
        RandomParameterInitialisationStrategy init = new RandomParameterInitialisationStrategy();
        strategy.setInitialisationStrategy(init);

        Assert.assertEquals(init, strategy.getInitialisationStrategy());
    }

    @Test
    public void setInitialisationStrategyTest() {
        SaCRWParameterAdaptationStrategy strategy = new SaCRWParameterAdaptationStrategy();
        RandomParameterInitialisationStrategy init = new RandomParameterInitialisationStrategy();
        strategy.setInitialisationStrategy(init);

        Assert.assertEquals(init, strategy.getInitialisationStrategy());
    }
}
