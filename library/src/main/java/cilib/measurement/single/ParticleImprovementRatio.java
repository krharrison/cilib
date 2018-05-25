/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.entity.Property;
import cilib.measurement.Measurement;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.type.types.Real;


/**
 * Calculates the ratio of particles which improved their fitness
 * in this iteration.
 */
public class ParticleImprovementRatio implements Measurement<Real>{
    @Override
    public Measurement<Real> getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        PSO pso = (PSO) algorithm;

        int numberOfImprovements = 0;

        //an improvement is when the fitness has improved
        for(Particle p : pso.getTopology())
        {
            if(p.getFitness().compareTo(p.get(Property.PREVIOUS_FITNESS)) > 0) numberOfImprovements++;
        }

        return Real.valueOf((double) numberOfImprovements / (double) pso.getTopology().length());
    }
}
