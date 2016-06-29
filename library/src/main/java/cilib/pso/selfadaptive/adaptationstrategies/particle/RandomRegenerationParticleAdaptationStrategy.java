/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies.particle;

import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.parametersetgenerator.ConvergentParameterSetGenerator;
import cilib.pso.selfadaptive.parametersetgenerator.ParameterSetGenerator;

public class RandomRegenerationParticleAdaptationStrategy implements ParticleAdaptationStrategy {
    private ParameterSetGenerator parameterGenerator;

    public RandomRegenerationParticleAdaptationStrategy(){
        parameterGenerator = new ConvergentParameterSetGenerator();
    }

    public RandomRegenerationParticleAdaptationStrategy(RandomRegenerationParticleAdaptationStrategy copy){
        this.parameterGenerator = copy.parameterGenerator.getClone();
    }

    @Override
    public void adapt(Particle p, PSO algorithm) {
        SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
        sp.setParameterSet(parameterGenerator.generate());
    }

    @Override
    public ParticleAdaptationStrategy getClone() {
        return new RandomRegenerationParticleAdaptationStrategy(this);
    }

    public void setParameterGenerator(ParameterSetGenerator parameterGenerator){
        this.parameterGenerator = parameterGenerator;
    }
}
