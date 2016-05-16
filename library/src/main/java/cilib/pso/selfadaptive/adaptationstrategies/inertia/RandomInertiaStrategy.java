/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies.inertia;

import cilib.controlparameter.ConstantControlParameter;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.UniformDistribution;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.adaptationstrategies.AlgorithmAdaptationStrategy;

/**
 * Random Inertia Weight Strategy
 *
 * R. C. Eberhart and Y. Shi, “Tracking and Optimizing Dynamic Systems with Particle Swarms,”
 * in Proceedings of the 2001 IEEE Congress on Evolutionary Computation, 2001, vol. 1, pp. 94–100.
 */

public class RandomInertiaStrategy implements AlgorithmAdaptationStrategy{

    protected ProbabilityDistributionFunction distribution;
    protected double baseInertia;

    public RandomInertiaStrategy(){
        distribution = new UniformDistribution(ConstantControlParameter.of(0), ConstantControlParameter.of(0.5));
        baseInertia = 0.5;
    }

    public RandomInertiaStrategy(RandomInertiaStrategy copy){
        this.distribution = copy.distribution;
        this.baseInertia = copy.baseInertia;
    }

    @Override
    public void adapt(PSO algorithm) {
        double inertia = baseInertia + distribution.getRandomNumber();

        for(Particle p: algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle)p;
            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
        }
    }

    @Override
    public RandomInertiaStrategy getClone() {
        return new RandomInertiaStrategy(this);
    }

    public ProbabilityDistributionFunction getDistribution() {
        return distribution;
    }

    public void setDistribution(ProbabilityDistributionFunction distribution) {
        this.distribution = distribution;
    }

    public double getBaseInertia() {
        return baseInertia;
    }

    public void setBaseInertia(double baseInertia) {
        this.baseInertia = baseInertia;
    }

}
