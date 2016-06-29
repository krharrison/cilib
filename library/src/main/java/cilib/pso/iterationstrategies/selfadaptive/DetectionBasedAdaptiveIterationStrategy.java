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
import cilib.pso.selfadaptive.adaptationstrategies.particle.DoNothingParticleAdaptationStrategy;
import cilib.pso.selfadaptive.adaptationstrategies.particle.ParticleAdaptationStrategy;
import cilib.pso.selfadaptive.detectionstrategies.particle.AlwaysTrueDetectionStrategy;
import cilib.pso.selfadaptive.detectionstrategies.particle.ParticleUpdateDetectionStrategy;

public class DetectionBasedAdaptiveIterationStrategy extends AbstractIterationStrategy<PSO> {

    protected ParticleAdaptationStrategy adaptationStrategy;
    protected ParticleUpdateDetectionStrategy detectionStrategy;
    protected IterationStrategy<PSO> delegate;

    public DetectionBasedAdaptiveIterationStrategy(){
        delegate = new SynchronousIterationStrategy();
        adaptationStrategy = new DoNothingParticleAdaptationStrategy();
        detectionStrategy = new AlwaysTrueDetectionStrategy();
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

            if (detectionStrategy.detect(p)){
                adaptationStrategy.adapt(p, algorithm);
            }
        }

        delegate.performIteration(algorithm);
    }


    public ParticleAdaptationStrategy getAdaptationStrategy() {
        return adaptationStrategy;
    }

    public void setAdaptationStrategy(ParticleAdaptationStrategy adaptationStrategy) {
        this.adaptationStrategy = adaptationStrategy;
    }


    public ParticleUpdateDetectionStrategy getDetectionStrategy() {
        return detectionStrategy;
    }

    public void setDetectionStrategy(ParticleUpdateDetectionStrategy detectionStrategy) {
        this.detectionStrategy = detectionStrategy;
    }


    public IterationStrategy<PSO> getDelegate() {
        return delegate;
    }

    public void setDelegate(IterationStrategy<PSO> delegate) {
        this.delegate = delegate;
    }
}
