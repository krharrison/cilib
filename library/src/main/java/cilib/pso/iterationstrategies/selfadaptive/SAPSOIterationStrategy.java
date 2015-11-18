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
import cilib.entity.Property;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.pso.PSO;
import cilib.pso.behaviour.StandardParticleBehaviour;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.selfadaptive.adaptationstrategies.AlgorithmAdaptationStrategy;
import cilib.pso.selfadaptive.adaptationstrategies.PSOAdaptationStrategy;
import cilib.pso.velocityprovider.SelfAdaptiveVelocityProvider;

public class SAPSOIterationStrategy implements IterationStrategy<PSO>{
    protected IterationStrategy<PSO> iterationStrategy;
    protected AlgorithmAdaptationStrategy adaptationStrategy;
    //protected ParameterUpdateTriggerDetectionStrategy detectionStrategy;
    protected int period;

    public SAPSOIterationStrategy(){
        iterationStrategy = new SynchronousIterationStrategy();
        adaptationStrategy = new PSOAdaptationStrategy();
        period = 50;
    }

    public SAPSOIterationStrategy(SAPSOIterationStrategy copy){
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.adaptationStrategy = copy.adaptationStrategy.getClone();
        this.period = copy.period;
    }

    @Override
    public IterationStrategy<PSO> getClone() {
        return new SAPSOIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        int iteration = AbstractAlgorithm.get().getIterations();

        //adapt and reset fitness if time to do so
        if(iteration > 0 && iteration % period == 0){
            adaptationStrategy.adapt(algorithm);
            for(Particle p : algorithm.getTopology()){
                StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
                SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider)behaviour.getVelocityProvider();
                provider.getParameterSet().resetFitness();
            }
        }

        iterationStrategy.performIteration(algorithm);

        for(Particle p : algorithm.getTopology()){
            //if the particle improved in fitness, increment the fitness of the parameters by 1
            if(p.getFitness().compareTo(p.get(Property.PREVIOUS_FITNESS)) > 0){
                StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
                SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider)behaviour.getVelocityProvider();
                provider.getParameterSet().incrementFitness(1);
            }
        }
    }

    public void setAdaptationStrategy(AlgorithmAdaptationStrategy strategy){
        this.adaptationStrategy = strategy;
    }

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return iterationStrategy.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        iterationStrategy.setBoundaryConstraint(boundaryConstraint);

    }

    public void setPeriod(int period){
        this.period = period;
    }

}
