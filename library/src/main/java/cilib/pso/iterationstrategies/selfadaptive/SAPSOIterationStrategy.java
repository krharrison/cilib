/**
 *      __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.IterationStrategy;
import cilib.entity.Property;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.adaptationstrategies.particle.ParticleAdaptationStrategy;
import cilib.pso.selfadaptive.adaptationstrategies.particle.RandomRegenerationParticleAdaptationStrategy;
import cilib.pso.selfadaptive.detectionstrategies.particle.ParticleUpdateDetectionStrategy;

public class SAPSOIterationStrategy implements IterationStrategy<PSO>{
    protected IterationStrategy<PSO> iterationStrategy;
    protected ParticleAdaptationStrategy adaptationStrategy;
    protected ParticleUpdateDetectionStrategy detectionStrategy;

    public SAPSOIterationStrategy(){
        iterationStrategy = new SynchronousIterationStrategy();
        adaptationStrategy = new RandomRegenerationParticleAdaptationStrategy();
    }

    public SAPSOIterationStrategy(SAPSOIterationStrategy copy){
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.adaptationStrategy = copy.adaptationStrategy.getClone();
    }

    @Override
    public IterationStrategy<PSO> getClone() {
        return new SAPSOIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        for(Particle p : algorithm.getTopology()) {
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            p.put(Property.PREVIOUS_PARAMETERS, sp.getParameterSet().asVector());
            if (detectionStrategy.detect(p)) {
                adaptationStrategy.adapt(p, algorithm);
            }
        }

        iterationStrategy.performIteration(algorithm);
    }

    public void setAdaptationStrategy(ParticleAdaptationStrategy strategy){
        this.adaptationStrategy = strategy;
    }

    public void setDetectionStrategy(ParticleUpdateDetectionStrategy strategy){
        this.detectionStrategy = strategy;
    }

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return iterationStrategy.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        iterationStrategy.setBoundaryConstraint(boundaryConstraint);
    }
}
