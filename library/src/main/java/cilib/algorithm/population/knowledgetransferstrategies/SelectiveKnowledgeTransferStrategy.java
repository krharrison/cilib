/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.algorithm.population.knowledgetransferstrategies;

import java.util.List;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Entity;
import cilib.type.types.Type;
import cilib.util.selection.recipes.ElitistSelector;
import cilib.util.selection.recipes.RingBasedPopulationSelector;
import cilib.util.selection.recipes.Selector;

/**
 * An implementation of {@link KnowledgeTransferStrategy} where two
 * {@link Selector} instances are used to first select a
 * {@link PopulationBasedAlgorithm sub-population} from a collection of
 * population-based algorithms (see {@link MultiPopulationBasedAlgorithm}) and
 * then within this sub-population's
 * {@link cilib.entity.Topology}, which {@link Entity}'s
 * knowledge is to be transferred to the caller requesting it.
 */
public class SelectiveKnowledgeTransferStrategy implements KnowledgeTransferStrategy {

    private static final long serialVersionUID = 402688951924934682L;

    private Selector<SinglePopulationBasedAlgorithm> populationSelection;
    private Selector<Entity> entitySelection;

    public SelectiveKnowledgeTransferStrategy() {
        this.populationSelection = new RingBasedPopulationSelector();
        this.entitySelection = new ElitistSelector<Entity>();
    }

    public SelectiveKnowledgeTransferStrategy(SelectiveKnowledgeTransferStrategy copy) {
        this.populationSelection = copy.populationSelection;
        this.entitySelection = copy.entitySelection;
    }

    @Override
    public SelectiveKnowledgeTransferStrategy getClone() {
        return new SelectiveKnowledgeTransferStrategy(this);
    }

    public void setPopulationSelection(Selector<SinglePopulationBasedAlgorithm> populationSelection) {
        this.populationSelection = populationSelection;
    }

    public Selector<SinglePopulationBasedAlgorithm> getPopulationSelection() {
        return this.populationSelection;
    }

    public void setEntitySelection(Selector<Entity> entitySelection) {
        this.entitySelection = entitySelection;
    }

    public Selector<Entity> getEntitySelection() {
        return this.entitySelection;
    }

    @Override
    public Type transferKnowledge(List<SinglePopulationBasedAlgorithm> allPopulations) {
        SinglePopulationBasedAlgorithm population = this.populationSelection.on(allPopulations).select();
        Entity entity = this.entitySelection.on((Iterable<Entity>) population.getTopology()).select();
        return entity.getProperties();
    }
}
