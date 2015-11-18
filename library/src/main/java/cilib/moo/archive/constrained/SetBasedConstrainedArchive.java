/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.moo.archive.constrained;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import cilib.algorithm.AbstractAlgorithm;
import cilib.algorithm.Algorithm;
import cilib.moo.archive.Archive;
import cilib.problem.MOOptimisationProblem;
import cilib.problem.solution.OptimisationSolution;
import cilib.util.selection.Selection;
import cilib.util.selection.recipes.DistanceBasedElitistSelector;
import cilib.util.selection.recipes.Selector;

/**
 * <p> A constrained set-driven {@link Archive} implementation. It makes use of
 * a {@link Selection} to determine which solution from the archive will be
 * selected next for removal if the archive grows larger than the capacity. </p>
 *
 */
public class SetBasedConstrainedArchive extends ConstrainedArchive {

    private Set<OptimisationSolution> solutions;
    private Selector<OptimisationSolution> pruningSelection;

    public SetBasedConstrainedArchive() {
        this.solutions = Sets.newLinkedHashSet();
        this.pruningSelection = new DistanceBasedElitistSelector<OptimisationSolution>();
    }

    public SetBasedConstrainedArchive(SetBasedConstrainedArchive copy) {
        super(copy);
        this.solutions = Sets.newLinkedHashSet();
        for (OptimisationSolution solution : copy.solutions) {
            this.solutions.add(solution.getClone());
        }
        this.pruningSelection = copy.pruningSelection;
    }

    public void setPruningSelection(Selector<OptimisationSolution> pruningSelection) {
        this.pruningSelection = pruningSelection;
    }

    public Selector<OptimisationSolution> getPruningSelection() {
        return this.pruningSelection;
    }

    @Override
    public boolean dominates(OptimisationSolution candidateSolution) {
        Algorithm populationBasedAlgorithm = AbstractAlgorithm.getAlgorithmList().index(0);
        MOOptimisationProblem mooProblem = ((MOOptimisationProblem)populationBasedAlgorithm.getOptimisationProblem());

        for (OptimisationSolution archiveSolution : this.solutions) {
            if (mooProblem.getFitness(archiveSolution.getPosition()).compareTo(mooProblem.getFitness(candidateSolution.getPosition())) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isDominatedBy(OptimisationSolution candidateSolution) {
        Algorithm populationBasedAlgorithm = AbstractAlgorithm.getAlgorithmList().index(0);
        MOOptimisationProblem mooProblem = ((MOOptimisationProblem)populationBasedAlgorithm.getOptimisationProblem());

        for (OptimisationSolution archiveSolution : this.solutions) {
            if (mooProblem.getFitness(candidateSolution.getPosition()).compareTo(mooProblem.getFitness(archiveSolution.getPosition())) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<OptimisationSolution> getDominant(OptimisationSolution candidateSolution) {
        Algorithm populationBasedAlgorithm = AbstractAlgorithm.getAlgorithmList().index(0);
        MOOptimisationProblem mooProblem = ((MOOptimisationProblem)populationBasedAlgorithm.getOptimisationProblem());

        List<OptimisationSolution> dominantSolutions = Lists.newLinkedList();
        for (OptimisationSolution archiveSolution : this.solutions) {
            if (mooProblem.getFitness(archiveSolution.getPosition()).compareTo(mooProblem.getFitness(candidateSolution.getPosition())) > 0) {
                dominantSolutions.add(archiveSolution);
            }
        }
        return dominantSolutions;
    }

    @Override
    public Collection<OptimisationSolution> getDominated(OptimisationSolution candidateSolution) {
        Algorithm populationBasedAlgorithm = AbstractAlgorithm.getAlgorithmList().index(0);
        MOOptimisationProblem mooProblem = ((MOOptimisationProblem)populationBasedAlgorithm.getOptimisationProblem());

        List<OptimisationSolution> dominatedSolutions = Lists.newLinkedList();
        for (OptimisationSolution archiveSolution : this.solutions) {
            if (mooProblem.getFitness(candidateSolution.getPosition()).compareTo(mooProblem.getFitness(archiveSolution.getPosition())) > 0) {
                dominatedSolutions.add(archiveSolution);
            }
        }
        return dominatedSolutions;
    }

    @Override
    protected void prune() {
        // If the archive size is greater than the capacity, select a group of solutions and remove them from the archive.
        int numSolutionsToRemove = size() - getCapacity();
        for (int i = 0; i < numSolutionsToRemove; ++i) {
            OptimisationSolution solutionToRemove = this.pruningSelection.on(this).select();
            remove(solutionToRemove);
        }
    }

    @Override
    public boolean addToStructure(OptimisationSolution optimisationSolution) {
        return this.solutions.add(optimisationSolution);
    }

    @Override
    protected Collection<OptimisationSolution> delegate() {
        return this.solutions;
    }
}
