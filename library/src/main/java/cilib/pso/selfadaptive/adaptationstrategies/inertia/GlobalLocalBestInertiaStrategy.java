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
import cilib.pso.selfadaptive.adaptationstrategies.SwarmAdaptationStrategy;
import cilib.util.distancemeasure.DistanceMeasure;
import cilib.util.distancemeasure.EuclideanDistanceMeasure;
import fj.F2;

/**
 * Global-Local best inertia weight PSO
 *
 * M. S. Arumugam and M. V. C. Rao, “On the Improved Performances of the Particle Swarm Optimization Algorithms  with
 * Adaptive Parameters, Cross-over Operators and Root Mean Square (RMS) Variants for  Computing Optimal Control of
 * a Class of Hybrid Systems,” Applied Soft Computing, vol. 8, no. 1, pp. 324–336, Jan. 2008.
 */

public class GlobalLocalBestInertiaStrategy implements SwarmAdaptationStrategy {

    private DistanceMeasure measure = new EuclideanDistanceMeasure();

    @Override
    public void adapt(PSO algorithm) {
        double gBest = algorithm.getBestSolution().getFitness().getValue();

        double sumPersonalBest = algorithm.getTopology().foldLeft(new F2<Double, Particle, Double>() {
            @Override
            public Double f(Double sum, Particle particle) {
                return sum + particle.getBestFitness().getValue().doubleValue();
            }
        }, 0.0);

        double avgPersonalBest = sumPersonalBest / algorithm.getTopology().length();
        double inertia = 1.1 - gBest / avgPersonalBest;

        for (Particle p : algorithm.getTopology()) {
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
        }
    }


    @Override
    public GlobalLocalBestInertiaStrategy getClone() {
        return this;
    }
}
