/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.pbestupdate;

import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.pso.particle.ExtendedParticle;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Types;

/**
 * Update the personal best of the particle, if it is a valid update. Valid updates are
 * defined to be only within the problem search space. Any particle drifting into an
 * infeasible part of the search space will be allowed to do so, however, any solutions
 * found will not allowed to become personal best positions.
 *
 */
public class ExtendedParticleBoundedPersonalBestUpdateStrategy implements PersonalBestUpdateStrategy {

	private PersonalBestUpdateStrategy delegate;

    public ExtendedParticleBoundedPersonalBestUpdateStrategy() {
        this.delegate = new StandardPersonalBestUpdateStrategy();
    }

    public ExtendedParticleBoundedPersonalBestUpdateStrategy(ExtendedParticleBoundedPersonalBestUpdateStrategy copy) {
        this.delegate = copy.delegate.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedParticleBoundedPersonalBestUpdateStrategy getClone() {
        return new ExtendedParticleBoundedPersonalBestUpdateStrategy(this);
    }

    /**
     * Update personal best if and only if the particle's solution is within the bounds of the
     * search space / problem.
     * @param particle The particle to update.
     */
    @Override
    public void updatePersonalBest(Particle particle) {
    	ExtendedParticle extParticle = (ExtendedParticle) particle;
        if (!Types.isInsideBounds(extParticle.getSolution())) {
            particle.put(Property.FITNESS, InferiorFitness.instance());
            return;
        }

        delegate.updatePersonalBest(particle);
    }

    public void setDelegate(PersonalBestUpdateStrategy delegate) {
        this.delegate = delegate;
    }

    public PersonalBestUpdateStrategy getDelegate() {
        return delegate;
    }
}
