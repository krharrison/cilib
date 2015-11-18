/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.coevolution.cooperative;

import java.util.Arrays;
import java.util.List;
import cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.coevolution.CoevolutionAlgorithm;
import cilib.coevolution.cooperative.contextupdate.ContextUpdateStrategy;
import cilib.coevolution.cooperative.contextupdate.SelectiveContextUpdateStrategy;
import cilib.coevolution.cooperative.contributionselection.ContributionSelectionStrategy;
import cilib.coevolution.cooperative.contributionselection.TopologyBestContributionSelectionStrategy;
import cilib.coevolution.cooperative.contributionselection.ZeroContributionSelectionStrategy;
import cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import cilib.coevolution.cooperative.problemdistribution.PerfectSplitDistributionStrategy;
import cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import cilib.problem.solution.OptimisationSolution;
import cilib.type.types.container.Vector;

/**
 * This class forms the basis for any co-operative coevolution optimisation
 * algorithm implementations. A cooperative algorithm is an algorithm that
 * maintains a context solution and a list of participating algorithms. Each
 * participating algorithm optimizes only a subsection of the problem, and
 * fitness values are computed by inserting an entity's solution into the
 * current context vector before it is evaluated. The context vector is simply
 * the concatenation of the best solutions from each participating population.
 * <p>
 * Any algorithm that wishes to participate in a co-operative optimisation
 * algorithm must implement the {@link ParticipatingAlgorithm} interface. This class
 * also implements {@link ParticipatingAlgorithm}, meaning that co-operative
 * algorithms can be composed of co-operative algorithms again.
 * <p>
 * References:
 * <p>
 * <ul>
 * <li> M. Potter and K.D. Jong, "A Cooperative Coevolutionary approach to function optimization,"
 * in Proceedings of the Third conference on Parallel Problem Solving from Nature, pp. 249-257,
 * Springer-Verlag, 1994.
 * </li>
 * <li> F. van den Bergh and A. Engelbrecht, "A cooperative approach to particle swarm optimization,"
 * IEEE Transactions on Evolutionary Computation, vol. 8, no. 3, pp 225-239, 2004.
 * </li>
 * </ul>
 *
 * TODO: test this class.
 *
 */
public class CooperativeCoevolutionAlgorithm extends MultiPopulationBasedAlgorithm implements ParticipatingAlgorithm, CoevolutionAlgorithm {

    private static final long serialVersionUID = 3351497412601778L;
    protected ContextEntity context;
    protected ProblemDistributionStrategy problemDistribution;
    protected ContributionSelectionStrategy contributionSelection;
    protected ContextUpdateStrategy contextUpdate;

    /**
     * Constructor
     */
    public CooperativeCoevolutionAlgorithm() {
        context = new ContextEntity();
        problemDistribution = new PerfectSplitDistributionStrategy();
        contributionSelection = new TopologyBestContributionSelectionStrategy();
        contextUpdate = new SelectiveContextUpdateStrategy();
    }

    /**
     * Copy constructor
     * @param copy The {@linkplain CooperativeCoevolutionAlgorithm} to make a copy of.
     */
    public CooperativeCoevolutionAlgorithm(CooperativeCoevolutionAlgorithm copy) {
        super(copy);
        context = copy.context.getClone();
        problemDistribution = copy.problemDistribution;
        contributionSelection = copy.contributionSelection.getClone();
        contextUpdate = copy.contextUpdate.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmInitialisation() {
        /*use the problem distribution class to allocate segments of the problem to the different algorithms, this class gives each sub population
        a wrapped problem, which contains the original problem and the current context vector*/
        context.initialise(optimisationProblem);
        problemDistribution.performDistribution(subPopulationsAlgorithms, optimisationProblem, context.getPosition());

        //Initialise each sub population, and add the randomised solution vector from each population to the current context.
        for (SinglePopulationBasedAlgorithm algorithm : subPopulationsAlgorithms) {
            CooperativeCoevolutionProblemAdapter problem = (CooperativeCoevolutionProblemAdapter) algorithm.getOptimisationProblem();
            algorithm.performInitialisation();
            context.copyFrom((Vector) algorithm.getBestSolution().getPosition(), problem.getProblemAllocation());
        }
        context.updateFitness(context.getBehaviour().getFitnessCalculator().getFitness(context));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void algorithmIteration() {
        //iterate through each algorithm
        algorithmIterator.setAlgorithms(subPopulationsAlgorithms);
        while (algorithmIterator.hasNext()) {
            //get the optimisation problem from the algorithm
            CooperativeCoevolutionProblemAdapter problem = (CooperativeCoevolutionProblemAdapter) algorithmIterator.next().getOptimisationProblem();
            //update the context solution to point to the current context
            problem.updateContext(context.getPosition());
            //perform an iteration of the sub population algorithm
            algorithmIterator.current().performIteration();
            //select the contribution from the population
            contextUpdate.updateContext(context, ((ParticipatingAlgorithm) algorithmIterator.current()).getContributionSelectionStrategy().getContribution(algorithmIterator.current()), problem.getProblemAllocation());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationSolution getBestSolution() {
        return new OptimisationSolution(Vector.copyOf(context.getPosition()), context.getFitness());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CooperativeCoevolutionAlgorithm getClone() {
        return new CooperativeCoevolutionAlgorithm(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        return Arrays.asList(getBestSolution());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPopulationBasedAlgorithm(SinglePopulationBasedAlgorithm algorithm) {
        // TODO: There should be a better way to perfrom this test, rather than using an instanceof.
        if (((ParticipatingAlgorithm) algorithm).getContributionSelectionStrategy() instanceof ZeroContributionSelectionStrategy) {
            ((ParticipatingAlgorithm) algorithm).setContributionSelectionStrategy(contributionSelection);
        }

        super.addPopulationBasedAlgorithm(algorithm);
    }

    @Override
    public ContributionSelectionStrategy getContributionSelectionStrategy() {
        return contributionSelection;
    }

    @Override
    public void setContributionSelectionStrategy(ContributionSelectionStrategy strategy) {
        contributionSelection = strategy;
    }

    public void setContextUpdate(ContextUpdateStrategy contextUpdate) {
        this.contextUpdate = contextUpdate;
    }

    public void setProblemDistribution(ProblemDistributionStrategy problemDistribution) {
        this.problemDistribution = problemDistribution;
    }

    public ContextEntity getContext() {
        return context;
    }
}
