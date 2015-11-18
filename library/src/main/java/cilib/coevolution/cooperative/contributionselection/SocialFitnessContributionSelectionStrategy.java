/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.coevolution.cooperative.contributionselection;

import java.util.Comparator;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.MemoryBasedEntity;
import cilib.entity.SocialEntity;
import cilib.entity.Topologies;
import cilib.entity.comparator.SocialBestFitnessComparator;
import cilib.type.types.container.Vector;

/**
 * Select the contribution based on the best position of the social best entity of the
 * {@linkplain PopulationBasedAlgorithm}.
 *
 */
public class SocialFitnessContributionSelectionStrategy implements
        ContributionSelectionStrategy {
    private static final long serialVersionUID = -2477638582277950895L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getContribution(SinglePopulationBasedAlgorithm algorithm) {
        MemoryBasedEntity entity = (MemoryBasedEntity) Topologies.getBestEntity(algorithm.getTopology(),
                (Comparator) new SocialBestFitnessComparator<SocialEntity>());
        return (Vector) entity.getBestPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContributionSelectionStrategy getClone() {
        return this;
    }

}
