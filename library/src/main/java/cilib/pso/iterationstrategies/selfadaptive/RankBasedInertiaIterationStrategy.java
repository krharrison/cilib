/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.iterationstrategies.selfadaptive;

import java.util.Iterator;

import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.controlparameter.ConstantControlParameter;
import cilib.pso.PSO;
import cilib.pso.behaviour.StandardParticleBehaviour;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.velocityprovider.SelfAdaptiveVelocityProvider;
import cilib.pso.particle.Particle;
import cilib.util.selection.arrangement.Arrangement;
import cilib.util.selection.arrangement.SortedArrangement;

public class RankBasedInertiaIterationStrategy extends AbstractIterationStrategy<PSO>{

    protected IterationStrategy<PSO> delegate;
    protected Arrangement<Particle> arrangement;

    protected double inertiaMin;
    protected double inertiaMax;

    public RankBasedInertiaIterationStrategy(){
        super();
        arrangement = new SortedArrangement<Particle>();
        delegate = new SynchronousIterationStrategy();
        inertiaMin = 0.1;
        inertiaMax = 0.7;
    }

    public RankBasedInertiaIterationStrategy(RankBasedInertiaIterationStrategy copy){
        super(copy);
        this.arrangement = copy.arrangement;
        this.delegate = copy.delegate.getClone();
    }

    @Override
    public RankBasedInertiaIterationStrategy getClone() {
        return new RankBasedInertiaIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {

        Iterable<Particle> ordering = orderParticles(algorithm);
        Iterator<Particle> iterator = ordering.iterator();
        int rank = 1;
        int particles = algorithm.getTopology().length();

        SelfAdaptiveParticle p;
        while(iterator.hasNext()){
            p = (SelfAdaptiveParticle) iterator.next();
            double inertia = inertiaMin + (inertiaMax - inertiaMin) * (particles - rank)/(double)(particles - 1);
            p.setInertiaWeight(ConstantControlParameter.of(inertia));

            rank++; //increment rank for next particle
        }

        delegate.performIteration(algorithm);
    }

    private Iterable<Particle> orderParticles(PSO algorithm){
        return arrangement.arrange(algorithm.getTopology());
    }

    public void setDelegate(IterationStrategy<PSO> delegate){
        this.delegate = delegate;
    }

    public void setInertiaMin(double inertiaMin){
        this.inertiaMin = inertiaMin;
    }

    public void setInertiaMax(double inertiaMax){
        this.inertiaMax = inertiaMax;
    }
}