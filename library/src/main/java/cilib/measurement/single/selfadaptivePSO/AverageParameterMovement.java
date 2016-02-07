/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single.selfadaptivePSO;

import cilib.algorithm.Algorithm;
import cilib.entity.Property;
import cilib.measurement.Measurement;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;

public class AverageParameterMovement implements Measurement<Real> {

    public AverageParameterMovement(){ }

    @Override
    public Measurement<Real> getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        PSO pso = (PSO) algorithm;

        double sum = 0;

        for(Particle p : pso.getTopology()){

            if(!p.has(Property.PREVIOUS_PARAMETERS))  return Real.valueOf(0);

            Vector params = ((SelfAdaptiveParticle) p).getParameterSet().asVector();
            Vector previousParams = (Vector) p.get(Property.PREVIOUS_PARAMETERS);

            sum += params.subtract(previousParams).norm();
        }

        return Real.valueOf(sum / pso.getTopology().length()); //TODO: remove hardcoding of the number of parameters
    }
}
