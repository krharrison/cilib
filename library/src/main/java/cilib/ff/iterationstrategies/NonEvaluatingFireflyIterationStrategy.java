/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.ff.iterationstrategies;


import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.ff.FFA;
import cilib.ff.firefly.Firefly;

/**
 * Implementation of the standard iteration strategy for the Firefly algorithm
 * without the fitness evaluation.
 */
public class NonEvaluatingFireflyIterationStrategy extends AbstractIterationStrategy<FFA> {

    /**
     * {@inheritDoc}
     */
    @Override
    public NonEvaluatingFireflyIterationStrategy getClone() {
        return this;
    }

    /**
     * <p>This is the standard Firefly iteration strategy:</p>
     * <ol>
     * <li>For each Firefly<sub>i</sub></li>
     * <ol>
     * <li>For each other Firefly<sub>j</sub><li>
     * <ol>
     * <li>If Intensity<sub>j</sub> &gt; Intensity<sub>i</sub></li>
     * <ol>
     * <li>Move Firefly<sub>i</sub> toward Firefly<sub>j</sub>
     * according to Attractiveness<sub>j</sub></li>
     * </ol>
     * </ol>
     * </ol>
     * </ol>
     * @param algorithm The algorithm to which an iteration is to be applied.
     */
    @Override
    public void performIteration(FFA algorithm) {
        fj.data.List<Firefly> topology = algorithm.getTopology();

        for (Firefly current : topology) {
            for (Firefly other : topology) {
                if (other.isBrighter(current)) {
                    current.updatePosition(other);
                    boundaryConstraint.enforce(current);
                }
            }
        }
    }
}