/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.moo.iterationstrategies;

import java.util.ArrayList;
import java.util.List;
import cilib.algorithm.AbstractAlgorithm;
import cilib.algorithm.Algorithm;
import cilib.algorithm.population.IterationStrategy;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Entity;
import cilib.moo.archive.Archive;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.problem.solution.OptimisationSolution;
import cilib.type.types.Type;
import cilib.type.types.Types;

/**
 * <p>
 * A generic multi-objective {@link IterationStrategy} class that wraps another {@code IterationStrategy}
 * and is responsible for populating the {@link Archive} of Pareto optimal solutions after the execution
 * of the inner {@code IterationStrategy} class.
 * </p>
 *
 *
 * @param <E> The PopulationBasedAlgorithm that will have it's entities' positions added to
 * the archive as potential solutions.
 */
public class ArchivingIterationStrategy<E extends SinglePopulationBasedAlgorithm> implements IterationStrategy<E> {

    private static final long serialVersionUID = 4029628616324259998L;
    private IterationStrategy<SinglePopulationBasedAlgorithm> iterationStrategy;

    public ArchivingIterationStrategy() {
    }

    public ArchivingIterationStrategy(ArchivingIterationStrategy<E> copy) {
        this.iterationStrategy = copy.iterationStrategy.getClone();
    }

    @Override
    public ArchivingIterationStrategy<E> getClone() {
        return new ArchivingIterationStrategy<>(this);
    }

    public void setIterationStrategy(IterationStrategy<SinglePopulationBasedAlgorithm> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    public IterationStrategy<SinglePopulationBasedAlgorithm> getIterationStrategy() {
        return this.iterationStrategy;
    }

    protected void updateArchive(fj.data.List<? extends Entity> population) {
        Algorithm topLevelAlgorithm = AbstractAlgorithm.getAlgorithmList().index(0);
        List<OptimisationSolution> optimisationSolutions = new ArrayList<>();
        for (Entity entity : population) {
            if(Types.isInsideBounds(entity.getPosition())){
                Type solution = entity.getPosition().getClone();
                optimisationSolutions.add(new OptimisationSolution(solution,
                    topLevelAlgorithm.getOptimisationProblem().getFitness(solution)));
            }
        }
        Archive.Provider.get().addAll(optimisationSolutions);
    }

    @Override
    public void performIteration(E algorithm) {
        this.iterationStrategy.performIteration(algorithm);
        updateArchive(algorithm.getTopology());
    }

    public void setArchive(Archive archive) {
        Archive.Provider.set(archive);
    }

    public Archive getArchive() {
        return Archive.Provider.get();
    }

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return this.iterationStrategy.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
