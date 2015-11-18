/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.stoppingcondition;

import cilib.algorithm.Algorithm;
import cilib.measurement.single.diversity.Diversity;

/**
 * A stopping condition that defines an algorithm to be complete if a given stopping condition has been maintained
 * for a number of consecutive iterations.
 */
public class MaintainedStoppingCondition extends StoppingCondition {

    private int consecutiveIterations;
    private int count;
    private StoppingCondition condition;
    private double percentage;

    public MaintainedStoppingCondition() {
        this(new MeasuredStoppingCondition(new Diversity(), new Minimum(), 1.0), 10);
    }

    public MaintainedStoppingCondition(MaintainedStoppingCondition rhs) {
        this.consecutiveIterations = rhs.consecutiveIterations;
        this.condition = rhs.condition.getClone();
        this.count = rhs.count;
        this.percentage = rhs.percentage;
    }

    public MaintainedStoppingCondition(StoppingCondition condition, int consecutiveIterations) {
        this.consecutiveIterations = consecutiveIterations;
        this.condition = condition;
        this.count = 0;
        this.percentage = 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MaintainedStoppingCondition getClone() {
        return new MaintainedStoppingCondition(this);
    }

    @Override
    public double getPercentageCompleted(Algorithm algorithm) {
        percentage = Math.max(percentage, count / (double) consecutiveIterations);
        return percentage;
    }

    @Override
    public Boolean f(Algorithm algorithm) {
        count = condition.f(algorithm) ? count + 1 : 0;
        return count >= consecutiveIterations;
    }

    public StoppingCondition getCondition() {
        return condition;
    }

    public int getConsecutiveIterations() {
        return consecutiveIterations;
    }

    public void setConsecutiveIterations(int consecutiveIterations) {
        this.consecutiveIterations = consecutiveIterations;
    }

    public void setCondition(StoppingCondition condition) {
        this.condition = condition;
    }
}
