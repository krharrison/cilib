/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.measurement.single.selfadaptivePSO;

import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.type.types.Real;

/**
 * Calculates the number of particles in the current swarm that
 * contain parameters which exhibit convergent behaviour.
 */
public class ConvergentParticleRatio implements Measurement<Real> {
    @Override
    public Measurement<Real> getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        PSO pso = (PSO) algorithm;
        int count = 0;

        for(Particle p : pso.getTopology()){

            if(!(p instanceof SelfAdaptiveParticle)) return Real.valueOf(-1.0);
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;

            if(sp.getParameterSet().isConvergent()) count++;
        }

        return Real.valueOf((double) count / pso.getTopology().length());
    }
}
