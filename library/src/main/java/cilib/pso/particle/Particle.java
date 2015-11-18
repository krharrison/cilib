/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.particle;

import cilib.entity.Entity;
import cilib.entity.MemoryBasedEntity;
import cilib.entity.SocialEntity;
import cilib.entity.initialisation.InitialisationStrategy;
import cilib.pso.pbestupdate.PersonalBestUpdateStrategy;
import cilib.type.types.Type;
import cilib.type.types.container.StructuredType;

/**
 * Definition of a particle.
 */
public interface Particle extends Entity, SocialEntity, MemoryBasedEntity {

    /**
     * {@inheritDoc}
     */
    @Override
    Particle getClone();

    /**
     * Get the current velocity of the {@linkplain Particle}.
     * @return The {@linkplain Type} representing the velocity.
     */
    StructuredType getVelocity();

    /**
     * Get the current best particle within the {@linkplain Particle}s neighbourhood.
     * @return The neighbourhood best particle.
     */
    Particle getNeighbourhoodBest();

    /**
     * Set the current neighbourhood best particle within the current neighbourhood.
     * @param particle The particle to set as the neighbourhood best.
     */
    void setNeighbourhoodBest(Particle particle);

    /**
     * Update the current position of the {@linkplain Particle}.
     */
    void updatePosition(StructuredType newPosition);

    /**
     * Update the velocity of the {@linkplain Particle}.
     */
    void updateVelocity(StructuredType newVelocity);

    /**
     * Get the strategy for the initialisation of the velocity.
     * @return The velocity {@linkplain InitialisationStrategy} that is currently set.
     */
    InitialisationStrategy getVelocityInitialisationStrategy();

    /**
     * Set the initialisation strategy for the {@linkplain Particle}s velocity.
     * @param velocityInitialisationStrategy The velocity initialisation strategy to set.
     */
    void setVelocityInitialisationStrategy(InitialisationStrategy velocityInitialisationStrategy);

    /**
     * Get the current strategy to perform personal best updates.
     * @return The current {@link PersonalBestUpdateStrategy}.
     */
    PersonalBestUpdateStrategy getPersonalBestUpdateStrategy();

    /**
     * Set the strategy to perform personal best updates.
     * @param personalBestUpdateStrategy The instance to set.
     */
    void setPersonalBestUpdateStrategy(PersonalBestUpdateStrategy personalBestUpdateStrategy);

}
