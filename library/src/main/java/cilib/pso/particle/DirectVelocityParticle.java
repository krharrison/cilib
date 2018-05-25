package cilib.pso.particle;

import cilib.pso.behaviour.StandardParticleBehaviour;
import cilib.pso.velocityprovider.DirectVelocityProvider;
import cilib.type.types.container.Vector;

/**
 * A particle which has its velocity set directly (for example, via Genetic Programming)
 */
public class DirectVelocityParticle extends StandardParticle {

    public DirectVelocityParticle(){
        ((StandardParticleBehaviour) this.behaviour).setVelocityProvider(new DirectVelocityProvider());
    }

    public DirectVelocityParticle(DirectVelocityParticle copy){
        super(copy);
    }


    @Override
    public DirectVelocityParticle getClone() {
        return new DirectVelocityParticle(this);
    }


    public void setVelocity(int index, double value){
        getVelocity().setReal(index, value); //TODO: does this even change the velocity?
    }

    public void setVelocity(Vector velocity){
        updateVelocity(velocity);
    }
}
