/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.parametersetgenerator;

import cilib.pso.selfadaptive.ParameterSet;

public interface ParameterSetGenerator {
    ParameterSet generate();

    ParameterSetGenerator getClone();
}
