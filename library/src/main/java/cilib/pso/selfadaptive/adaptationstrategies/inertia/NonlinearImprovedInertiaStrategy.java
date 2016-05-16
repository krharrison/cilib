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
 * Nonlinear improved inertia.
 *
 * B. Jiao, Z. Lian, and X. Gu, “A Dynamic Inertia Weight Particle Swarm Optimization Algorithm,”
 * Chaos, Solitons and Fractals, vol. 37, no. 3, pp. 698–705, 2008.
 */

public class NonlinearImprovedInertiaStrategy implements AlgorithmAdaptationStrategy{

    protected double initialInertia;
    protected double u;

    public NonlinearImprovedInertiaStrategy() {
        initialInertia = 0.3;
        u = 1.0002;
    }

    @Override
    public void adapt(PSO algorithm) {
        double inertia = initialInertia * Math.pow(u, -algorithm.getIterations());

        for (Particle p : algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle)p;
            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
        }
    }

    @Override
    public AlgorithmAdaptationStrategy getClone() {
        return this;
    }

    public double getInitialInertia() {
        return initialInertia;
    }

    public void setInitialInertia(double initialInertia) {
        this.initialInertia = initialInertia;
    }

    public double getU() {
        return u;
    }

    public void setU(double u) {
        this.u = u;
    }
}
