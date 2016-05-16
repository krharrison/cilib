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
 * Inertia weight using a Sugeno function.
 *
 * K. Lei, Y. Qiu, and Y. He, “A New Adaptive Well-Chosen Inertia Weight Strategy to Automatically Harmonize Global
 * and Local Search Ability in Particle Swarm Optimization,” in 1st International Symposium on Systems and Control
 * in Aerospace and Astronautics, 2006, pp. 977–980.
 */

public class SugenoFunctionInertiaStrategy implements AlgorithmAdaptationStrategy {
    protected double s;

    public SugenoFunctionInertiaStrategy(){
        s = 2.0;
    }

    @Override
    public void adapt(PSO algorithm) {
        double completed = algorithm.getPercentageComplete();

        double inertia = (1 - completed) / (1 + s * completed);

        for (Particle p : algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
        }
    }

    @Override
    public AlgorithmAdaptationStrategy getClone() {
        return this;
    }

    public double getS() {
        return s;
    }

    public void setS(double s) {
        this.s = s;
    }
}
