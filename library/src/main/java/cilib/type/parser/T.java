/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.type.parser;

import cilib.type.types.Bounds;
import cilib.type.types.StringType;
import cilib.type.types.Type;

/**
 *
 *
 */
final class T implements TypeCreator {
    private static final long serialVersionUID = 1198714503772193216L;

    /**
     * {@inheritDoc}
     */
    public Type create() {
        return new StringType("");
    }

    /**
     * {@inheritDoc}
     */
    public Type create(double value) {
        throw new UnsupportedOperationException("StringTypes with single values do not exist");
    }

    /**
     * {@inheritDoc}
     */
    public Type create(final Bounds bounds) {
        throw new UnsupportedOperationException("StringTypes with bounds do not exist");
    }

}
