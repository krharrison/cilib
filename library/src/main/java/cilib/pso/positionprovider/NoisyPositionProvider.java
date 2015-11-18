/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.positionprovider;

import cilib.math.random.GaussianDistribution;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.pso.particle.Particle;
import cilib.type.types.container.Vector;

/**
 * Decorates a {@link PositionProvider} with random noise from any
 * {@link ProbabilityDistributionFunction}.
 */
public class NoisyPositionProvider implements PositionProvider {

    private static final long serialVersionUID = -2665293187543545962L;
    private ProbabilityDistributionFunction distribution;
    private PositionProvider delegate;

    public NoisyPositionProvider() {
        this.distribution = new GaussianDistribution();
        this.delegate = new StandardPositionProvider();
    }

    public NoisyPositionProvider(NoisyPositionProvider rhs) {
        this.distribution = rhs.distribution;
        this.delegate = rhs.delegate.getClone();
    }

    @Override
    public Vector get(Particle particle) {
        Vector position = (Vector) this.delegate.get(particle);
        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); i++) {
            builder.add(this.distribution.getRandomNumber());
        }
        return position.plus(builder.build());
    }

    @Override
    public NoisyPositionProvider getClone() {
        return new NoisyPositionProvider(this);
    }

    public PositionProvider getDelegate() {
        return this.delegate;
    }

    public void setDelegate(PositionProvider delegate) {
        this.delegate = delegate;
    }

    public ProbabilityDistributionFunction getDistribution() {
        return this.distribution;
    }

    public void setDistribution(ProbabilityDistributionFunction distribution) {
        this.distribution = distribution;
    }
}
