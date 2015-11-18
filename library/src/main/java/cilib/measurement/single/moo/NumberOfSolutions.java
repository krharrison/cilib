/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single.moo;

import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.moo.archive.Archive;
import cilib.type.types.Int;

/**
 * <p>
 * Measures the number of non-dominated solutions in an archive.
 * </p>
 *
 */
public class NumberOfSolutions implements Measurement {

    private static final long serialVersionUID = 7678968480113931897L;

    public NumberOfSolutions() {
    }

    public NumberOfSolutions(NumberOfSolutions copy) {
    }

    @Override
    public Measurement getClone() {
        return new NumberOfSolutions(this);
    }

    @Override
    public Int getValue(Algorithm algorithm) {
        Archive archive = Archive.Provider.get();
        return Int.valueOf(archive.size());
    }
}
