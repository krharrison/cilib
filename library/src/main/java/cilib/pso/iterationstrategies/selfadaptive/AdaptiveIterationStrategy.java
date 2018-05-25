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
import cilib.pso.selfadaptive.adaptationstrategies.DoNothingAdaptationStrategy;
import cilib.pso.selfadaptive.adaptationstrategies.SwarmAdaptationStrategy;
import cilib.pso.selfadaptive.detectionstrategies.swarm.AlwaysTrueDetectionStrategy;
import cilib.pso.selfadaptive.detectionstrategies.swarm.SwarmUpdateDetectionStrategy;
import cilib.pso.selfadaptive.parameterfitnessadjustmentstrategies.ParameterFitnessAdjustmentStrategy;
import cilib.pso.selfadaptive.parameterfitnessadjustmentstrategies.SuccessfulAdjustmentStrategy;

public class AdaptiveIterationStrategy extends AbstractIterationStrategy<PSO> {

    protected SwarmAdaptationStrategy adaptationStrategy;
    protected SwarmUpdateDetectionStrategy detectionStrategy;
    protected IterationStrategy<PSO> delegate;
    protected ParameterFitnessAdjustmentStrategy parameterFitnessAdjustmentStrategy;

    public AdaptiveIterationStrategy(){
        delegate = new SynchronousIterationStrategy();
        adaptationStrategy = new DoNothingAdaptationStrategy();
        detectionStrategy = new AlwaysTrueDetectionStrategy();
        parameterFitnessAdjustmentStrategy = new SuccessfulAdjustmentStrategy();
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

        if(detectionStrategy.detect(algorithm)) {
            adaptationStrategy.adapt(algorithm);
        }

        delegate.performIteration(algorithm);

        parameterFitnessAdjustmentStrategy.adjustFitness(algorithm);
    }


    public SwarmAdaptationStrategy getAdaptationStrategy() {
        return adaptationStrategy;
    }

    public void setAdaptationStrategy(SwarmAdaptationStrategy adaptationStrategy) {
        this.adaptationStrategy = adaptationStrategy;
    }

    public void setDetectionStrategy(SwarmUpdateDetectionStrategy detectionStrategy) {
        this.detectionStrategy = detectionStrategy;
    }

    public IterationStrategy<PSO> getDelegate() {
        return delegate;
    }

    public void setDelegate(IterationStrategy<PSO> delegate) {
        this.delegate = delegate;
    }

    public void setParameterFitnessAdjustmentStrategy(ParameterFitnessAdjustmentStrategy strategy){
        this.parameterFitnessAdjustmentStrategy = strategy;
    }
}
