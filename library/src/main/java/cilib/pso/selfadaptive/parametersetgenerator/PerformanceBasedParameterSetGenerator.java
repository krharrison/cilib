/**
 *           __  __
 *   _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *  / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.parametersetgenerator;

import cilib.controlparameter.ConstantControlParameter;
import cilib.math.random.GaussianDistribution;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.UniformDistribution;
import cilib.math.random.generator.Rand;
import cilib.pso.selfadaptive.ParameterSet;

public class PerformanceBasedParameterSetGenerator implements ParameterSetGenerator {

    protected ProbabilityDistributionFunction inertiaDistribution;
    protected GaussianDistribution accelerationDistribution;

    public PerformanceBasedParameterSetGenerator(){
        accelerationDistribution = new GaussianDistribution();
        inertiaDistribution = new UniformDistribution();
        //inertiaDistribution.setMean(ConstantControlParameter.of(0.7));
        //inertiaDistribution.setDeviation(ConstantControlParameter.of(0.1));
    }

    public PerformanceBasedParameterSetGenerator(PerformanceBasedParameterSetGenerator copy){
        this.inertiaDistribution = copy.inertiaDistribution;
        this.accelerationDistribution = copy.accelerationDistribution;
    }

    @Override
    public ParameterSet generate() {
        double inertia = inertiaDistribution.getRandomNumber(0,1);

        //calculate the cusp of poli's region based on the inertia value
        double cusp = (24 * (1 - inertia * inertia)) / (7 - 5 * inertia);

        //generate a value of C near the cusp, then evenly divide between c1 and c2
        //double c = accelerationDistribution.getRandomNumber(cusp, 0.1);

        double c = cusp - 0.1 * Rand.nextDouble();

        ParameterSet params = new ParameterSet();
        params.setInertiaWeight(ConstantControlParameter.of(inertia));
        params.setCognitiveAcceleration(ConstantControlParameter.of(c/2));
        params.setSocialAcceleration(ConstantControlParameter.of(c/2));

        return params;
    }

    @Override
    public ParameterSetGenerator getClone() {
        return new PerformanceBasedParameterSetGenerator(this);
    }
}
