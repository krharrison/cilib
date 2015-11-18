/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.ec;

import java.util.List;

import cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.ec.iterationstrategies.GeneticAlgorithmIterationStrategy;
import cilib.entity.Entity;
import cilib.entity.Property;
import cilib.entity.Topologies;
import cilib.entity.initialisation.InitialisationStrategy;
import cilib.entity.initialisation.NullInitialisationStrategy;
import cilib.problem.solution.OptimisationSolution;

import com.google.common.collect.Lists;

import fj.F;

/**
 * Generic EC skeleton algorithm. The algorithm is altered by defining the
 * appropriate {@linkplain cilib.algorithm.population.IterationStrategy}.
 */
public class EC<I extends Individual> extends SinglePopulationBasedAlgorithm<Individual> {

    private static final long serialVersionUID = -4324446523858690744L;

    private IterationStrategy iterationStrategy;
    private InitialisationStrategy strategyParameterInitialisation;

    /**
     * Create a new instance of {@code EC}.
     */
    public EC() {
        this.initialisationStrategy = new ClonedPopulationInitialisationStrategy();
        this.initialisationStrategy.setEntityType(new Individual());
        this.iterationStrategy = new GeneticAlgorithmIterationStrategy();
        this.strategyParameterInitialisation = new NullInitialisationStrategy();
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public EC(EC copy) {
        super(copy);
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.strategyParameterInitialisation = copy.strategyParameterInitialisation.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EC getClone() {
        return new EC(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmInitialisation() {
        topology = fj.data.List.iterableList(initialisationStrategy.<Individual>initialise(optimisationProblem))
                .map(new F<Individual, Individual>() {
                    @Override
                    public Individual f(Individual i) {
                        i.updateFitness(i.getBehaviour().getFitnessCalculator().getFitness(i));
                        strategyParameterInitialisation.initialise(Property.STRATEGY_PARAMETERS, i);
                        return i;
                    }
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmIteration() {
        iterationStrategy.performIteration(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationSolution getBestSolution() {
        Entity bestEntity = Topologies.getBestEntity(topology);
        OptimisationSolution solution = new OptimisationSolution(bestEntity.getPosition().getClone(), bestEntity.getFitness());

        return solution;
    }

    /**
     * Get the {@linkplain cilib.algorithm.population.IterationStrategy} for the current
     * {@code EC}.
     * @return The current {@linkplain cilib.algorithm.population.IterationStrategy}.
     */
    public IterationStrategy getIterationStrategy() {
        return iterationStrategy;
    }

    /**
     * Set the current {@linkplain cilib.algorithm.population.IterationStrategy}.
     * @param iterationStrategy The value to set.
     */
    public void setIterationStrategy(IterationStrategy iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        return Lists.newArrayList(getBestSolution());
    }

    public InitialisationStrategy<Individual> getStrategyParameterInitialisation() {
        return strategyParameterInitialisation;
    }

    public void setStrategyParameterInitialisation(InitialisationStrategy<Individual> strategyParameterInitialisation) {
        this.strategyParameterInitialisation = strategyParameterInitialisation;
    }
}
