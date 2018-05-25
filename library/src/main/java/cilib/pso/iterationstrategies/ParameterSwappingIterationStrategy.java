package cilib.pso.iterationstrategies;

import cilib.algorithm.population.IterationStrategy;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterSet;

public class ParameterSwappingIterationStrategy implements IterationStrategy<PSO> {

    protected IterationStrategy<PSO> delegate;
    protected ParameterSet parameterSet;
    protected double ratio;

    public ParameterSwappingIterationStrategy(){
        delegate = new SynchronousIterationStrategy();
        parameterSet = ParameterSet.fromValues(0.729844, 1.496180, 1.496180);
        ratio = 0.5;
    }

    @Override
    public IterationStrategy<PSO> getClone() {
        return null;
    }

    @Override
    public void performIteration(PSO algorithm) {

        if(algorithm.getPercentageComplete() >= ratio){
            for(Particle p : algorithm.getTopology()) {
                if (p instanceof SelfAdaptiveParticle) {
                    SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
                    sp.setParameterSet(parameterSet);
                }
            }
        }

        delegate.performIteration(algorithm);
    }

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return delegate.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        delegate.setBoundaryConstraint(boundaryConstraint);
    }

    public void setDelegate(IterationStrategy<PSO> delegate) {
        this.delegate = delegate;
    }

    public void setParameterSet(ParameterSet initialParameters) {
        this.parameterSet = initialParameters;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}
