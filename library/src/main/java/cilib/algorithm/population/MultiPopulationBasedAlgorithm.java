/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.algorithm.population;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import cilib.algorithm.AbstractAlgorithm;
import cilib.algorithm.iterator.AlgorithmIterator;
import cilib.algorithm.iterator.SequentialAlgorithmIterator;

/**
 * Algorithm class to describe the notion of aggregated {@linkplain PopulationBasedAlgorithm} instances.
 * <p>
 * The objective of this class is to ensure that the manner in which various multi-population based
 * algorithms are interfaced in the same manner.
 * <p>
 * Examples of such algorithms can include:
 * <ul>
 *   <li>Island Genetic Algorithms</li>
 *   <li>Niching Algorithms</li>
 *   <li>etc.</li>
 * </ul>
 *
 */
public abstract class MultiPopulationBasedAlgorithm extends AbstractAlgorithm implements Iterable<SinglePopulationBasedAlgorithm> {
    private static final long serialVersionUID = -5311450612897848103L;

    protected List<SinglePopulationBasedAlgorithm> subPopulationsAlgorithms;
    protected AlgorithmIterator<SinglePopulationBasedAlgorithm> algorithmIterator;

    /**
     * Create an instance of {@linkplain MultiPopulationBasedAlgorithm}.
     */
    public MultiPopulationBasedAlgorithm() {
        this.subPopulationsAlgorithms = Lists.newArrayList();
        this.algorithmIterator = new SequentialAlgorithmIterator<SinglePopulationBasedAlgorithm>();
        this.algorithmIterator.setAlgorithms(this.subPopulationsAlgorithms);
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public MultiPopulationBasedAlgorithm(MultiPopulationBasedAlgorithm copy) {
        super(copy);
        subPopulationsAlgorithms = Lists.newArrayList();

        for (SinglePopulationBasedAlgorithm algorithm : copy.subPopulationsAlgorithms) {
            subPopulationsAlgorithms.add(algorithm.getClone());
        }

        algorithmIterator = copy.algorithmIterator;
        algorithmIterator.setAlgorithms(subPopulationsAlgorithms);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<SinglePopulationBasedAlgorithm> iterator() {
        return this.algorithmIterator.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected abstract void algorithmIteration();

    /**
     * Get the {@linkplain List} of current sub-populations.
     * @return The {@linkplain List} of {@linkplain PopulationBasedAlgorithm}.
     */
    public List<SinglePopulationBasedAlgorithm> getPopulations() {
        return subPopulationsAlgorithms;
    }

    /**
     * Set the list of {@linkplain PopulationBasedAlgorithm} instances that the
     * {@linkplain MultiPopulationBasedAlgorithm} should maintain.
     * @param populationBasedAlgorithms The {@linkplain List} of {@linkplain PopulationBasedAlgorithm}s to set.
     */
    public void setPopulations(List<SinglePopulationBasedAlgorithm> populationBasedAlgorithms) {
        this.subPopulationsAlgorithms = populationBasedAlgorithms;
    }

    /**
     * Add a {@linkplain PopulationBasedAlgorithm} to the list of maintained sub-populations.
     * @param algorithm The {@linkplain PopulationBasedAlgorithm} to add to the current collection.
     */
    public void addPopulationBasedAlgorithm(SinglePopulationBasedAlgorithm algorithm) {
        this.subPopulationsAlgorithms.add(algorithm);
    }

    /**
     * Remove the provided {@linkplain PopulationBasedAlgorithm} from the collection of maintained
     * instances.
     * @param algorithm The instance to remove from the collection.
     */
    public void removePopulationBasedalgorithm(SinglePopulationBasedAlgorithm algorithm) {
        this.subPopulationsAlgorithms.remove(algorithm);
    }

    /**
     * Get an {@linkplain AlgorithmIterator} to iterate over the current collection of
     * {@linkplain PopulationBasedAlgorithm}s.
     * @return An {@linkplain AlgorithmIterator} over the current collection.
     */
    public AlgorithmIterator<SinglePopulationBasedAlgorithm> getAlgorithmIterator() {
        return algorithmIterator;
    }

    /**
     * Set the type of iterator to be used.
     * @param algorithmIterator The iterator instance to set.
     */
    public void setAlgorithmIterator(AlgorithmIterator<SinglePopulationBasedAlgorithm> algorithmIterator) {
        this.algorithmIterator = algorithmIterator;
        this.algorithmIterator.setAlgorithms(this.subPopulationsAlgorithms);
    }
}
