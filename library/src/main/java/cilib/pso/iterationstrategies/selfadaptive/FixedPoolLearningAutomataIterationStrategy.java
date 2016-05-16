/**
 *         __  __
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
import cilib.pso.selfadaptive.ParameterSet;
import cilib.pso.selfadaptive.detectionstrategies.swarm.AlwaysTrueDetectionStrategy;
import cilib.pso.selfadaptive.detectionstrategies.swarm.SwarmUpdateDetectionStrategy;
import cilib.pso.selfadaptive.parametersetgenerator.ConvergentParameterSetGenerator;
import cilib.pso.selfadaptive.parametersetgenerator.ParameterSetGenerator;
import cilib.util.selection.recipes.RouletteWheelSelector;
import cilib.util.selection.weighting.ParameterSetFitnessWeighting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Uses a learning automata to select parameter sets for the swarm.
 *
 * Heavily inspired by:
 * A. B. Hashemi and M. R. Meybodi, “Adaptive Parameter Selection Scheme for PSO: A Learning Automata Approach,”
 * 14th International CSI Computer Conf., vol. 1, no. 1, pp. 403–411, 2009.
 */

public class FixedPoolLearningAutomataIterationStrategy implements IterationStrategy<PSO> {
    protected IterationStrategy<PSO> delegate;
    protected RouletteWheelSelector<ParameterSet> selector;
    protected List<ParameterSet> parameters;
    protected SwarmUpdateDetectionStrategy detectionStrategy;
    protected ParameterSetGenerator generator;
    protected ParameterSetFitnessWeighting weighting;
    protected int poolSize;
    protected double reward;
    protected double penalty;
    protected double successThreshold;

    public FixedPoolLearningAutomataIterationStrategy(){
        delegate = new SynchronousIterationStrategy();
        selector = new RouletteWheelSelector<>();
        parameters = new ArrayList<>();
        detectionStrategy = new AlwaysTrueDetectionStrategy();
        poolSize = 30;
        generator = new ConvergentParameterSetGenerator();
        weighting = new ParameterSetFitnessWeighting();
        reward = 0.01;
        penalty = 0.01;
        successThreshold = 0.5;
    }

    public FixedPoolLearningAutomataIterationStrategy(FixedPoolLearningAutomataIterationStrategy copy){
        this.delegate = copy.delegate.getClone();
        this.selector = copy.selector;
        this.detectionStrategy = copy.detectionStrategy.getClone();
        this.parameters = copy.parameters; //TODO: deep copy?
        this.poolSize = copy.poolSize;
        this.generator = copy.generator.getClone();
        this.weighting = copy.weighting;
        this.reward = copy.reward;
        this.penalty = copy.penalty;
        this.successThreshold = copy.successThreshold;
    }


    @Override
    public FixedPoolLearningAutomataIterationStrategy getClone() {
        return new FixedPoolLearningAutomataIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        Preconditions.checkArgument(poolSize > 0, "There must be elements within the parameter set.");

        ParameterSet current = null;

        //initialize the parameter pool and the weighting scheme
        if(algorithm.getIterations() == 0){
            parameters = Lists.newArrayListWithCapacity(poolSize);
            for(int i = 0; i < poolSize; i++){
                ParameterSet p = generator.generate();
                p.setFitness(1.0 / poolSize); //assumes equal initial probabilities
                parameters.add(p);
            }

            selector.setWeighing(weighting);
        }

        if(detectionStrategy.detect(algorithm)) {
            current = selector.on(parameters).select();

            for (Particle p : algorithm.getTopology()) {
                SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
                sp.setParameterSet(current);
            }
        }

        delegate.performIteration(algorithm);

        if(successful(algorithm)) updateSuccessful(current);
        else updateUnsuccessful(current);
    }

    /**
     * Return true if the ratio of particle improvements is greater than successThreshold
     * @param algorithm
     * @return
     */
    private boolean successful(PSO algorithm){
        int successes = 0;
        for (Particle p : algorithm.getTopology()) {
            if(p.getFitness().compareTo(p.get(Property.PREVIOUS_FITNESS)) > 0) successes++;
        }

        return successes / (double)algorithm.getTopology().length() > successThreshold;
    }

    private void updateSuccessful(ParameterSet current){
        for (ParameterSet p : parameters){
            double currFit = p.getFitness().getValue();
            if(p == current){
                p.setFitness(currFit + reward * (1 - currFit));
            }
            else{
                p.setFitness(currFit * (1 - reward));
            }
        }
    }

    private void updateUnsuccessful(ParameterSet current){
        for (ParameterSet p : parameters){
            double currFit = p.getFitness().getValue();
            if(p == current){
                p.setFitness(currFit * (1 - penalty));
            }
            else{
                p.setFitness((penalty / (poolSize - 1)) + currFit * (1 - penalty));
            }
        }
    }

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return this.delegate.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        this.delegate.setBoundaryConstraint(boundaryConstraint);
    }

    public void setPoolSize(int poolSize){
        this.poolSize = poolSize;
    }

    public void setSuccessThreshold(double successThreshold) {
        this.successThreshold = successThreshold;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public void setGenerator(ParameterSetGenerator generator) {
        this.generator = generator;
    }

    public void setDetectionStrategy(SwarmUpdateDetectionStrategy detectionStrategy) {
        this.detectionStrategy = detectionStrategy;
    }

    public void setDelegate(IterationStrategy<PSO> delegate) {
        this.delegate = delegate;
    }
}
