/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.generic;

import cilib.algorithm.AbstractAlgorithm;
import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.type.types.Real;

/**
 *
 */
public class PercentageComplete implements Measurement<Real> {
    private static final long serialVersionUID = 552272710698138639L;

    @Override
    public PercentageComplete getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        AbstractAlgorithm alg = (AbstractAlgorithm) algorithm;
        return Real.valueOf(alg.getPercentageComplete());
    }

}
