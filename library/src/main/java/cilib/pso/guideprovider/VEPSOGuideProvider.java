/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.guideprovider;

import cilib.algorithm.AbstractAlgorithm;
import cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import cilib.algorithm.population.MultiPopulationCriterionBasedAlgorithm;
import cilib.algorithm.population.knowledgetransferstrategies.KnowledgeTransferStrategy;
import cilib.algorithm.population.knowledgetransferstrategies.SelectiveKnowledgeTransferStrategy;
import cilib.entity.Property;
import cilib.moo.criterion.CriterionBasedMOProblemAdapter;
import cilib.pso.particle.Particle;
import cilib.type.types.Blackboard;
import cilib.type.types.Type;
import cilib.type.types.container.StructuredType;

/**
 * Vector-Evaluated Particle Swarm Optimisation Guide Provider
 *
 * <p>
 * This {@link GuideProvider} implements the basic behaviour of VEPSO where each
 * particle's global guide is selected as the position of a particle within
 * another swarm (see {@link MultiPopulationCriterionBasedAlgorithm}). Each swarm
 * is evaluated according to a different sub-objective (see {@link
 * CriterionBasedMOProblemAdapter}) of a Multi-objective optimisation problem. A
 * {@link KnowledgeTransferStrategy} is used to determine which swarm is selected
 * (either random or ring-based) as well as which particle's position within this
 * swarm will be used as guide (usually the gBest particle).
 * </p>
 *
 * <p>
 * References:
 * </p>
 * <p>
 * <ul>
 * <li> K. E. Parsopoulos, D. K. Tasoulis and M. N. Vrahatis, "Multiobjective Optimization using
 * Parallel Vector Evaluated Particle Swarm Optimization", in Proceedings of the IASTED International
 * Conference on Artificial Intelligence and Applications, vol 2, pp. 823-828, 2004.
 * </li>
 * </ul>
 * </p>
 *
 */
public class VEPSOGuideProvider implements GuideProvider {

    private static final long serialVersionUID = -8916378051119235043L;
    private KnowledgeTransferStrategy knowledgeTransferStrategy;

    public VEPSOGuideProvider() {
        this.knowledgeTransferStrategy = new SelectiveKnowledgeTransferStrategy();
    }

    public VEPSOGuideProvider(VEPSOGuideProvider copy) {
        this.knowledgeTransferStrategy = copy.knowledgeTransferStrategy.getClone();
    }

    @Override
    public VEPSOGuideProvider getClone() {
        return new VEPSOGuideProvider(this);
    }

    public void setKnowledgeTransferStrategy(KnowledgeTransferStrategy knowledgeTransferStrategy) {
        this.knowledgeTransferStrategy = knowledgeTransferStrategy;
    }

    public KnowledgeTransferStrategy getKnowledgeTransferStrategy() {
        return this.knowledgeTransferStrategy;
    }

    @SuppressWarnings("unchecked")
    @Override
    public StructuredType get(Particle particle) {
        MultiPopulationBasedAlgorithm topLevelAlgorithm = (MultiPopulationBasedAlgorithm) AbstractAlgorithm.getAlgorithmList().head();
        Blackboard<Property, Type> knowledge = (Blackboard<Property, Type>) this.knowledgeTransferStrategy.transferKnowledge(topLevelAlgorithm.getPopulations());
        return (StructuredType) knowledge.get(Property.BEST_POSITION);
    }
}
