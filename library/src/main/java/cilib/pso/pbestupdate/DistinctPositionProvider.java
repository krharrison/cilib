/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.pbestupdate;

import fj.F;
import cilib.pso.particle.Particle;
import cilib.type.types.container.Vector;
import cilib.util.Cloneable;

public abstract class DistinctPositionProvider extends F<Particle, Vector> implements Cloneable {
    @Override
    public abstract DistinctPositionProvider getClone();
}
