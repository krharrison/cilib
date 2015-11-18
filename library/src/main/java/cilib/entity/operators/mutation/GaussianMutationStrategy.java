/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.entity.operators.mutation;

import java.util.List;
import cilib.controlparameter.ControlParameter;
import cilib.controlparameter.ProportionalControlParameter;
import cilib.entity.Entity;
import cilib.math.random.GaussianDistribution;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.type.types.container.Vector;

/**
 */
public class GaussianMutationStrategy extends MutationStrategy {

    private static final long serialVersionUID = -4219155909474892419L;
    private double mean;
    private ControlParameter deviationStrategy;
    private final ProbabilityDistributionFunction gaussian;

    public GaussianMutationStrategy() {
        super();
        this.mean = 0;
        this.deviationStrategy = new ProportionalControlParameter();
        this.gaussian = new GaussianDistribution();
    }

    public GaussianMutationStrategy(GaussianMutationStrategy copy) {
        super(copy);
        this.mean = copy.mean;
        this.deviationStrategy = copy.deviationStrategy.getClone();
        this.gaussian = copy.gaussian;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GaussianMutationStrategy getClone() {
        return new GaussianMutationStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Entity> List<E> mutate(List<E> entity) {
        for (E current : entity) {
            Vector chromosome = (Vector) current.getPosition();

            for (int i = 0; i < chromosome.size(); i++) {
                double random = this.getRandomDistribution().getRandomNumber();
                if (random <= this.getMutationProbability().getParameter()) {
                    double deviation = this.gaussian.getRandomNumber();
                    double value = this.getOperatorStrategy().evaluate(chromosome.doubleValueOf(i), this.gaussian.getRandomNumber(this.mean, deviation));

                    chromosome.setReal(i, value);
                }
            }
        }
        return entity;
    }

    public ControlParameter getDeviationStrategy() {
        return deviationStrategy;
    }

    public void setDeviationStrategy(ControlParameter deviationStrategy) {
        this.deviationStrategy = deviationStrategy;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }
}
