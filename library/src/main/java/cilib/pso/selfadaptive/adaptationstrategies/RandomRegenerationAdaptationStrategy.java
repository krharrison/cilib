/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies;

import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.parametersetgenerator.ConvergentParameterSetGenerator;
import cilib.pso.selfadaptive.parametersetgenerator.ParameterSetGenerator;

public class RandomRegenerationAdaptationStrategy implements SwarmAdaptationStrategy {
    private ParameterSetGenerator parameterGenerator;

    public RandomRegenerationAdaptationStrategy(){
        parameterGenerator = new ConvergentParameterSetGenerator();
    }

    public RandomRegenerationAdaptationStrategy(RandomRegenerationAdaptationStrategy copy){
        this.parameterGenerator = copy.parameterGenerator.getClone();
    }

    @Override
    public void adapt(PSO algorithm) {
        for(Particle p : algorithm.getTopology()) {
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            sp.setParameterSet(parameterGenerator.generate());
        }
    }

    @Override
    public SwarmAdaptationStrategy getClone() {
        return new RandomRegenerationAdaptationStrategy(this);
    }

    public void setParameterGenerator(ParameterSetGenerator parameterGenerator){
        this.parameterGenerator = parameterGenerator;
    }
}
