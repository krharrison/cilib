/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.type.types.Numeric;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;

/**
 * Wrapper that calculates the mean of a Vector returned by another measurement.
 */
public class MeanOfVectorMeasurement implements Measurement<Real> {

    private Measurement<Vector> measurement;

    public MeanOfVectorMeasurement() {}

    public MeanOfVectorMeasurement(MeanOfVectorMeasurement rhs) {
        measurement = rhs.measurement.getClone();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public MeanOfVectorMeasurement getClone() {
        return new MeanOfVectorMeasurement(this);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        Vector result = measurement.getValue(algorithm);

        double total = 0;
        for (Numeric curElement : result) {
            total += curElement.doubleValue();
        }
        
        return Real.valueOf(total/result.size());
    }

    /**
     * Sets the measurement to wrap.
     * 
     * @param measurement The measurement to wrap.
     */
    public void setMeasurement(Measurement<Vector> measurement) {
        this.measurement = measurement;
    }
}
