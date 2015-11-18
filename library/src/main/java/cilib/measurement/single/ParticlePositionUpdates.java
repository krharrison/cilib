/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import fj.F;
import fj.function.Integers;
import cilib.algorithm.Algorithm;
import cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Property;
import cilib.measurement.Measurement;
import cilib.niching.NichingAlgorithm;
import cilib.pso.particle.Particle;
import cilib.type.types.Int;

public class ParticlePositionUpdates implements Measurement<Int> {
	private Integer updates = 0;

    @Override
    public Measurement<Int> getClone() {
        return this;
    }

    @Override
    public Int getValue(Algorithm algorithm) {
        updates = 0;
        if (algorithm instanceof SinglePopulationBasedAlgorithm) {
        	updates += posUpdateCount((SinglePopulationBasedAlgorithm<Particle>) algorithm);
            return Int.valueOf(updates);
        } else {
            MultiPopulationBasedAlgorithm psos = (MultiPopulationBasedAlgorithm) algorithm;
            for (SinglePopulationBasedAlgorithm sPop : psos.getPopulations()) {
                updates += posUpdateCount(sPop);
            }
            
            if (algorithm instanceof NichingAlgorithm) {
            	updates += posUpdateCount(((NichingAlgorithm) algorithm).getMainSwarm());
            }

            return Int.valueOf(updates);
        }
    }

    private int posUpdateCount(SinglePopulationBasedAlgorithm<Particle> algorithm) {
        return Integers.sum(algorithm.getTopology().map(new F<Particle, Integer>() {
            @Override
            public Integer f(Particle a) {
            	int b = a.get(Property.POSITION_UPDATE_COUNTER).intValue();
                return b;
            }            
        }));
    }
}
