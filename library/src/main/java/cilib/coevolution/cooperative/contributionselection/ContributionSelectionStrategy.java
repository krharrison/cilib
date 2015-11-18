/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.coevolution.cooperative.contributionselection;

import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import cilib.type.types.container.Vector;
import cilib.util.Cloneable;

/**
 * This interface dictates how a participating {@linkplain PopulationBasedAlgorithm} should select its participant solution for a {@linkplain CooperativeCoevolutionAlgorithm}.
 */
public interface ContributionSelectionStrategy extends Cloneable {

    /**
     * Return the relevant participant solution.
     * @param algorithm The {@linkplain PopulationBasedAlgorithm} to select the participant from.
     * @return The participating solution {@linkplain Vector}
     */
    Vector getContribution(SinglePopulationBasedAlgorithm algorithm);

    /**
     * {@inheritDoc}
     */
    @Override
    ContributionSelectionStrategy getClone();

}
