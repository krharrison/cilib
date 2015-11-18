/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.problem.solution;

import cilib.util.Cloneable;

/**
 * This is a common abstraction for all solution classes. All solutions should extend this interface.
 * This interface does not declare any methods.
 *
 */
public interface Solution extends Cloneable {

    @Override
    public Solution getClone();
}
