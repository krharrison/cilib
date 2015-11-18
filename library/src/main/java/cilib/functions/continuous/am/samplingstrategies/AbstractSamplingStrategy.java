/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.am.samplingstrategies;

import java.util.ArrayList;
import cilib.type.types.Numeric;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;

/**
 * Abstract class to construct sampling points for AM sampling strategies
 */
public abstract class AbstractSamplingStrategy implements SamplingStrategy {
    protected Vector constructInputVector(double x, Vector values) {
        ArrayList<Numeric> inputValues = new ArrayList<>(values);

        inputValues.add(0, Real.valueOf(x));
        Numeric[] arr = inputValues.toArray(new Numeric[0]);
        
        return Vector.of(arr);
    }
}
