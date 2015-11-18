/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.velocityprovider;

import fj.P1;
import cilib.algorithm.AbstractAlgorithm;
import cilib.math.random.GaussianDistribution;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.type.types.container.Vector;

public final class RandomNearbyVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = 8204479765311251730L;

    private ProbabilityDistributionFunction random;

    /** Creates a new instance of StandardVelocityUpdate. */
    public RandomNearbyVelocityProvider() {
        this.random = new GaussianDistribution();
    }

    /**
     * Copy constructor.
     * @param copy The object to copy.
     */
    public RandomNearbyVelocityProvider(RandomNearbyVelocityProvider copy) {
        this.random = copy.random;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomNearbyVelocityProvider getClone() {
        return new RandomNearbyVelocityProvider(this);
    }

    /**
     * Perform the velocity update for the given <tt>Particle</tt>.
     * @param particle The Particle velocity that should be updated.
     */
    @Override
    public Vector get(Particle particle) {
        fj.data.List<Particle> topology = ((PSO) AbstractAlgorithm.get()).getTopology();
        Vector average = Vector.fill(0.0, particle.getDimension());

        for(Particle p : topology) {
            average = average.plus((Vector) p.getVelocity());
        }

        average = average.divide(topology.length());

        average.multiply(new P1<Number>() {
            @Override
            public Number _1() {
                return random.getRandomNumber();
            }
        });

        return average;
    }

    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }

    public ProbabilityDistributionFunction getRandom() {
        return random;
    }
}
