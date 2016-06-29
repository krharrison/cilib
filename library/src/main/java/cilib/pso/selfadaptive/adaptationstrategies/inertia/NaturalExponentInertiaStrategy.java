/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies.inertia;

import cilib.controlparameter.ConstantControlParameter;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.adaptationstrategies.SwarmAdaptationStrategy;

/**
 * Natural Exponent Inertia Weight
 *
 * G. Chen, Z. Min, J. Jia, and H. Xinbo, “Natural Exponential Inertia Weight Strategy in Particle Swarm Optimization,”
 * Proceedings of the 6th World Congress on Intelligent Control and Automation, vol. 1, pp. 3672–3675, 2006.
 */
public class NaturalExponentInertiaStrategy implements SwarmAdaptationStrategy {

    protected double minValue;
    protected double maxValue;

    public NaturalExponentInertiaStrategy(){
        minValue = 0.4;
        maxValue = 0.9;
    }

    @Override
    public void adapt(PSO algorithm) {
        double inertia = minValue + (maxValue - minValue) * Math.exp(-10 * algorithm.getPercentageComplete());

        for (Particle p : algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
        }
    }

    @Override
    public SwarmAdaptationStrategy getClone() {
        return this;
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
