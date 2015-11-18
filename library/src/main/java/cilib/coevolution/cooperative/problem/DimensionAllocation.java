/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.coevolution.cooperative.problem;

import cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import cilib.measurement.single.Fitness;
import cilib.util.Cloneable;

/**
 * This abstract class hides the details of which sub-dimensions a participating
 * algorithm in a {@link CooperativeCoevolutionAlgorithm} optimizes.
 * <p>
 * The {@linkplain CooperativeCoevolutionProblemAdapter} class uses the concrete
 * {@linkplain DimensionAllocation} to copy the solution from a participating
 * {@code Entity} in a participating {@link PopulationBasedAlgorithm}
 * into the context vector so that its {@link Fitness} can be calculated.
 */
public abstract class DimensionAllocation implements Cloneable {
    private int size;

    /**
     * Constructor
     */
    public DimensionAllocation(){
        size = 0;
    }

    /**
     * Constructor
     * @param size The number of dimensions stored by this {@linkplain DimensionAllocation}.
     */
    public DimensionAllocation(int size){
        this.size = size;
    }

    /**
     * Copy constructor.
     * @param copy
     */
    public DimensionAllocation(DimensionAllocation copy){
        size = copy.size;
    }

    /**
     * Return the number of dimensions stored by this {@linkplain DimensionAllocation}.
     * @return the number of dimensions stored.
     */
    public int getSize() {
        return size;
    }

    /**
     * Return the index into the original problem vector of the element at the given index into the participant's solution.
     * @param elementIndex The index into the participant's solution vector.
     * @return The index of the same value for the original problem vector.
     */
    public abstract int getProblemIndex(int elementIndex);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract DimensionAllocation getClone();
}
