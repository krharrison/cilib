/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.solution;

/**
 * This class is used to represent a fitness value that is always superior.
 * <p />
 * This class is a singleton.
 *
 */
public final class SuperiorFitness implements Fitness {

    private SuperiorFitness() {
    }

    /**
     * Get the cloned instance of this object. Due to this object being a
     * Singleton, the same instance is returned and is not cloned.
     */
    public SuperiorFitness getClone() {
        return instance;
    }

    /**
     * Always returns null. <code>SuperiorFitness</code> does not have a value. The
     * most sensible value to return is Double.NaN as it is still a value, however,
     * it represents something that is not a number (effectively null). Returning
     * Double.NaN will ensure that some of the Measurements do get an value, even if
     * the value is Double.NaN
     *
     * @return Double.NaN as the value is always inferior.
     */
    public Double getValue() {
        return Double.NaN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Fitness newInstance(Double value) {
        return instance();
    }

    /**
     * Returns 1, unless other is also the <code>SuperiorFitness</code> instance.
     *
     * @param other The {@code Fitness} to compare.
     * @return 0 if other is <code>SuperiorFitness</code> instance, 1 otherwise.
     */
    @Override
    public int compareTo(Fitness other) {
        return (this == other) ? 0 : 1;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if ((object == null) || (this.getClass() != object.getClass()))
            return false;

        Fitness otherFitness = (Fitness) object;
        return getValue().equals(otherFitness.getValue());
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + getValue().hashCode();
        return hash;
    }

    /**
     * Obtain a reference to the <code>SuperiorFitness</code> instance.
     *
     * @return the <code>SuperiorFitness</code> instance.
     */
    public static Fitness instance() {
        return instance;
    }

    private static SuperiorFitness instance = new SuperiorFitness();

}
