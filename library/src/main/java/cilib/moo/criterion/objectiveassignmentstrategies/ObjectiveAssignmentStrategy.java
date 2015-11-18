/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.moo.criterion.objectiveassignmentstrategies;

import java.util.List;
import cilib.algorithm.population.MultiPopulationCriterionBasedAlgorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.problem.MOOptimisationProblem;
import cilib.util.Cloneable;

/**
 * <p>
 * Used by {@link MultiPopulationCriterionBasedAlgorithm} to assign the different sub-objectives
 * in a {@link MOOptimisationProblem} to specific {@link PopulationBasedAlgorithm}s.
 * </p>
 *
 */
public interface ObjectiveAssignmentStrategy extends Cloneable {

    @Override
    public ObjectiveAssignmentStrategy getClone();

    public void assignObjectives(MOOptimisationProblem problem, List<SinglePopulationBasedAlgorithm> populations);
}
