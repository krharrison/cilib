/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.coevolution.cooperative.contributionselection;

import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Topologies;
import cilib.type.types.container.Vector;

/**
 * Select the contribution based on the topology best entity of the {@linkplain SinglePopulationBasedAlgorithm}.
 */
public class TopologyBestContributionSelectionStrategy implements
        ContributionSelectionStrategy {
    private static final long serialVersionUID = -3129164721354568798L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getContribution(SinglePopulationBasedAlgorithm algorithm) {
        return (Vector) Topologies.getBestEntity(algorithm.getTopology()).getPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TopologyBestContributionSelectionStrategy getClone() {
        return new TopologyBestContributionSelectionStrategy();
    }
}
