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
import cilib.pso.selfadaptive.ParameterBounds;
import cilib.pso.selfadaptive.ParameterSet;

public class VarianceParameterSetGenerator implements ParameterSetGenerator{

    protected ParameterBounds inertiaBounds;
    protected ParameterBounds accelerationBounds;
    protected ProbabilityDistributionFunction inertiaDistribution;
    protected ProbabilityDistributionFunction accelerationDistribution;
    protected double minVariance;

    public VarianceParameterSetGenerator(){
        inertiaBounds = new ParameterBounds(0, 1);
        accelerationBounds = new ParameterBounds(0, 2.1);
        inertiaDistribution = new UniformDistribution(ConstantControlParameter.of(-1), ConstantControlParameter.of(1));
        accelerationDistribution = new UniformDistribution(ConstantControlParameter.of(0), ConstantControlParameter.of(2.1));
        minVariance = 1;
    }

    public VarianceParameterSetGenerator(VarianceParameterSetGenerator copy){
        this.inertiaBounds = copy.inertiaBounds;
        this.accelerationBounds = copy.accelerationBounds;
        this.inertiaDistribution = copy.inertiaDistribution;
        this.accelerationDistribution = copy.accelerationDistribution;
        this.minVariance = copy.minVariance;
    }

    public ParameterSet generate(){

        ParameterSet params = new ParameterSet();
        double w;
        double c;
        do{
            w = inertiaDistribution.getRandomNumber();
            c = accelerationDistribution.getRandomNumber();

            params.setInertiaWeight(ConstantControlParameter.of(w));
            params.setSocialAcceleration(ConstantControlParameter.of(c));
            params.setCognitiveAcceleration(ConstantControlParameter.of(c));

        } while(!(params.isConvergent() || varianceCheck(w, c))); //TODO: add check for bounds

        params.setInertiaWeight(ConstantControlParameter.of(w));
        params.setSocialAcceleration(ConstantControlParameter.of(c));
        params.setCognitiveAcceleration(ConstantControlParameter.of(c));

        return params;
    }

    private boolean varianceCheck(double w, double c) {
        double numerator = c * (w + 1);
        double denominator = 4 * ( c * ( 5 * w - 7) - 12 * (w*w) + 12);

        return numerator / denominator > minVariance;
    }

    public VarianceParameterSetGenerator getClone(){
        return new VarianceParameterSetGenerator(this);
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
     * Sets the range for the acceleration coefficients.
     * @param accelerationBounds The bounds for the acceleration coefficients.
     */
    public void setAccelerationBounds(ParameterBounds accelerationBounds){
        this.accelerationBounds = accelerationBounds;
    }

    public ParameterBounds getAccelerationBounds(){
        return this.accelerationBounds;
    }

    public void setInertiaDistribution(ProbabilityDistributionFunction distribution){
        this.inertiaDistribution = distribution;
    }

    public void setAcclerationDistribution(ProbabilityDistributionFunction distribution){
        this.accelerationDistribution = distribution;
    }

}
