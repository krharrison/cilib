/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies;

import cilib.entity.behaviour.generator.BehaviourGenerator;
import cilib.pso.PSO;
//import cilib.pso.behaviour.generators.StandardVelocityProviderBehaviourGenerator;
import cilib.pso.particle.Particle;

public class RandomRegenerationAdaptationStrategy implements AdaptationStrategy{
    //protected BehaviourGenerator generator;

    public RandomRegenerationAdaptationStrategy(){
        //generator = new StandardVelocityProviderBehaviourGenerator();
    }

    public RandomRegenerationAdaptationStrategy(RandomRegenerationAdaptationStrategy copy){
        //this.generator = copy.generator.getClone();
    }

    @Override
    public void adapt(Particle p, PSO algorithm) {
      //  p.setBehaviour(generator.generate());
    }

    @Override
    public AdaptationStrategy getClone() {
        return new RandomRegenerationAdaptationStrategy(this);
    }

//    public void setBehaviourGenerator(BehaviourGenerator generator){
  //      this.generator = generator;
  //  }
}
