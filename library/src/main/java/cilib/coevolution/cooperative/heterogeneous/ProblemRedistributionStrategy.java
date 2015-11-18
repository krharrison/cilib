/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.coevolution.cooperative.heterogeneous;

import java.util.List;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.coevolution.cooperative.ContextEntity;
import cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import cilib.problem.Problem;
import cilib.type.types.container.Vector;
import cilib.util.Cloneable;

/**
 * This interface defines a problem re-distribution strategy. This strategy is
 * used to re-distribute a problem amongst a number of participating
 * {@link PopulationBasedAlgorithm}s in a {@link HeterogeneousCooperativeAlgorithm}.
 */
public interface ProblemRedistributionStrategy extends Cloneable {
    /**
     * Re-distribute the given problem amongst the participating
     * {@link PopulationBasedAlgorithm}s.
     *
     * @param populations           A {@link List} of participating {@link PopulationBasedAlgorithm}s.
     * @param problem               The {@link Problem} that is being optimised.
     * @param distributionStrategy  The {@link CooperativeCoevolutionAlgorithm}'s
     *                              original {@link ProblemDistributionStrategy},
     *                              which may be used to recalculate the distribution.
     * @param context               The current {@link ContextEntity} of the
     *                              {@link CooperativeCoevolutionAlgorithm}.
     */
    void redistributeProblem(List<SinglePopulationBasedAlgorithm> populations, Problem problem, ProblemDistributionStrategy distributionStrategy, Vector context);

    /**
     * {@inheritDoc}
     */
    @Override
    ProblemRedistributionStrategy getClone();
}
