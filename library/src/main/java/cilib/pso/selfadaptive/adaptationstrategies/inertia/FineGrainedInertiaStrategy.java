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
import cilib.type.types.container.Vector;
import cilib.util.distancemeasure.DistanceMeasure;
import cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * Fine-grained inertia weight PSO
 *
 * K. Deep, P. Chauhan, and M. Pant, “A New Fine Grained Inertia Weight Particle Swarm Optimization,”
 * in Proceedings of the 2011 World Congress on Information and Communication Technologies, 2011, pp. 424–429.
 */

public class FineGrainedInertiaStrategy implements SwarmAdaptationStrategy {

    private DistanceMeasure measure = new EuclideanDistanceMeasure();

    @Override
    public void adapt(PSO algorithm) {
        double completed = algorithm.getPercentageComplete();
        Vector gBest = (Vector) algorithm.getBestSolution().getPosition();

        if(algorithm.getIterations() == 0 ) return;  //no inertia change during first iteration
        for (Particle p : algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            double inertia = sp.getInertiaWeight().getParameter();
            double diff = measure.distance(gBest, sp.getBestPosition());

            double newInertia = inertia - (inertia - 0.4) * Math.exp(-diff*completed);
            sp.setInertiaWeight(ConstantControlParameter.of(newInertia));
        }
    }

    @Override
    public FineGrainedInertiaStrategy getClone() {
        return this;
    }

}
