/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.dynamic.detectionstrategies;

import cilib.algorithm.Algorithm;
import cilib.algorithm.population.HasNeighbourhood;
import cilib.algorithm.population.HasTopology;
import cilib.pso.dynamic.DynamicIterationStrategy;
import cilib.util.Cloneable;

/**
 * This abstract class defines the interface that detection strategies have to adhere to.
 * Detection strategies are used within the scope of a {@link DynamicIterationStrategy} to
 * detect whether the environment has change during the course of an
 * {@link Algorithm algorithm's} execution.
 */
public abstract class EnvironmentChangeDetectionStrategy implements Cloneable {
    protected double epsilon = 0.0;
    protected int interval = 0;

    public EnvironmentChangeDetectionStrategy() {
        epsilon = 0.001;
        interval = 10;
    }

    public EnvironmentChangeDetectionStrategy(EnvironmentChangeDetectionStrategy rhs) {
        epsilon = rhs.epsilon;
        interval = rhs.interval;
    }

    /**
     * Clone the <tt>EnvironmentChangeDetectionStrategy</tt> object.
     * @return A cloned <tt>EnvironmentChangeDetectionStrategy</tt>
     */
    public abstract EnvironmentChangeDetectionStrategy getClone();

    /**
     * Check the environment in which the specified PSO algorithm is running for changes.
     * @param algorithm The <tt>PSO</tt> that runs in a dynamic environment.
     * @return true if any changes are detected, false otherwise
     */
    public abstract <E extends HasTopology & Algorithm & HasNeighbourhood> boolean detect(E algorithm);

    public void setEpsilon(double e) {
        if (e < 0.0) {
            throw new IllegalArgumentException("The epsilon value cannot be negative");
        }

        epsilon = e;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setIterationsModulus(int im) {
        if (im <= 0) {
            throw new IllegalArgumentException("The number of consecutive iterations to pass cannot be <= 0");
        }

        interval = im;
    }

    public int getIterationsModulus() {
        return interval;
    }
}
