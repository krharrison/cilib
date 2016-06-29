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
 * Logarithm Inertia Weight
 *
 * Y. Gao, X. An, and J. Liu, “A Particle Swarm Optimization Algorithm with Logarithm Decreasing Inertia Weight and Chaos Mutation,”
 * in Proceedings of the 2008 International Conference on Computational Intelligence and Security, 2008, pp. 61–65.
 */
public class LogarithmDecreasingInertiaStrategy implements SwarmAdaptationStrategy {

    protected double minValue;
    protected double maxValue;
    protected double a;

    public LogarithmDecreasingInertiaStrategy(){
        minValue = 0.4;
        maxValue = 0.9;
        a = 1.0;
    }

    @Override
    public void adapt(PSO algorithm) {
        double inertia = maxValue + (minValue - maxValue) * Math.log10(a + 10 * algorithm.getPercentageComplete());

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

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }


}
