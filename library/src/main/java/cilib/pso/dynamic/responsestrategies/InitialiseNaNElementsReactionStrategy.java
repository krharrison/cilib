/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.dynamic.responsestrategies;

import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Entity;
import cilib.pso.dynamic.DynamicParticle;
import cilib.pso.particle.Particle;
import cilib.type.types.container.Vector;

/**
 * This reaction strategy initialises new dimensions introduced into the
 * particles. These new dimensions are indicated by initially having the value
 * Double.NaN.
 *
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */
public class InitialiseNaNElementsReactionStrategy<E extends SinglePopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy {

    public InitialiseNaNElementsReactionStrategy() {
    }

    public InitialiseNaNElementsReactionStrategy(InitialiseNaNElementsReactionStrategy<E> rhs) {
        super(rhs);
    }

    @Override
    public InitialiseNaNElementsReactionStrategy<E> getClone() {
        return new InitialiseNaNElementsReactionStrategy<E>(this);
    }

    /**
     * Initialise the dimensions that have the value of Double.NaN. This is done
     * for all the particles in the topology.
     *
     * {@inheritDoc}
     */
    @Override
    public <P extends Particle, A extends SinglePopulationBasedAlgorithm<P>> void performReaction(A algorithm) {
        fj.data.List<? extends Entity> entities = algorithm.getTopology();

        for (Entity entity : entities) {
            DynamicParticle particle = (DynamicParticle) entity;
            //initialise position
            Vector position = (Vector) particle.getPosition();
            for (int curElement = 0; curElement < position.size(); ++curElement) {
                if (Double.isNaN(position.doubleValueOf(curElement))) {
                    position.get(curElement).randomise();
                }
            }

            //initialise personal best
            Vector personalBest = (Vector) particle.getBestPosition();
            for (int curElement = 0; curElement < position.size(); ++curElement) {
                if (Double.isNaN(personalBest.doubleValueOf(curElement))) {
                    personalBest.setReal(curElement, position.doubleValueOf(curElement));
                }
            }

            //initialise velocity
            Vector velocity = particle.getVelocity();
            for (int curElement = 0; curElement < position.size(); ++curElement) {
                if (Double.isNaN(velocity.doubleValueOf(curElement))) {
                    velocity.setReal(curElement, 0.0);
                }
            }
        }
    }
}
