/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.type.parser;

import cilib.math.random.generator.Rand;
import cilib.type.types.Bit;
import cilib.type.types.Bounds;
import cilib.type.types.Type;

final class B implements TypeCreator {

    private static final long serialVersionUID = 7124782787032789332L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Type create() {
        return Bit.valueOf(Rand.nextBoolean());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type create(double value) {
        if (Double.compare(value, 0.0) == 0) {
            return Bit.valueOf(false);
        } else if (Double.compare(value, 1.0) == 0) {
            return Bit.valueOf(true);
        }
        throw new UnsupportedOperationException("Cannot create a bit type with the specified value.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type create(final Bounds bounds) {
        throw new UnsupportedOperationException("Bit types cannot be constructed with bounds");
    }
}
