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
import cilib.pso.selfadaptive.adaptationstrategies.AlgorithmAdaptationStrategy;

/**
 * Nonlinear time-varying inertia weight.
 *
 * C. Yang, W. Gao, N. Liu, and C. Song, “Low-discrepancy Sequence Initialized Particle Swarm Optimization Algorithm
 * with High-Order Nonlinear Time-varying Inertia Weight,” Applied Soft Computing, vol. 29, pp. 386–394, 2015.
 */

public class NonlinearInertiaStrategy implements AlgorithmAdaptationStrategy{

    protected double minInertia;
    protected double maxInertia;
    protected double alpha;

    public NonlinearInertiaStrategy() {
        this.minInertia = 0.4;
        this.maxInertia = 0.9;
        this.alpha = 1 / (Math.PI * Math.PI);
    }

    @Override
    public void adapt(PSO algorithm) {
        double inertia = maxInertia - (maxInertia - minInertia) * Math.pow(algorithm.getPercentageComplete(), alpha);

        for (Particle p : algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle)p;
            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
        }
    }

    @Override
    public AlgorithmAdaptationStrategy getClone() {
        return this;
    }

    public double getMinInertia() {
        return minInertia;
    }

    public void setMinInertia(double minInertia) {
        this.minInertia = minInertia;
    }

    public double getMaxInertia() {
        return maxInertia;
    }

    public void setMaxInertia(double maxInertia) {
        this.maxInertia = maxInertia;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
}
