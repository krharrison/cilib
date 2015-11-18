/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.ec.iterationstrategies;

import java.util.List;
import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.ec.EC;
import cilib.ec.Individual;
import cilib.entity.Entity;
import cilib.entity.operators.CrossoverOperator;
import cilib.entity.operators.crossover.CrossoverStrategy;
import cilib.entity.operators.crossover.discrete.UniformCrossoverStrategy;
import cilib.entity.operators.mutation.GaussianMutationStrategy;
import cilib.entity.operators.mutation.MutationStrategy;

import com.google.common.collect.Lists;
import cilib.util.selection.Samples;
import cilib.util.selection.recipes.ElitistSelector;
import cilib.util.selection.recipes.Selector;

/**
 * TODO: Complete this javadoc.
 */
public class GeneticAlgorithmIterationStrategy extends AbstractIterationStrategy<EC> {

    private static final long serialVersionUID = -2429984051022079804L;
    private CrossoverOperator crossover;
    private MutationStrategy mutationStrategy;
    private Selector<Individual> populationSelector;

    /**
     * Create an instance of the {@linkplain IterationStrategy}. Default cross-over
     * and mutation operators are {@linkplain UniformCrossoverStrategy} and
     * {@linkplain GaussianMutationStrategy} respectively.
     */
    public GeneticAlgorithmIterationStrategy() {
        this.crossover = new CrossoverOperator();
        this.crossover.setCrossoverStrategy(new UniformCrossoverStrategy());
        this.mutationStrategy = new GaussianMutationStrategy();
        this.populationSelector = new ElitistSelector<>();
    }

    /**
     * Copy constructor. Create an instance that is a copy of the provided instance.
     * @param copy the instance to copy.
     */
    public GeneticAlgorithmIterationStrategy(GeneticAlgorithmIterationStrategy copy) {
        this.crossover = copy.crossover.getClone();
        this.mutationStrategy = copy.mutationStrategy.getClone();
        this.populationSelector = copy.populationSelector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GeneticAlgorithmIterationStrategy getClone() {
        return new GeneticAlgorithmIterationStrategy(this);
    }

    /**
     * {@inheritDoc}
     *
     * @param ec The EC algorithm to perform the iteration on.
     */
    @Override
    public void performIteration(EC ec) {
        fj.data.List<Individual> population = ec.getTopology();
        int populationSize = population.length();

        // Perform crossover: Allow each individual to create an offspring
        List<Individual> crossedOver = Lists.newArrayList();
        for (int i = 0, n = populationSize; i < n; i++) {
            crossedOver.addAll(crossover.crossover(ec.getTopology()));
        }

        // Perform mutation on offspring
        mutationStrategy.mutate(crossedOver);

        // Evaluate the fitness values of the generated offspring
        for (Entity entity : crossedOver) {
            boundaryConstraint.enforce(entity);
            entity.updateFitness(entity.getBehaviour().getFitnessCalculator().getFitness(entity));
        }

        // Perform new population selection
        ec.setTopology(fj.data.List.iterableList(populationSelector
            .on(population.append(fj.data.List.iterableList(crossedOver)))
            .select(Samples.first(populationSize))));
    }

    /**
     * Get the currently specified {@linkplain CrossoverStrategy}.
     * @return The current {@linkplain CrossoverStrategy}.
     */
    public CrossoverOperator getCrossover() {
        return crossover;
    }

    /**
     * Set the current {@linkplain CrossoverStrategy} and reinitialise the operator pipeline.
     * @param crossoverStrategy The {@linkplain CrossoverStrategy} to use.
     */
    public void setCrossover(CrossoverOperator crossoverStrategy) {
        this.crossover = crossoverStrategy;
    }

    /**
     * Get the currently specified {@linkplain MutationStrategy}.
     * @return The current {@linkplain MutationStrategy}.
     */
    public MutationStrategy getMutationStrategy() {
        return mutationStrategy;
    }

    /**
     * Set the current {@linkplain MutationStrategy} and reinitialise the operator pipeline.
     * @param mutationStrategy The {@linkplain MutationStrategy} to use.
     */
    public void setMutationStrategy(MutationStrategy mutationStrategy) {
        this.mutationStrategy = mutationStrategy;
    }

    public Selector<Individual> getPopulationSelector() {
        return populationSelector;
    }

    public void setPopulationSelector(Selector<Individual> populationSelector) {
        this.populationSelector = populationSelector;
    }
}
