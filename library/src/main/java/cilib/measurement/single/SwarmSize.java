/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.algorithm.population.HasTopology;
import cilib.entity.Entity;
import cilib.measurement.Measurement;
import cilib.type.types.Int;

public class SwarmSize implements Measurement<Int> {

    @Override
    public Measurement<Int> getClone() {
        return this;
    }

    @Override
    public Int getValue(Algorithm algorithm) {
        return Int.valueOf(((HasTopology<Entity>) algorithm).getTopology().length());
    }
}
