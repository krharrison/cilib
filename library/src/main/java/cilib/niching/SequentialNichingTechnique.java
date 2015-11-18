/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.niching;

import java.util.Collections;
import java.util.List;

import cilib.algorithm.AbstractAlgorithm;
import cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.ec.EC;
import cilib.entity.Entity;
import cilib.problem.DeratingOptimisationProblem;
import cilib.problem.Problem;
import cilib.problem.solution.OptimisationSolution;
import cilib.type.types.container.Vector;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class SequentialNichingTechnique<E extends Entity> extends SinglePopulationBasedAlgorithm<E> {
    private SinglePopulationBasedAlgorithm algorithm;
    private ControlParameter threshold;
    protected List<OptimisationSolution> solutions;

    public SequentialNichingTechnique() {
        this.algorithm = new EC();
        this.threshold = ConstantControlParameter.of(0);
        this.solutions = Lists.<OptimisationSolution>newLinkedList();
    }

    public SequentialNichingTechnique(SequentialNichingTechnique copy) {
        this.algorithm = copy.algorithm.getClone();
        this.threshold = copy.threshold.getClone();
        this.solutions = Lists.<OptimisationSolution>newLinkedList(copy.solutions);
    }

    @Override
    public SequentialNichingTechnique getClone() {
        return new SequentialNichingTechnique(this);
    }

    @Override
    public void algorithmInitialisation() {
        //algorithm.setOptimisationProblem(optimisationProblem);
    }

    @Override
    protected void algorithmIteration() {
        AbstractAlgorithm alg = algorithm.getClone();
        alg.setOptimisationProblem(optimisationProblem);
        alg.performInitialisation();

        while (!alg.isFinished()) {
            alg.performIteration();
        }

        OptimisationSolution best = alg.getBestSolution();
        ((DeratingOptimisationProblem) optimisationProblem).addSolution((Vector) best.getPosition());

        if (best.getFitness().getValue() > threshold.getParameter()) {
            solutions.add(best);
        }
    }

    @Override
    public fj.data.List<E> getTopology() {
        return algorithm.getTopology();
    }

    @Override
    public OptimisationSolution getBestSolution() {
        return Collections.max(solutions);
    }

    @Override
    public Iterable<OptimisationSolution> getSolutions() {
        return solutions;
    }

    @Override
    public void setInitialisationStrategy(PopulationInitialisationStrategy initialisationStrategy) {
        algorithm.setInitialisationStrategy(initialisationStrategy);
    }

    @Override
    public PopulationInitialisationStrategy getInitialisationStrategy() {
        return algorithm.getInitialisationStrategy();
    }

    @Override
    public void setOptimisationProblem(Problem problem) {
        Preconditions.checkArgument(problem instanceof DeratingOptimisationProblem,
                "SequentialNiching can only be used with DeratingOptimisationProblem.");
        optimisationProblem = problem;
    }

    public void setAlgorithm(SinglePopulationBasedAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public SinglePopulationBasedAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setThreshold(ControlParameter threshold) {
        this.threshold = threshold;
    }

    public ControlParameter getThreshold() {
        return threshold;
    }
}
