/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies;


import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterSet;
import fj.F;
import cilib.controlparameter.ConstantControlParameter;
import cilib.entity.Property;
import cilib.ff.FFA;
import cilib.ff.firefly.Firefly;
import cilib.ff.firefly.StandardFirefly;
import cilib.ff.iterationstrategies.NonEvaluatingFireflyIterationStrategy;
import cilib.problem.boundaryconstraint.ClampingBoundaryConstraint;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;

public class FireflyAdaptationStrategy implements SwarmAdaptationStrategy {

    protected FFA adaptorFFA;

    public FireflyAdaptationStrategy(){
        adaptorFFA = new FFA();
        NonEvaluatingFireflyIterationStrategy iterationStrategy = new NonEvaluatingFireflyIterationStrategy();
        iterationStrategy.setBoundaryConstraint(new ClampingBoundaryConstraint());
        adaptorFFA.setIterationStrategy(iterationStrategy);
    }

    public FireflyAdaptationStrategy(FireflyAdaptationStrategy copy){
        this.adaptorFFA = copy.adaptorFFA.getClone();
    }


    @Override
    public void adapt(PSO algorithm) {

        final F<Particle, Firefly> create = new F<Particle, Firefly>() {
            @Override
            public Firefly f(Particle p) {
                SelfAdaptiveParticle p2 = (SelfAdaptiveParticle) p;

                Firefly newFirefly = new StandardFirefly();
                newFirefly.setPosition(p2.getParameterSet().asVector());
                newFirefly.put(Property.FITNESS, p2.getParameterSet().getFitness());
                return newFirefly;
            }
        };

        adaptorFFA.setTopology(algorithm.getTopology().map(create));
        adaptorFFA.performIteration();

        //iterate through fireflies of the adaptorFFA and update the parameters of the original PSO accordingly
        for(int i = 0; i < algorithm.getTopology().length(); i++){
            SelfAdaptiveParticle p = (SelfAdaptiveParticle) algorithm.getTopology().index(i);

            Firefly parameterFirefly = adaptorFFA.getTopology().index(i);
            Vector parameterPosition = parameterFirefly.getPosition();
            p.setParameterSet(ParameterSet.createFromVector(parameterPosition));
            //p.setInertiaWeight(ConstantControlParameter.of(parameterPosition.doubleValueOf(0)));
            //p.setCognitiveAcceleration(ConstantControlParameter.of(parameterPosition.doubleValueOf(1)));
            //p.setSocialAcceleration(ConstantControlParameter.of(parameterPosition.doubleValueOf(2)));
        }
    }

    @Override
    public FireflyAdaptationStrategy getClone() {
        return new FireflyAdaptationStrategy(this);
    }

}