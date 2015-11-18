/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.hs;

import cilib.entity.AbstractEntity;
import cilib.entity.Property;
import cilib.problem.Problem;
import cilib.problem.solution.InferiorFitness;
import cilib.type.types.container.StructuredType;

/**
 * Entity definition for a Harmony.
 */
public class Harmony extends AbstractEntity {
    private static final long serialVersionUID = -4941414797957384798L;

    public Harmony() {
    }

    public Harmony(Harmony copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Harmony getClone() {
        return new Harmony(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise(Problem problem) {
        StructuredType harmony = problem.getDomain().getBuiltRepresentation().getClone();
        harmony.randomise();

        setPosition(harmony);
        put(Property.FITNESS, InferiorFitness.instance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinitialise() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
