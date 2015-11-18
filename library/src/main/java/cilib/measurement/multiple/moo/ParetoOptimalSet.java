/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.multiple.moo;

import java.util.Collection;
import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.moo.archive.Archive;
import cilib.problem.solution.OptimisationSolution;
import cilib.type.types.container.TypeList;
import cilib.type.types.container.Vector;

/**
 * <p>
 * Measures the set of non-dominated decision vectors within an archive.
 * </p>
 *
 */
public class ParetoOptimalSet implements Measurement<TypeList> {

    private static final long serialVersionUID = 8157352173131734782L;

    @Override
    public ParetoOptimalSet getClone() {
        return this;
    }

    @Override
    public TypeList getValue(Algorithm algorithm) {
        TypeList allPositions = new TypeList();
        Collection<OptimisationSolution> solutions = Archive.Provider.get();
        for (OptimisationSolution solution : solutions) {
            Vector position = (Vector) solution.getPosition();
            allPositions.add(position);
        }
        return allPositions;
    }
}
