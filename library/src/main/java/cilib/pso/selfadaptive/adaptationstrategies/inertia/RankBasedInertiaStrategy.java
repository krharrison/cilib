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
import cilib.util.selection.arrangement.Arrangement;
import cilib.util.selection.arrangement.SortedArrangement;

import java.util.Iterator;

/**
 * Rank-based inertia strategy
 *
 * B. K. Panigrahi, V. Ravikumar Pandi, and S. Das, “Adaptive Particle Swarm Optimization Approach for
 * Static and Dynamic Economic Load Dispatch,” Energy Conversion and Management, vol. 49, no. 6, pp. 1407–1415, 2008.
 */
public class RankBasedInertiaStrategy implements SwarmAdaptationStrategy {

    protected double minValue;
    protected double maxValue;
    protected Arrangement<Particle> arrangement;

    public RankBasedInertiaStrategy(){
        minValue = 0.4;
        maxValue = 0.9;
        arrangement = new SortedArrangement<>();
    }

    @Override
    public void adapt(PSO algorithm) {

        Iterable<Particle> ordering = orderParticles(algorithm);
        Iterator<Particle> iterator = ordering.iterator();
        int particles = algorithm.getTopology().length();
        int rank = particles - 1; //start at highest rank as the particles are ordered from worst to best

        SelfAdaptiveParticle p;
        while(iterator.hasNext()){
            p = (SelfAdaptiveParticle) iterator.next();
            double inertia = minValue + ((maxValue - minValue) * rank)/(double)(particles - 1);
            p.setInertiaWeight(ConstantControlParameter.of(inertia));

            rank--; //decrement rank for next particle
        }
    }

    @Override
    public RankBasedInertiaStrategy getClone() {
        return this;
    }

    private Iterable<Particle> orderParticles(PSO algorithm){
        return arrangement.arrange(algorithm.getTopology());
    }

}
