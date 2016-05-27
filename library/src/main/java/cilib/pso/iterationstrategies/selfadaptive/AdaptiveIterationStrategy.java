/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.entity.Property;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.adaptationstrategies.AlgorithmAdaptationStrategy;

public class AdaptiveIterationStrategy extends AbstractIterationStrategy<PSO> {

    protected AlgorithmAdaptationStrategy adaptationStrategy;
    protected IterationStrategy<PSO> delegate;

    public AdaptiveIterationStrategy(){
        delegate = new SynchronousIterationStrategy();
        adaptationStrategy = new AlgorithmAdaptationStrategy() {
            @Override
            public void adapt(PSO algorithm) {
                //do nothing
            }

            @Override
            public AlgorithmAdaptationStrategy getClone() {
                return null;
            }
        };
    }

    @Override
    public AbstractIterationStrategy<PSO> getClone() {
        return null;
    }

    @Override
    public void performIteration(PSO algorithm) {

        for(Particle p : algorithm.getTopology()){
            if(p instanceof SelfAdaptiveParticle){
                SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
                sp.put(Property.PREVIOUS_PARAMETERS, sp.getParameterSet().asVector());
            }
        }

        adaptationStrategy.adapt(algorithm);

        delegate.performIteration(algorithm);
    }


    public AlgorithmAdaptationStrategy getAdaptationStrategy() {
        return adaptationStrategy;
    }

    public void setAdaptationStrategy(AlgorithmAdaptationStrategy adaptationStrategy) {
        this.adaptationStrategy = adaptationStrategy;
    }

    public IterationStrategy<PSO> getDelegate() {
        return delegate;
    }

    public void setDelegate(IterationStrategy<PSO> delegate) {
        this.delegate = delegate;
    }
}
