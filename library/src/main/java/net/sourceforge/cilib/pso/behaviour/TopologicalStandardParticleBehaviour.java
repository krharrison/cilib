package net.sourceforge.cilib.pso.behaviour;

import net.sourceforge.cilib.entity.topologies.GBestNeighbourhood;
import net.sourceforge.cilib.entity.topologies.Neighbourhood;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * Created by kyle on 09/12/15.
 */
public class TopologicalStandardParticleBehaviour extends StandardParticleBehaviour {
    Neighbourhood<Particle> neighbourhood;

    public TopologicalStandardParticleBehaviour(){
        this.neighbourhood = new GBestNeighbourhood();
    }

    public TopologicalStandardParticleBehaviour(TopologicalStandardParticleBehaviour clone){
        super(clone);
        this.neighbourhood = clone.neighbourhood;
    }

    @Override
    public TopologicalStandardParticleBehaviour getClone(){
        return new TopologicalStandardParticleBehaviour(this);
    }

    public Neighbourhood<Particle> getNeighbourhood(){
        return neighbourhood;
    }

    public void setNeighbourhood(Neighbourhood<Particle> neighbourhood){
        this.neighbourhood = neighbourhood;
    }

}
