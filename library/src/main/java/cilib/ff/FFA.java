/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.ff;

import java.util.List;

import cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Topologies;
import cilib.entity.comparator.DescendingFitnessComparator;
import cilib.ff.firefly.Firefly;
import cilib.ff.firefly.StandardFirefly;
import cilib.ff.iterationstrategies.StandardFireflyIterationStrategy;
import cilib.problem.solution.OptimisationSolution;

import com.google.common.collect.Lists;

/**
 * <p>
 * An implementation of the standard Firefly algorithm.
 * </p>
 * <p>
 * References:
 * </p>
 * <p>
 * <ul>
 * <li>Yang, Xin-She. "Firefly algorithms for multimodal optimization."
 * Stochastic algorithms: foundations and applications (2009): 169-178.
 * </li>
 * </ul>
 * </p>
 */
public class FFA extends SinglePopulationBasedAlgorithm<Firefly> {

    private IterationStrategy<FFA> iterationStrategy;

    /**
     * Creates a new instance of the <code>Firefly</code> algorithm.
     * All fields are initialised to reasonable defaults.
     */
    public FFA() {
        iterationStrategy = new StandardFireflyIterationStrategy();
        initialisationStrategy = new ClonedPopulationInitialisationStrategy();
        initialisationStrategy.setEntityType(new StandardFirefly());
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public FFA(FFA copy) {
        super(copy);
        this.iterationStrategy = copy.iterationStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FFA getClone() {
        return new FFA(this);
    }

    /**
     * Perform the required initialisation for the algorithm. Create the fireflies
     * and add them to the specified topology.
     */
    @Override
    public void algorithmInitialisation() {
    	topology = fj.data.List.iterableList(initialisationStrategy.<Firefly>initialise(optimisationProblem));

        for (Firefly f : topology) {
            f.updateFitness(f.getBehaviour().getFitnessCalculator().getFitness(f));
        }
    }

    /**
     * Perform the iteration of the Firefly algorithm, use the appropriate <code>IterationStrategy</code>
     * to perform the iteration.
     */
    @Override
    protected void algorithmIteration() {
        iterationStrategy.performIteration(this);
    }

    /**
     * Get the best current solution.
     * @return The <code>OptimisationSolution</code> representing the best solution.
     */
    @Override
    public OptimisationSolution getBestSolution() {
        Firefly bestEntity = Topologies.getBestEntity(topology, new DescendingFitnessComparator<Firefly>());
        return new OptimisationSolution(bestEntity.getPosition(), bestEntity.getFitness());
    }

    /**
     * Get the <code>IterationStrategy</code> of the Firefly algorithm.
     * @return Returns the iterationStrategy.
     */
    public IterationStrategy<FFA> getIterationStrategy() {
        return iterationStrategy;
    }

    /**
     * Set the <code>IterationStrategy</code> to be used.
     * @param iterationStrategy The iterationStrategy to set.
     */
    public void setIterationStrategy(IterationStrategy<FFA> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    /**
     * Get the collection of best solutions.
     * @return The <code>Collection&lt;OptimisationSolution&gt;</code> containing the solutions.
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> solutions = Lists.newLinkedList();
        for (Firefly e : Topologies.getNeighbourhoodBestEntities(topology, neighbourhood, new DescendingFitnessComparator<Firefly>())) {
            solutions.add(new OptimisationSolution(e.getPosition(), e.getFitness()));
        }
        return solutions;
    }
}
