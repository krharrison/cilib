package net.sourceforge.cilib.algorithm.initialisation;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.IndividualizedNeighbourhood;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.problem.Problem;

/**
 * Created by kyle on 08/12/15.
 */
public class HeterogeneousNeighbourhoodPopulationInitialisationStrategy implements PopulationInitialisationStrategy{

    protected PopulationInitialisationStrategy delegate;

    public HeterogeneousNeighbourhoodPopulationInitialisationStrategy(){
        this.delegate = new ClonedPopulationInitialisationStrategy();
    }

    public HeterogeneousNeighbourhoodPopulationInitialisationStrategy(HeterogeneousNeighbourhoodPopulationInitialisationStrategy copy){
        this.delegate = copy.delegate.getClone();
    }

    @Override
    public HeterogeneousNeighbourhoodPopulationInitialisationStrategy getClone() {
        return new HeterogeneousNeighbourhoodPopulationInitialisationStrategy(this);
    }

    @Override
    public void setEntityType(Entity entity) {
        Preconditions.checkArgument(entity instanceof IndividualizedNeighbourhood,
                "Entity must implement IndividualizedNeighbourhood");

        delegate.setEntityType(entity);

    }

    @Override
    public Entity getEntityType() {
        return delegate.getEntityType();
    }

    @Override
    public <E extends Entity> Iterable<E> initialise(Problem problem) {
        Iterable<E> entities = delegate.initialise(problem);

        for(Entity e : entities){
            IndividualizedNeighbourhood nEntity = (IndividualizedNeighbourhood) e;
            //TODO: verify we can actually get numEntity as a value from the random
            nEntity.setNeighbourhoodSize(Rand.nextInt(getEntityNumber()));
        }

        return entities;
    }

    @Override
    public int getEntityNumber() {
        return delegate.getEntityNumber();
    }

    @Override
    public void setEntityNumber(int entityNumber) {
        delegate.setEntityNumber(entityNumber);
    }
}
