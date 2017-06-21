/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.parametersetgenerator;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.controlparameter.LinearlyVaryingControlParameter;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.UniformDistribution;
import cilib.pso.selfadaptive.ParameterBounds;
import cilib.pso.selfadaptive.ParameterSet;
import org.omg.Dynamic.Parameter;

public class ConvergentParameterSetGenerator2 implements ParameterSetGenerator{

    //protected ParameterBounds inertiaBounds;
    //protected ParameterBounds socialBounds;
    //protected ParameterBounds cognitiveBounds;
    protected ProbabilityDistributionFunction inertiaDistribution;
    protected ProbabilityDistributionFunction socialDistribution;
    protected ProbabilityDistributionFunction cognitiveDistribution;
    protected ControlParameter factor;

    public ConvergentParameterSetGenerator2(){
        //inertiaBounds = new ParameterBounds(0, 1);
        //socialBounds = new ParameterBounds(0, 4);
        //cognitiveBounds = new ParameterBounds(0, 4);
        inertiaDistribution = new UniformDistribution(ConstantControlParameter.of(-1), ConstantControlParameter.of(1));
        socialDistribution = new UniformDistribution(ConstantControlParameter.of(0), ConstantControlParameter.of(4));
        cognitiveDistribution = new UniformDistribution(ConstantControlParameter.of(0), ConstantControlParameter.of(4));
        factor = ConstantControlParameter.of(0.75);
    }

    public ConvergentParameterSetGenerator2(ConvergentParameterSetGenerator2 copy){
        //this.inertiaBounds = copy.inertiaBounds;
        //this.socialBounds = copy.socialBounds;
        //this.cognitiveBounds = copy.cognitiveBounds;
        this.inertiaDistribution = copy.inertiaDistribution;
        this.cognitiveDistribution = copy.cognitiveDistribution;
        this.socialDistribution = copy.socialDistribution;
    }

    public ParameterSet generate(){

        ParameterSet params = new ParameterSet();

        double w, c1, c2;

        do{
            w = inertiaDistribution.getRandomNumber();
            c1 = cognitiveDistribution.getRandomNumber();
            c2 = socialDistribution.getRandomNumber();
        } while(!check(w, c1, c2)); //TODO: add check for bounds

        params.setInertiaWeight(ConstantControlParameter.of(w));
        params.setCognitiveAcceleration(ConstantControlParameter.of(c1));
        params.setSocialAcceleration(ConstantControlParameter.of(c2));

        return params;
    }


    private boolean check(double w, double c1, double c2)
    {
        double upper = (24 * (1 - w * w)) / (7 - 5 * w);
        double lower = (22 - 30 * w * w) / (7 - 5 * w);
        double c = c1 + c2;

        return c > lower && c < upper;
    }

    public ConvergentParameterSetGenerator2 getClone(){
        return new ConvergentParameterSetGenerator2(this);
    }

    /**
     * Sets the range for the inertia coefficient.
     * @param inertiaBounds The bounds for the inertia coefficient.
     */
    //public void setInertiaBounds(ParameterBounds inertiaBounds){
    //    this.inertiaBounds = inertiaBounds;
    //}

    //public ParameterBounds getInertiaBounds(){
    //    return this.inertiaBounds;
    //}

    /**
     * Sets the range for the social coefficient.
     * @param socialBounds The bounds for the social coefficient.
     */
    //public void setSocialBounds(ParameterBounds socialBounds){
    //    this.socialBounds = socialBounds;
    //}

    //public ParameterBounds getSocialBounds(){
    //    return this.socialBounds;
    //}

    /**
     * Sets the range for the cognitive coefficient.
     * @param cognitiveBounds The bounds for the cognitive coefficient.
     */
    //public void setCognitiveBounds(ParameterBounds cognitiveBounds){
    //   this.cognitiveBounds = cognitiveBounds;
   // }

    //public ParameterBounds getCognitiveBounds(){
    //    return this.cognitiveBounds;
    //}

    public void setInertiaDistribution(ProbabilityDistributionFunction distribution){
        this.inertiaDistribution = distribution;
    }

    public void setCognitiveDistribution(ProbabilityDistributionFunction distribution){
        this.cognitiveDistribution = distribution;
    }

    public void setSocialDistribution(ProbabilityDistributionFunction distribution){
        this.socialDistribution = distribution;
    }

    public void setFactor(ControlParameter parameter){
        this.factor = parameter;
    }
}
