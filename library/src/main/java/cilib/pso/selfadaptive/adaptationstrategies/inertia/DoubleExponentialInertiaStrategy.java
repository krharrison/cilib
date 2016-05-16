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
import cilib.type.types.container.Vector;
import cilib.util.distancemeasure.DistanceMeasure;
import cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * Double Exponential inertia weight PSO
 *
 * P. Chauhan, K. Deep, and M. Pant, “Novel Inertia Weight Strategies for Particle Swarm Optimization,”
 * Memetic Computing, vol. 5, no. 3, pp. 229–251, 2013.
 */

public class DoubleExponentialInertiaStrategy implements AlgorithmAdaptationStrategy {

    private DistanceMeasure measure = new EuclideanDistanceMeasure();

    @Override
    public void adapt(PSO algorithm) {
        double completed = algorithm.getIterations();
        Vector gBest = (Vector) algorithm.getBestSolution().getPosition();

        if(algorithm.getIterations() == 0 ) return;  //no inertia change during first iteration
        for (Particle p : algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            double distance = measure.distance(gBest, sp.getBestPosition());
            double r = distance * (1 - completed);
            double newInertia = Math.exp(-Math.exp(-r));
            sp.setInertiaWeight(ConstantControlParameter.of(newInertia));
        }
    }

    @Override
    public DoubleExponentialInertiaStrategy getClone() {
        return this;
    }
}
