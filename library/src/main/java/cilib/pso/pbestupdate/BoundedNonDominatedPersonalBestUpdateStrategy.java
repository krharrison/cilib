/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.pbestupdate;

import cilib.entity.Property;
import cilib.pso.particle.Particle;
import cilib.problem.solution.InferiorFitness;
import cilib.type.types.Types;

/**
 * If the current fitness is better than the best fitness, or both are
 * non-dominated, update the best fitness to the current fitness.
 */
public class BoundedNonDominatedPersonalBestUpdateStrategy extends NonDominatedPersonalBestUpdateStrategy {

   /**
    * {@inheritDoc}
    */
    @Override
    public PersonalBestUpdateStrategy getClone() {
        return this;
    }

    /**
     * If the current fitness is better than the best fitness, or both are
     * non-dominated, update the best fitness to the current fitness.
     * <p>
     * If the current fitness is not updated, increase the particle's pbest
     * stagnation counter.
     *
     * @param particle The particle to update.
     */
    @Override
    public void updatePersonalBest(Particle particle) {
        if (!Types.isInsideBounds(particle.getPosition())) {
            particle.put(Property.FITNESS, InferiorFitness.instance());
            return;
        }

        super.updatePersonalBest(particle);
    }
}
