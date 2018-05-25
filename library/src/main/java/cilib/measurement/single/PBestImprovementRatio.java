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
 * Calculates the ratio of particles which improved their personal best position
 * in this iteration.
 */
public class PBestImprovementRatio implements Measurement<Real> {
    @Override
    public Measurement<Real> getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        PSO pso = (PSO) algorithm;

        int numberOfImprovements = 0;

        //an improvement is when the personal best stagnation counter is 0.
        for(Particle p : pso.getTopology())
        {
            if(p.get(Property.PBEST_STAGNATION_COUNTER).intValue() == 0) numberOfImprovements++;
        }

        return Real.valueOf((double) numberOfImprovements / (double) pso.getTopology().length());
    }
}
