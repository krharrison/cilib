/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.behaviour;

import cilib.entity.Entity;
import cilib.entity.behaviour.AbstractBehaviour;
import cilib.pso.particle.Particle;
import cilib.pso.positionprovider.PositionProvider;
import cilib.pso.positionprovider.StandardPositionProvider;
import cilib.pso.velocityprovider.StandardVelocityProvider;
import cilib.pso.velocityprovider.VelocityProvider;

/**
 * Behaviour representing normal particle behaviour sans fitness update. The behaviour is:
 * 1-update velocity
 * 2-update position
 * 3-enforce boundary constraints
 */
public class NonEvaluatingStandardParticleBehaviour extends AbstractBehaviour {

    private PositionProvider positionProvider;
    private VelocityProvider velocityProvider;

    /**
     * Default constructor assigns standard position and velocity provider.
     */
    public NonEvaluatingStandardParticleBehaviour() {
        this.positionProvider = new StandardPositionProvider();
        this.velocityProvider = new StandardVelocityProvider();
    }

    /**
     * Copy Constructor.
     *
     * @param copy The {@link NonEvaluatingStandardParticleBehaviour} object to copy.
     */
    public NonEvaluatingStandardParticleBehaviour(NonEvaluatingStandardParticleBehaviour copy) {
        super(copy);

        this.positionProvider = copy.positionProvider.getClone();
        this.velocityProvider = copy.velocityProvider.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NonEvaluatingStandardParticleBehaviour getClone() {
        return new NonEvaluatingStandardParticleBehaviour(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Particle performIteration(Entity entity) {

		((Particle) entity).updateVelocity(velocityProvider.get((Particle) entity));
        ((Particle) entity).updatePosition(positionProvider.get((Particle) entity));

        boundaryConstraint.enforce(entity);

        return (Particle) entity;
    }
    
    /**
     * Get the currently set {@link PositionProvider}.
     *
     * @return The current {@link PositionProvider}.
     */
    public PositionProvider getPositionProvider() {
        return positionProvider;
    }

    /**
     * Set the {@link PositionProvider}.
     *
     * @param positionProvider The {@link PositionProvider} to set.
     */
    public void setPositionProvider(PositionProvider positionProvider) {
        this.positionProvider = positionProvider;
    }

    /**
     * Get the currently set {@link VelocityProvider}.
     *
     * @return The current {@link VelocityProvider}.
     */
    public VelocityProvider getVelocityProvider() {
        return this.velocityProvider;
    }

    /**
     * Set the {@link VelocityProvider}.
     *
     * @param velocityProvider The {@link VelocityProvider} to set.
     */
    public void setVelocityProvider(VelocityProvider velocityProvider) {
        this.velocityProvider = velocityProvider;
    }
}