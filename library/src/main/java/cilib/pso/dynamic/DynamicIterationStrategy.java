/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.dynamic;

import cilib.algorithm.population.IterationStrategy;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.pso.PSO;
import cilib.pso.dynamic.detectionstrategies.EnvironmentChangeDetectionStrategy;
import cilib.pso.dynamic.detectionstrategies.RandomSentryDetectionStrategy;
import cilib.pso.dynamic.responsestrategies.EnvironmentChangeResponseStrategy;
import cilib.pso.dynamic.responsestrategies.PartialReinitialisationResponseStrategy;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;

/**
 * Dynamic iteration strategy for PSO in dynamic environments.
 * In each iteration, it checks for an environmental change, and reinitialises
 * a percentage of the swarm once such a change is detected in order to preserve
 * diversity.
 *
 * The algorithm is adopted from<br/>
 * @book{focsi,
 *         author=         {Andries P. Engelbrecht},
 *         title=          {{Fundamentals of Computational Swarm Intelligence}},
 *        publisher=      {John Wiley \& Sons, Ltd},
 *         year=           {2005}
 * }
 *
 */
public class DynamicIterationStrategy implements IterationStrategy<PSO> {

    private static final long serialVersionUID = -4441422301948289718L;
    private IterationStrategy<PSO> iterationStrategy;
    private EnvironmentChangeDetectionStrategy detectionStrategy;
    private EnvironmentChangeResponseStrategy responseStrategy;

    /**
     * Create a new instance of {@linkplain DynamicIterationStrategy}.
     * <p>
     * The following defaults are set in the constructor:
     * randomiser is instantiated as a MersenneTwister,
     * theta is set to 0.001,
     * reinitialisationRatio is set to 0.5 (reinitialise one half of the swarm)
     */
    public DynamicIterationStrategy() {
        this.iterationStrategy = new SynchronousIterationStrategy();
        this.detectionStrategy = new RandomSentryDetectionStrategy();
        this.responseStrategy = new PartialReinitialisationResponseStrategy();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public DynamicIterationStrategy(DynamicIterationStrategy copy) {
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.detectionStrategy = copy.detectionStrategy.getClone();
        this.responseStrategy = copy.responseStrategy.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DynamicIterationStrategy getClone() {
        return new DynamicIterationStrategy(this);
    }

    /**
     * Structure of Dynamic iteration strategy with re-initialisation:
     *
     * <ol>
     *   <li>Check for environment change</li>
     *   <li>If the environment has changed:</li>
     *   <ol>
     *     <li>Respond to change</li>
     *   <ol>
     *   <li>Perform normal iteration</li>
     * </ol>
     */
    @Override
    public void performIteration(PSO algorithm) {
        boolean hasChanged = detectionStrategy.detect(algorithm);

        if (hasChanged) {
            responseStrategy.respond(algorithm);
        }

        iterationStrategy.performIteration(algorithm);
    }

    /**
     * Get the current {@linkplain IterationStrategy}.
     * @return The current {@linkplain IterationStrategy}.
     */
    public IterationStrategy<PSO> getIterationStrategy() {
        return iterationStrategy;
    }

    /**
     * Set the {@linkplain IterationStrategy} to be used.
     * @param iterationStrategy The value to set.
     */
    public void setIterationStrategy(IterationStrategy<PSO> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    /**
     * Get the currently defined {@linkplain EnvironmentChangeDetectionStrategy}.
     * @return The current {@linkplain EnvironmentChangeDetectionStrategy}.
     */
    public EnvironmentChangeDetectionStrategy getDetectionStrategy() {
        return detectionStrategy;
    }

    /**
     * Set the {@linkplain EnvironmentChangeDetectionStrategy} to be used.
     * @param detectionStrategy The {@linkplain EnvironmentChangeDetectionStrategy} to set.
     */
    public void setDetectionStrategy(EnvironmentChangeDetectionStrategy detectionStrategy) {
        this.detectionStrategy = detectionStrategy;
    }

    /**
     * Get the currently defined {@linkplain EnvironmentChangeResponseStrategy},
     * @return The current {@linkplain EnvironmentChangeResponseStrategy}.
     */
    public EnvironmentChangeResponseStrategy getResponseStrategy() {
        return responseStrategy;
    }

    /**
     * Set the current {@linkplain EnvironmentChangeResponseStrategy} to use.
     * @param responseStrategy The {@linkplain EnvironmentChangeResponseStrategy} to set.
     */
    public void setResponseStrategy(EnvironmentChangeResponseStrategy responseStrategy) {
        this.responseStrategy = responseStrategy;
    }

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return this.iterationStrategy.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        this.iterationStrategy.setBoundaryConstraint(boundaryConstraint);
    }
}
