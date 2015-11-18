/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.controlparameter.adaptation;

import cilib.controlparameter.AdaptableControlParameter;
import cilib.controlparameter.ConstantControlParameter;
import cilib.entity.Entity;
import cilib.math.random.CauchyDistribution;
import cilib.math.random.GaussianDistribution;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.generator.Rand;

/**
 * This is the DE with Neighbourhood Search parameter adaptation strategy described
 * in the beginning of Zhenyu, Tang and Yao's 2008 paper "Self-adaptive Differential
 * Evolution with Neighbourhood Search".
 */
public class NSDEParameterAdaptationStrategy implements ParameterAdaptationStrategy{
    private double scalingFactorProbability;
    private ProbabilityDistributionFunction random;
    private ProbabilityDistributionFunction cauchyVariableRandom;

    /*
     * Default constructor for the NSDEParameterAdaptationStrategy
     */
    public NSDEParameterAdaptationStrategy() {
        GaussianDistribution gaussian = new GaussianDistribution();
        gaussian.setDeviation(ConstantControlParameter.of(0.5));
        random = gaussian;
        cauchyVariableRandom = new CauchyDistribution();
        scalingFactorProbability = 0.5;
    }

    /*
     * Copy constructor of the NSDEParameterAdaptationStrategy
     * @param copy The NSDEParameterAdaptationStrategy to be copied
     */
    public NSDEParameterAdaptationStrategy(NSDEParameterAdaptationStrategy copy) {
        scalingFactorProbability = copy.scalingFactorProbability;
        random = copy.random;
        cauchyVariableRandom = copy.cauchyVariableRandom;
    }

    /*
     * Clone method of NSDEParameterAdaptationStrategy
     * @return A new instance of this NSDEParameterAdaptationStrategy
     */
    @Override
    public ParameterAdaptationStrategy getClone() {
        return new NSDEParameterAdaptationStrategy(this);
    }

    /*
     * This changes the value of the parameter according to the
     * DE with Neighbourhood Search
     * @param parameter The parameter to be changed
     */
    @Override
    public void change(AdaptableControlParameter parameter) {
        if(Rand.nextDouble() < scalingFactorProbability) {
            parameter.update(random.getRandomNumber());
        } else {
            parameter.update(cauchyVariableRandom.getRandomNumber());
        }
    }

    /*
     * This function is not applicable to the DE with Neighbourhood Search
     */
    @Override
    public void accepted(AdaptableControlParameter parameter, Entity entity, boolean accepted) {
        throw new UnsupportedOperationException("Not necessary for this adaptation strategy");
    }

    /*
     * This function is not applicable to the DE with Neighbourhood Search
     */
    @Override
    public double recalculateAdaptiveVariables() {
        throw new UnsupportedOperationException("No adaptive variables to be recalculated");
    }

    /*
     * Returns the value of the scaling factor probability variable
     * @return The scaling factor probability
     */
    public double getScalingFactorProbability() {
        return scalingFactorProbability;
    }

    /*
     * Sets the value of the scaling factor probability to the one received as a parameter
     * @param scalingFactorProbability The new value for the scaling factor probability
     */
    public void setScalingFactorProbability(double scalingFactorProbability) {
        this.scalingFactorProbability = scalingFactorProbability;
    }

}
