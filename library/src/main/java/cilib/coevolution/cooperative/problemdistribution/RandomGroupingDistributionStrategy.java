/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.coevolution.cooperative.problemdistribution;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import cilib.algorithm.Algorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import cilib.coevolution.cooperative.problem.RandomDimensionAllocation;
import cilib.problem.Problem;
import cilib.type.types.container.Vector;
import cilib.util.selection.Samples;
import cilib.util.selection.Selection;
import cilib.util.selection.arrangement.RandomArrangement;

/**
 * This {@linkplain ProblemDistributionStrategy} performs a split by assigning
 * a sequential portion of the varying length, which consists of random
 * dimensions of the problem vector, to each participating
 * {@linkplain PopulationBasedAlgorithm}. Defaults into a split of equal
 * sizes if possible. The order in which the algorithms are assigned
 * is generated randomly.
 */
public class RandomGroupingDistributionStrategy implements
        ProblemDistributionStrategy {

    /**
     * Splits up the given {@link Problem} into
     * sub-problems, where each sub-problem contains a portion of the problem,
     * of non-uniform length, which consists of random dimensions of the problem
     * vector, and assigns them to all the participating {@link Algorithm}s.
     * This implementation assigns a portion of length dimensionality/number of
     * populations + 1 to dimensionality % number of populations of the
     * participating populations. The order in which the algorithms are assigned
     * is generated randomly.
     *
     * @param populations   The list of participating
     *                      {@linkplain PopulationBasedAlgorithm}s.
     * @param problem       The {@linkplain Problem} that needs to be re-distributed.
     * @param context       The context vector maintained by the
     *                      {@linkplain CooperativeCoevolutionAlgorithm}.
     */
    @Override
    public void performDistribution(List<SinglePopulationBasedAlgorithm> populations,
            Problem problem, Vector context) {
        //need to do a completely random split depending on the number of sub populations
        Preconditions.checkArgument(populations.size() >= 2,
                "There should at least be two Cooperating populations in a Cooperative Algorithm");

        List<Integer> dimensions = new ArrayList<Integer>();
        for (int i = 0; i < problem.getDomain().getDimension(); ++i) {
            dimensions.add(i);
        }

        int dimension = problem.getDomain().getDimension() / populations.size();
        int oddDimensions = problem.getDomain().getDimension() % populations.size();
        for (int p = 0; p < populations.size(); ++p) {
            List<Integer> indexList = new ArrayList<Integer>();
            int actualDimension = dimension;
            if (p < oddDimensions) {
                actualDimension++;
            }
            List<Integer> selectedDimensions = Selection.copyOf(dimensions)
                    .orderBy(new RandomArrangement())
                    .select(Samples.first(actualDimension));
            for (Integer d : selectedDimensions) {
                indexList.add(d);
                dimensions.remove(d);
            }
            populations.get(p).setOptimisationProblem(new CooperativeCoevolutionProblemAdapter(problem, new RandomDimensionAllocation(indexList), context));
        }

    }
}
