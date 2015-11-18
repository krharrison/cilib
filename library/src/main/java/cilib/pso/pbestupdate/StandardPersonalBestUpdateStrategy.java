/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.pbestupdate;

import cilib.entity.Property;
import cilib.pso.particle.Particle;
import cilib.type.types.Int;

/**
 * Update the personal best of the particle, based on the standard PSO definition
 * of the process.
 *
 */
public class StandardPersonalBestUpdateStrategy implements PersonalBestUpdateStrategy {

    private static final long serialVersionUID = 266386833476786081L;

    /**
     * {@inheritDoc}
     */
    @Override
    public PersonalBestUpdateStrategy getClone() {
        return this;
    }

    /**
     * If the current fitness is better than the current best fitness, update
     * the best fitness of the particle to equal the current fitness and make
     * the personal best position a clone of the current particle position.
     *
     * If the current fitness is not better than the current best fitness,
     * increase the particle's pbest stagnation counter.
     *
     * @param particle The particle to update.
     */
    @Override
    public void updatePersonalBest(Particle particle) {
        if (particle.getFitness().compareTo(particle.getBestFitness()) > 0) {
            particle.getBehaviour().incrementSuccessCounter();
            particle.put(Property.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
            particle.put(Property.BEST_FITNESS, particle.getFitness());
            particle.put(Property.BEST_POSITION, particle.getPosition().getClone());
            return;
        }

        //PBest didn't change. Increment stagnation counter.
        int count = ((Int)particle.get(Property.PBEST_STAGNATION_COUNTER)).intValue();
        particle.put(Property.PBEST_STAGNATION_COUNTER,  Int.valueOf(++count));
    }
}
