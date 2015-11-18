/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.tuning.parameters;

import fj.P1;
import cilib.type.types.container.Vector;

/**
 * Generates a list of values (in a vector) for a particular parameter.
 */
public abstract class ParameterGenerator extends P1<Vector> {
    
    protected int precision;
    
    public ParameterGenerator() {
        this.precision = 2;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getPrecision() {
        return precision;
    }
}
