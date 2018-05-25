/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies;

import cilib.entity.Property;
import cilib.pso.behaviour.NonEvaluatingStandardParticleBehaviour;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.pbestupdate.BoundedPersonalBestUpdateStrategy;
import cilib.type.types.Int;
import fj.F;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.StandardParticle;
import cilib.type.types.container.Vector;

public class PSOAdaptationStrategy implements SwarmAdaptationStrategy {
    protected PSO adaptorPSO;

    public PSOAdaptationStrategy(){
        adaptorPSO = new PSO(); //TODO: clamping boundary constraint?
    }

    public PSOAdaptationStrategy(PSOAdaptationStrategy copy){
        this.adaptorPSO = copy.adaptorPSO.getClone();
    }

    @Override
    public void adapt(PSO algorithm) {

        //initialize the adaptor PSO
        if (adaptorPSO.getIterations() == 0) {
            adaptorPSO.setTopology(algorithm.getTopology().map(create).map(nbest));

        }
        else {
            for(int i = 0; i < algorithm.getTopology().length(); i++) {
                SelfAdaptiveParticle p = (SelfAdaptiveParticle) algorithm.getTopology().index(i);
                Particle paramParticle = adaptorPSO.getTopology().index(i);

                paramParticle.setPosition(p.getParameterSet().asVector());
                paramParticle.updateFitness(p.getFitness());
            }
        }

        adaptorPSO.performIteration();

        for(int i = 0; i < algorithm.getTopology().length(); i++){
            SelfAdaptiveParticle p = (SelfAdaptiveParticle) algorithm.getTopology().index(i);

            Particle paramParticle = adaptorPSO.getTopology().index(i);
            Vector parameterPosition = (Vector)paramParticle.getPosition();
            p.getParameterSet().fromVector(parameterPosition);
        }
    }

    final F<Particle, Particle> create = new F<Particle, Particle>() {
        @Override
        public Particle f(Particle p) {
            SelfAdaptiveParticle p2 = (SelfAdaptiveParticle) p;

            Particle newParticle = new StandardParticle();
            newParticle.setPersonalBestUpdateStrategy(new BoundedPersonalBestUpdateStrategy());
            newParticle.setPosition(p2.getParameterSet().asVector());
            newParticle.put(Property.FITNESS, p.getFitness());
            newParticle.put(Property.BEST_FITNESS,p.getFitness());

            newParticle.put(Property.BEST_POSITION, newParticle.getPosition());
            newParticle.setNeighbourhoodBest(newParticle);
            newParticle.put(Property.VELOCITY, Vector.fill(0,3));
            newParticle.setBehaviour(new NonEvaluatingStandardParticleBehaviour());

            newParticle.put(Property.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
            newParticle.put(Property.POSITION_UPDATE_COUNTER, Int.valueOf(0));
            newParticle.put(Property.PREVIOUS_SOLUTION, newParticle.getPosition());

            return newParticle;
        }
    };

    final F<Particle, Particle> nbest = new F<Particle, Particle>() {
        public Particle f(Particle current) {
            for (Particle other : adaptorPSO.getNeighbourhood().f(adaptorPSO.getTopology(), current)) {
                if (current.getSocialFitness().compareTo(other.getNeighbourhoodBest().getSocialFitness()) > 0) {
                    other.setNeighbourhoodBest(current);
                }
            }

            return current;
        }
    };

    public PSOAdaptationStrategy getClone(){
        return new PSOAdaptationStrategy(this);
    }





 /*   @Override
    public void adapt(PSO algorithm) {
        final F<Particle, Particle> create = new F<Particle, Particle>() {
            @Override
            public Particle f(Particle p) {
                SelfAdaptiveParticle p2 = (SelfAdaptiveParticle) p;

                //StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
                //SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider)behaviour.getVelocityProvider();

                //create a new vector as [w, c1, c2] from the current particles parameter set
                //Vector.Builder builder = Vector.newBuilder();
                //builder.add(p2.getInertiaWeight().getParameter());
                //builder.add(p2.getCognitiveAcceleration().getParameter());
                //builder.add(p2.getSocialAcceleration().getParameter());

                Particle newParticle = new StandardParticle();
                newParticle.setPersonalBestUpdateStrategy(new BoundedPersonalBestUpdateStrategy());
                //TODO need to initialize the particle
                //newParticle.setPosition(builder.build());
                newParticle.setPosition(p2.getParameterSet().asVector());
                //newParticle.updateFitness(p2.getParameterSet().getFitness());
                newParticle.put(Property.FITNESS, p2.getParameterSet().getFitness());
                newParticle.put(Property.BEST_POSITION, newParticle.getPosition()); //TODO: is pBest = pos acceptable?
                newParticle.setNeighbourhoodBest(newParticle);
                newParticle.put(Property.VELOCITY, Vector.fill(0,3));
                newParticle.setBehaviour(new NonEvaluatingStandardParticleBehaviour());
                return newParticle;
            }
        };

        adaptorPSO.setTopology(algorithm.getTopology().map(create));
        adaptorPSO.performIteration();

        //iterate through particles of the adaptorPSO and update the parameters of the original PSO accordingly
        for(int i = 0; i < algorithm.getTopology().length(); i++){
            SelfAdaptiveParticle p = (SelfAdaptiveParticle) algorithm.getTopology().index(i);
            //StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
            //SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider)behaviour.getVelocityProvider();

            Particle parameterParticle = adaptorPSO.getTopology().index(i);
            Vector parameterPosition = (Vector)parameterParticle.getPosition();
            //p.setInertiaWeight(ConstantControlParameter.of(parameterPosition.doubleValueOf(0)));
            //p.setCognitiveAcceleration(ConstantControlParameter.of(parameterPosition.doubleValueOf(1)));
            //p.setSocialAcceleration(ConstantControlParameter.of(parameterPosition.doubleValueOf(2)));
            p.getParameterSet().fromVector(parameterPosition);
        }
    } */

}
