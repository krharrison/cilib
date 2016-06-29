/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.parametersetgenerator;

import cilib.controlparameter.ConstantControlParameter;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.UniformDistribution;
import cilib.math.random.generator.Rand;
import cilib.pso.selfadaptive.ParameterBounds;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.type.types.Bounds;

public class ConvergentParameterSetGenerator implements ParameterSetGenerator{

    protected ParameterBounds inertiaBounds;
    protected ParameterBounds socialBounds;
    protected ParameterBounds cognitiveBounds;
    protected ProbabilityDistributionFunction inertiaDistribution;
    protected ProbabilityDistributionFunction socialDistribution;
    protected ProbabilityDistributionFunction cognitiveDistribution;

    public ConvergentParameterSetGenerator(){
        inertiaBounds = new ParameterBounds(0, 1);
        socialBounds = new ParameterBounds(0, 4);
        cognitiveBounds = new ParameterBounds(0, 4);
        inertiaDistribution = new UniformDistribution(ConstantControlParameter.of(0), ConstantControlParameter.of(1));
        socialDistribution = new UniformDistribution(ConstantControlParameter.of(0), ConstantControlParameter.of(4));
        cognitiveDistribution = new UniformDistribution(ConstantControlParameter.of(0), ConstantControlParameter.of(4));
    }

    public ConvergentParameterSetGenerator(ConvergentParameterSetGenerator copy){
        this.inertiaBounds = copy.inertiaBounds;
        this.socialBounds = copy.socialBounds;
        this.cognitiveBounds = copy.cognitiveBounds;
        this.inertiaDistribution = copy.inertiaDistribution;
        this.cognitiveDistribution = copy.cognitiveDistribution;
        this.socialDistribution = copy.socialDistribution;
    }

    public ParameterSet generate(){

        ParameterSet params = new ParameterSet();

        do{
            params.setInertiaWeight(ConstantControlParameter.of(inertiaDistribution.getRandomNumber()));
            params.setCognitiveAcceleration(ConstantControlParameter.of(cognitiveDistribution.getRandomNumber()));
            params.setSocialAcceleration(ConstantControlParameter.of(socialDistribution.getRandomNumber()));
        } while(!params.isConvergent()); //TODO: add check for bounds

        return params;
    }

    public ConvergentParameterSetGenerator getClone(){
        return new ConvergentParameterSetGenerator(this);
    }

    /**
     * Sets the range for the inertia coefficient.
     * @param inertiaBounds The bounds for the inertia coefficient.
     */
    public void setInertiaBounds(ParameterBounds inertiaBounds){
        this.inertiaBounds = inertiaBounds;
    }

    public ParameterBounds getInertiaBounds(){
        return this.inertiaBounds;
    }

    /**
     * Sets the range for the social coefficient.
     * @param socialBounds The bounds for the social coefficient.
     */
    public void setSocialBounds(ParameterBounds socialBounds){
        this.socialBounds = socialBounds;
    }

    public ParameterBounds getSocialBounds(){
        return this.socialBounds;
    }

    /**
     * Sets the range for the cognitive coefficient.
     * @param cognitiveBounds The bounds for the cognitive coefficient.
     */
    public void setCognitiveBounds(ParameterBounds cognitiveBounds){
        this.cognitiveBounds = cognitiveBounds;
    }

    public ParameterBounds getCognitiveBounds(){
        return this.cognitiveBounds;
    }

    public void setInertiaDistribution(ProbabilityDistributionFunction distribution){
        this.inertiaDistribution = distribution;
    }

    public void setCognitiveDistribution(ProbabilityDistributionFunction distribution){
        this.cognitiveDistribution = distribution;
    }

    public void setSocialDistribution(ProbabilityDistributionFunction distribution){
        this.socialDistribution = distribution;
    }
}
