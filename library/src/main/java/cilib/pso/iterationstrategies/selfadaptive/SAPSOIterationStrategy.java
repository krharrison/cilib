/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.AbstractAlgorithm;
import cilib.algorithm.population.IterationStrategy;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.selfadaptive.adaptationstrategies.AdaptationStrategy;
import cilib.pso.selfadaptive.adaptationstrategies.RandomRegenerationAdaptationStrategy;
import cilib.pso.selfadaptive.detectionstrategies.particle.ParticleUpdateDetectionStrategy;

public class SAPSOIterationStrategy implements IterationStrategy<PSO>{
    protected IterationStrategy<PSO> iterationStrategy;
    protected AdaptationStrategy adaptationStrategy;
    protected ParticleUpdateDetectionStrategy detectionStrategy;
    //protected int period;

    public SAPSOIterationStrategy(){
        iterationStrategy = new SynchronousIterationStrategy();
        adaptationStrategy = new RandomRegenerationAdaptationStrategy();
        //period = 50;
    }

    public SAPSOIterationStrategy(SAPSOIterationStrategy copy){
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.adaptationStrategy = copy.adaptationStrategy.getClone();
        //this.period = copy.period;
    }

    @Override
    public IterationStrategy<PSO> getClone() {
        return new SAPSOIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        int iteration = AbstractAlgorithm.get().getIterations();

        //adapt and reset fitness if time to do so
        for(Particle p : algorithm.getTopology()) {
            if (detectionStrategy.detect(p)) {
                adaptationStrategy.adapt(p, algorithm);
                //for (Particle p : algorithm.getTopology()) {
                //    ((SelfAdaptiveParticle) p).getParameterSet().resetFitness();
                //}
            }
        }

        iterationStrategy.performIteration(algorithm);

        //for(Particle p : algorithm.getTopology()){
            //if the particle improved in fitness, increment the fitness of the parameters by 1
        //    if(p.getFitness().compareTo(p.get(Property.PREVIOUS_FITNESS)) > 0){
        //        ((SelfAdaptiveParticle)p).getParameterSet().incrementFitness(1);
        //    }
        //}
    }

    public void setAdaptationStrategy(AdaptationStrategy strategy){
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

    //public void setPeriod(int period){
    //    this.period = period;
    //}

}
