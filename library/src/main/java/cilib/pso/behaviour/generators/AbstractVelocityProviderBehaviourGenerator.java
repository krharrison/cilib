/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.behaviour.generators;

import cilib.entity.behaviour.Behaviour;
import cilib.entity.behaviour.generator.BehaviourGenerator;
import cilib.pso.positionprovider.PositionProvider;
import cilib.pso.velocityprovider.generators.VelocityProviderGenerator;

/**
 *
 */
public abstract class AbstractVelocityProviderBehaviourGenerator implements BehaviourGenerator {

    protected PositionProvider positionProvider;
    protected VelocityProviderGenerator velocityProviderGenerator;
    /**
     *
     */
    @Override
    public abstract BehaviourGenerator getClone();

    /**
     *
     */
    @Override
    public abstract Behaviour generate();

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
     * Get the currently set {@link VelocityProviderGenerator}.
     *
     * @return The current {@link VelocityProviderGenerator}.
     */
    public VelocityProviderGenerator getVelocityProviderGenerator() {
        return velocityProviderGenerator;
    }

    /**
     * Set the {@link VelocityProviderGenerator}.
     *
     * @param velocityProviderGenerator The {@link VelocityProviderGenerator} to set.
     */
    public void setVelocityProviderGenerator(VelocityProviderGenerator velocityProviderGenerator) {
        this.velocityProviderGenerator = velocityProviderGenerator;
    }

}