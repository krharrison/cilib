/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.algorithm;

import cilib.problem.Problem;
import cilib.problem.solution.OptimisationSolution;
import cilib.util.Cloneable;

/**
 * Base interface defining all {@code Algorithm} classes.
 */
public interface Algorithm extends Runnable, Cloneable {

    /**
     * Perform the actions of the current {@linkplain Algorithm} for a single iteration.
     */
    void performIteration();

    /**
     * Perform the needed initialization required before the execution of the algorithm
     * starts.
     */
    void performInitialisation();

    /**
     * Obtain the best current solution.
     * @return The {@code OptimisationSolution} representing the best solution.
     */
    OptimisationSolution getBestSolution();

    /**
     * Obtain the collection of best solutions. This result does not actually make sense in normal
     * {@code PopulationBasedAlgorithm}s, but rather in a MultiObjective optimization.
     * @return An {@code Iterable} containing the solutions.
     */
    Iterable<OptimisationSolution> getSolutions();

    /**
     * Returns the number of iterations that have been performed by the algorithm.
     * @return The number of iterations.
     *
     * Not Needed>??>????????????
     */
    int getIterations();

    /**
     * Set the optimization {@link Problem} to be solved.
     * <p>
     * By default, the problem is {@code null}<code>null</code>. It is necessary
     * to set the optimization problem before calling {@link #performInitialisation()}.
     *
     * @param problem an implementation of the {@link Problem} interface.
     */
    void setOptimisationProblem(Problem problem);

    /**
     * Get the specified {@linkplain Problem}.
     * @return The specified {@linkplain Problem}.
     */
    Problem getOptimisationProblem();

    boolean isFinished();
}
