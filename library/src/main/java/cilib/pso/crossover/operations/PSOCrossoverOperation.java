/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.crossover.operations;

import fj.F;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.util.Cloneable;

/**
 * These classes perform different functions used with the PSOCrossoverIterationStrategy.
 */
public abstract class PSOCrossoverOperation extends F<PSO, fj.data.List<Particle>> implements Cloneable {
    @Override
    public abstract fj.data.List<Particle> f(PSO pso);

    @Override
    public abstract PSOCrossoverOperation getClone();
}
