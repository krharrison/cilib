/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.niching.iterationstrategies;

import com.google.common.collect.Lists;
import fj.P2;
import fj.data.List;
import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.niching.NichingAlgorithm;
import static cilib.niching.NichingFunctions.*;
import cilib.niching.NichingSwarms;
import static cilib.niching.NichingSwarms.onMainSwarm;
import cilib.niching.creation.NicheCreationStrategy;
import cilib.niching.creation.NicheDetection;
import cilib.niching.merging.MergeStrategy;
import cilib.pso.particle.Particle;
import static cilib.util.functions.Populations.enforceTopology;

public class NichePSO extends AbstractIterationStrategy<NichingAlgorithm> {

    @Override
    public NichePSO getClone() {
        return this;
    }

    /**
     * <p>
     * Perform an iteration of NichePSO.
     * </p>
     * <p>
     * The general format of this method would be the following steps:
     * <ol>
     *   <li>Perform an iteration of the main swarm.</li>
     *   <li>Perform an iteration for each of the contained sub-swarms.</li>
     *   <li>Merge any sub-swarms as defined my the associated {@link MergeStrategy}.</li>
     *   <li>Perform an absorption step defined by a {@link MergeStrategy}.</li>
     *   <li>Identify any new potential niches using a {@link NicheDetection}.</li>
     *   <li>Create new sub-swarms via a {@link NicheCreationStrategy} for the identified niches.</li>
     * </ol>
     * </p>
     */
    @Override
    public void performIteration(NichingAlgorithm alg) {
        P2<SinglePopulationBasedAlgorithm, List<SinglePopulationBasedAlgorithm>> newSwarms =
                onMainSwarm(alg.getMainSwarmIterator())
                .andThen(alg.getSubSwarmIterator())
                .andThen(merge(alg.getMergeDetector(),
                    alg.getMainSwarmMerger(),
                    alg.getSubSwarmMerger()))
                .andThen(absorb(alg.getAbsorptionDetector(),
                    alg.getMainSwarmAbsorber(),
                    alg.getSubSwarmAbsorber()))
                .andThen(onMainSwarm(enforceTopology(((Particle) alg.getEntityType()).getBehaviour())))
                .andThen(createNiches(alg.getNicheDetector(),
                    alg.getNicheCreator(),
                    alg.getMainSwarmCreationMerger()))
                .f(NichingSwarms.of(alg.getMainSwarm(), alg.getPopulations()));

        alg.setPopulations(Lists.newArrayList(newSwarms._2().toCollection()));
        alg.setMainSwarm(newSwarms._1());
    }
}
