/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.pbestupdate;

import fj.F;
import cilib.math.random.GaussianDistribution;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.pso.particle.Particle;
import cilib.type.types.Numeric;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;

public class MutatedDistinctPositionProvider extends DistinctPositionProvider {

    private ProbabilityDistributionFunction distribution;

    public MutatedDistinctPositionProvider() {
        this.distribution = new GaussianDistribution();
    }

    public MutatedDistinctPositionProvider(MutatedDistinctPositionProvider copy) {
        this.distribution = copy.distribution;
    }

    @Override
    public MutatedDistinctPositionProvider getClone() {
        return new MutatedDistinctPositionProvider(this);
    }

    @Override
    public Vector f(Particle particle) {
        return ((Vector) particle.getPosition()).map(new F<Numeric, Numeric>() {
            public Numeric f(Numeric input) {
                return Real.valueOf(input.doubleValue() + distribution.getRandomNumber(), input.getBounds());
            }
        });
    }

    public void setDistribution(ProbabilityDistributionFunction distribution) {
        this.distribution = distribution;
    }

    public ProbabilityDistributionFunction getDistribution() {
        return distribution;
    }
}
