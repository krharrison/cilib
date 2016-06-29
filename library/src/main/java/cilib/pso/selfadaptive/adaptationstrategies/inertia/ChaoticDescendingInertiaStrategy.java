/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies.inertia;

import cilib.controlparameter.ConstantControlParameter;
import cilib.math.random.generator.Rand;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.adaptationstrategies.SwarmAdaptationStrategy;

/**
 * Chaotic Descending Inertia Weight
 *
 * Y. Feng, G. Teng, A. Wang, and Y. Yao, “Chaotic Inertia Weight in Particle Swarm Optimization,”
 * in Second International Conference on Innovative Computing, Information and Control, 2007, pp. 475–479.
 */
public class ChaoticDescendingInertiaStrategy implements SwarmAdaptationStrategy {

    protected double z;
    protected double minValue;
    protected double maxValue;

    public ChaoticDescendingInertiaStrategy(){
        this.z = Rand.nextDouble();
        minValue = 0.4;
        maxValue = 0.9;
    }

    @Override
    public void adapt(PSO algorithm) {
        z = logistic(z);
        double inertia = z * minValue + (maxValue - minValue) * (1 - algorithm.getPercentageComplete());

        for (Particle p : algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
        }
    }

    @Override
    public SwarmAdaptationStrategy getClone() {
        return this;
    }

    private double logistic(double x){
        return 4 * x * (1 - x);
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

}
