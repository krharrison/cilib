/**
 *         __  __
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
import cilib.pso.selfadaptive.adaptationstrategies.AlgorithmAdaptationStrategy;

/**
 * PSO with Oscillating Inertia Weight
 *
 * K. Kentzoglanakis and M. Poole, “Particle Swarm Optimization with an Oscillating Inertia Weight,”
 * in Proceedings of the 11th Annual conference on Genetic and evolutionary computation, 2009, pp. 1749–1750.
 */

public class OscillatingInertiaStrategy implements AlgorithmAdaptationStrategy {

    protected double minValue;
    protected double maxValue;
    protected double k;
    protected double maxIters;

    public OscillatingInertiaStrategy(){
        minValue = 0.3;
        maxValue = 0.9;
        k = 7;
        maxIters = 5000;
    }

    @Override
    public void adapt(PSO algorithm) {
        double inertia;

        if(algorithm.getPercentageComplete() > 0.75){
            inertia = minValue;
        }
        else{
            int iter = algorithm.getIterations();
            double left = (minValue + maxValue) / 2;
            double right = ((maxValue - minValue) / 2) * Math.cos((4 * Math.PI * iter * (2 * k + 3)) / (3 * maxIters));
            inertia = left + right;
        }

        for (Particle p : algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
        }
    }

    @Override
    public AlgorithmAdaptationStrategy getClone() {
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

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getMaxIterations() {
        return maxIters;
    }

    public void setMaxIterations(double maxIters) {
        this.maxIters = maxIters;
    }
}
