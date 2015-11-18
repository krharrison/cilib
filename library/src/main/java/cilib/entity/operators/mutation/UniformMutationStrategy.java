/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.entity.operators.mutation;

import java.util.List;
import java.util.ListIterator;
import cilib.controlparameter.ControlParameter;
import cilib.controlparameter.ProportionalControlParameter;
import cilib.entity.Entity;
import cilib.type.types.container.Vector;

/**
 */
public class UniformMutationStrategy extends MutationStrategy {

    private static final long serialVersionUID = -3951730432882403768L;
    private final ControlParameter minStrategy, maxStrategy;

    public UniformMutationStrategy() {
        super();
        minStrategy = new ProportionalControlParameter();
        maxStrategy = new ProportionalControlParameter();
    }

    public UniformMutationStrategy(UniformMutationStrategy copy) {
        super(copy);
        this.minStrategy = copy.minStrategy.getClone();
        this.maxStrategy = copy.maxStrategy.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniformMutationStrategy getClone() {
        return new UniformMutationStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Entity> List<E> mutate(List<E> entity) {
        for (ListIterator<? extends Entity> individual = entity.listIterator(); individual.hasNext();) {
            Entity current = individual.next();
            Vector chromosome = (Vector) current.getPosition();

            if (this.getMutationProbability().getParameter() >= this.getRandomDistribution().getRandomNumber()) {
                for (int i = 0; i < chromosome.size(); i++) {
                    double value = this.getOperatorStrategy().evaluate(chromosome.doubleValueOf(i), this.getRandomDistribution().getRandomNumber(minStrategy.getParameter(), maxStrategy.getParameter()));
                    chromosome.setReal(i, value);
                }
            }
        }
        return entity;
    }
}
