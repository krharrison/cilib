package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.entity.IndividualizedNeighbourhood;
import net.sourceforge.cilib.entity.topologies.HeterogeneousNeighbourhood;

/**
 * An extension of the standard particle which contains the neighbourhood size.
 */
public class HeterogeneousNeighbourhoodParticle extends StandardParticle implements IndividualizedNeighbourhood {
    protected int neighbourhoodSize;

    public HeterogeneousNeighbourhoodParticle(){
        this.neighbourhoodSize = 3;
    }

    public HeterogeneousNeighbourhoodParticle(int size){
        this.neighbourhoodSize = size;
    }

    public HeterogeneousNeighbourhoodParticle(HeterogeneousNeighbourhoodParticle copy){
        super(copy);
        this.neighbourhoodSize = copy.neighbourhoodSize;
    }

    @Override
    public int getNeighbourhoodSize() {
        return neighbourhoodSize;
    }

    @Override
    public void setNeighbourhoodSize(int neighbourhoodSize){
        this.neighbourhoodSize = neighbourhoodSize;
    }

    @Override
    public HeterogeneousNeighbourhoodParticle getClone(){
        return new HeterogeneousNeighbourhoodParticle(this);
    }
}
