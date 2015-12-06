/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies;

import cilib.pso.particle.SelfAdaptiveParticle;
import fj.F;
import cilib.controlparameter.ConstantControlParameter;
import cilib.pso.PSO;
import cilib.pso.behaviour.StandardParticleBehaviour;
import cilib.pso.particle.Particle;
import cilib.pso.particle.StandardParticle;
import cilib.pso.velocityprovider.SelfAdaptiveVelocityProvider;
import cilib.type.types.container.Vector;

public class PSOAdaptationStrategy implements AlgorithmAdaptationStrategy {
    protected PSO adaptorPSO;

    public PSOAdaptationStrategy(){
        adaptorPSO = new PSO(); //TODO: clamping boundary constraint?
    }

    public PSOAdaptationStrategy(PSOAdaptationStrategy copy){
        this.adaptorPSO = copy.adaptorPSO.getClone();
    }

    public PSOAdaptationStrategy getClone(){
        return new PSOAdaptationStrategy(this);
    }


    @Override
    public void adapt(PSO algorithm) {
        final F<Particle, Particle> create = new F<Particle, Particle>() {
            @Override
            public Particle f(Particle p) {
                SelfAdaptiveParticle p2 = (SelfAdaptiveParticle) p;
                //StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
                //SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider)behaviour.getVelocityProvider();

                //create a new vector as [w, c1, c2] from the current particles parameter set
                Vector.Builder builder = Vector.newBuilder();
                builder.add(p2.getInertiaWeight().getParameter());
                builder.add(p2.getCognitiveAcceleration().getParameter());
                builder.add(p2.getSocialAcceleration().getParameter());

                Particle newParticle = new StandardParticle();
                //TODO need to initialize the particle
                newParticle.setPosition(builder.build());
                newParticle.updateFitness(p2.getParameterSet().getFitness());
                return newParticle;
            }
        };

        adaptorPSO.setTopology(algorithm.getTopology().map(create));
        adaptorPSO.performIteration();

        //iterate through particles of the adaptorPSO and update the parameters of the original PSO accordingly
        for(int i = 0; i < algorithm.getTopology().length(); i++){
            SelfAdaptiveParticle p = (SelfAdaptiveParticle) algorithm.getTopology().index(i);
            StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
            SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider)behaviour.getVelocityProvider();

            Particle parameterParticle = adaptorPSO.getTopology().index(i);
            Vector parameterPosition = (Vector)parameterParticle.getPosition();
            p.setInertiaWeight(ConstantControlParameter.of(parameterPosition.doubleValueOf(0)));
            p.setCognitiveAcceleration(ConstantControlParameter.of(parameterPosition.doubleValueOf(1)));
            p.setSocialAcceleration(ConstantControlParameter.of(parameterPosition.doubleValueOf(2)));

        }
    }

}
