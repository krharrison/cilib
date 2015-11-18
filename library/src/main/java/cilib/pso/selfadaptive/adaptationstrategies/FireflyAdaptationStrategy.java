/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies;


import fj.F;
import cilib.controlparameter.ConstantControlParameter;
import cilib.entity.Property;
import cilib.ff.FFA;
import cilib.ff.firefly.Firefly;
import cilib.ff.firefly.StandardFirefly;
import cilib.ff.iterationstrategies.NonEvaluatingFireflyIterationStrategy;
import cilib.problem.boundaryconstraint.ClampingBoundaryConstraint;
import cilib.pso.PSO;
import cilib.pso.behaviour.StandardParticleBehaviour;
import cilib.pso.particle.Particle;
import cilib.pso.velocityprovider.SelfAdaptiveVelocityProvider;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;

public class FireflyAdaptationStrategy implements AlgorithmAdaptationStrategy {

    protected FFA adaptorFFA;

    public FireflyAdaptationStrategy(){
        adaptorFFA = new FFA();
        NonEvaluatingFireflyIterationStrategy iterationStrategy = new NonEvaluatingFireflyIterationStrategy();
        iterationStrategy.setBoundaryConstraint(new ClampingBoundaryConstraint());
        adaptorFFA.setIterationStrategy(new NonEvaluatingFireflyIterationStrategy());
    }

    public FireflyAdaptationStrategy(FireflyAdaptationStrategy copy){
        this.adaptorFFA = copy.adaptorFFA.getClone();
    }


    @Override
    public void adapt(PSO algorithm) {

        final F<Particle, Firefly> create = new F<Particle, Firefly>() {
            @Override
            public Firefly f(Particle p) {
                StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
                SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider)behaviour.getVelocityProvider();

                //create a new vector as [w, c1, c2] from the current particles parameter set
                Vector.Builder builder = Vector.newBuilder();
                builder.add(Real.valueOf(provider.getInertiaWeight().getParameter(), provider.getParameterSet().getInertiaBounds()));
                builder.add(Real.valueOf(provider.getCognitiveAcceleration().getParameter(), provider.getParameterSet().getCognitiveBounds()));
                builder.add(Real.valueOf(provider.getSocialAcceleration().getParameter(), provider.getParameterSet().getSocialBounds()));

                Firefly newFirefly = new StandardFirefly();
                newFirefly.setPosition(builder.build());
                newFirefly.put(Property.FITNESS, provider.getParameterSet().getFitness());
                return newFirefly;
            }
        };

        adaptorFFA.setTopology(algorithm.getTopology().map(create));
        adaptorFFA.performIteration();

        //iterate through fireflies of the adaptorFFA and update the parameters of the original PSO accordingly
        for(int i = 0; i < algorithm.getTopology().length(); i++){
            Particle p = algorithm.getTopology().index(i);
            StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
            SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider)behaviour.getVelocityProvider();

            Firefly parameterFirefly = adaptorFFA.getTopology().index(i);
            Vector parameterPosition = parameterFirefly.getPosition();
            provider.setInertiaWeight(ConstantControlParameter.of(parameterPosition.doubleValueOf(0)));
            provider.setCognitiveAcceleration(ConstantControlParameter.of(parameterPosition.doubleValueOf(1)));
            provider.setSocialAcceleration(ConstantControlParameter.of(parameterPosition.doubleValueOf(2)));
        }
    }

    @Override
    public FireflyAdaptationStrategy getClone() {
        return new FireflyAdaptationStrategy(this);
    }

}