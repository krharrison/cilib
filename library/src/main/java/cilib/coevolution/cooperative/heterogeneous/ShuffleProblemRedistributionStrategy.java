/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.coevolution.cooperative.heterogeneous;

import java.util.ArrayList;
import java.util.List;
import cilib.algorithm.iterator.RandomAlgorithmIterator;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import cilib.problem.Problem;
import cilib.type.types.container.Vector;

/**
 * This {@link ProblemRedistributionStrategy} works by shuffling the problems of the participating {@link PopulationBasedAlgorithm}'s. A list of
 * all the current participating algorithm's {@link CooperativeCoevolutionProblemAdapter}s is created, and then randomly re-assigned to the list
 * of algorithms.
 *
 */
public class ShuffleProblemRedistributionStrategy implements
        ProblemRedistributionStrategy {
    private static final long serialVersionUID = 1852933965918949622L;

    /**
     * {@inheritDoc}
     */
    public void redistributeProblem(List<SinglePopulationBasedAlgorithm> populations, Problem problem, ProblemDistributionStrategy distributionStrategy, Vector context) {
        List<Problem> problems = new ArrayList<Problem>();
        for(SinglePopulationBasedAlgorithm algorithm: populations){
            problems.add(algorithm.getOptimisationProblem().getClone());
        }

        RandomAlgorithmIterator<SinglePopulationBasedAlgorithm> iterator = new RandomAlgorithmIterator<>(populations);
        while(iterator.hasNext()){
            iterator.next().setOptimisationProblem(problems.get(0));
            problems.remove(0);
        }
    }

    @Override
    public ShuffleProblemRedistributionStrategy getClone(){
        return new ShuffleProblemRedistributionStrategy();
    }

}
