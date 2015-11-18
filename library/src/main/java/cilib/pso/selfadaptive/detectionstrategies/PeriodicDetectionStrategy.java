/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.detectionstrategies;

import cilib.algorithm.AbstractAlgorithm;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.pso.particle.Particle;

public class PeriodicDetectionStrategy implements ParameterUpdateDetectionStrategy {
    private ControlParameter period;

    public PeriodicDetectionStrategy() {
        period = ConstantControlParameter.of(30.0);
    }

    /**
     * Construct a copy of the given {@link PeriodicDetectionStrategy}.
     *
     * @param copy the {@link PeriodicDetectionStrategy} to copy.
     */
    public PeriodicDetectionStrategy(PeriodicDetectionStrategy copy) {
        this.period = copy.period.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PeriodicDetectionStrategy getClone() {
        return new PeriodicDetectionStrategy(this);
    }

    @Override
    public boolean detect(Particle entity) {
        int iters = AbstractAlgorithm.get().getIterations();

        if (iters % period.getParameter() == 0) {
            return true;
        }

        return false;
    }

    public ControlParameter getPeriod() {
        return period;
    }

    public void setPeriod(ControlParameter period) {
        this.period = period;
    }

}
