package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.IterationStrategy;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.selfadaptive.adaptationstrategies.RandomRegenerationAdaptationStrategy;
import cilib.pso.selfadaptive.adaptationstrategies.SwarmAdaptationStrategy;
import cilib.pso.selfadaptive.detectionstrategies.swarm.SwarmUpdateDetectionStrategy;

public class HomogeneousSAPSOIterationStrategy implements IterationStrategy<PSO>{
    protected IterationStrategy<PSO> iterationStrategy;
    protected SwarmAdaptationStrategy adaptationStrategy;
    protected SwarmUpdateDetectionStrategy detectionStrategy;

    public HomogeneousSAPSOIterationStrategy(){
        iterationStrategy = new SynchronousIterationStrategy();
        adaptationStrategy = new RandomRegenerationAdaptationStrategy();
    }

    public HomogeneousSAPSOIterationStrategy(HomogeneousSAPSOIterationStrategy copy){
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.adaptationStrategy = copy.adaptationStrategy.getClone();
    }

    @Override
    public IterationStrategy<PSO> getClone() {
        return new HomogeneousSAPSOIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        if(detectionStrategy.detect(algorithm))
            adaptationStrategy.adapt(algorithm);

        iterationStrategy.performIteration(algorithm);
    }

    public void setAdaptationStrategy(SwarmAdaptationStrategy strategy){
        this.adaptationStrategy = strategy;
    }

    public void setDetectionStrategy(SwarmUpdateDetectionStrategy strategy){
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
