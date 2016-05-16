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
 * S.-K. S. Fan and Y.-Y. Chiu, “A Decreasing Inertia Weight Particle Swarm Optimizer,”
 * Engineering Optimization, vol. 39, no. 2, pp. 203–228, 2007.
 */

public class DWPSOInertiaStrategy implements AlgorithmAdaptationStrategy {
    @Override
    public void adapt(PSO algorithm) {
        int iter = algorithm.getIterations();
        double inertia = iter == 0 ? 1 : Math.pow(2 / (double)iter, 0.3);

        for (Particle p : algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
        }
    }

    @Override
    public AlgorithmAdaptationStrategy getClone() {
        return this;
    }
}
