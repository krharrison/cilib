/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.detectionstrategies.particle;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.entity.Property;
import cilib.pso.particle.Particle;
import cilib.type.types.Int;

public class PBestStagnationDetectionStrategy implements ParticleUpdateDetectionStrategy {
    private ControlParameter windowSize;

    public PBestStagnationDetectionStrategy() {
        windowSize = ConstantControlParameter.of(5.0);
    }

    /**
     * Construct a copy of the given {@link PBestStagnationDetectionStrategy}.
     *
     * @param copy the {@link PBestStagnationDetectionStrategy} to copy.
     */
    public PBestStagnationDetectionStrategy(PBestStagnationDetectionStrategy copy) {
        this.windowSize = copy.windowSize.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PBestStagnationDetectionStrategy getClone() {
        return new PBestStagnationDetectionStrategy(this);
    }

    @Override
    public boolean detect(Particle entity) {

        int counter = entity.get(Property.PBEST_STAGNATION_COUNTER).intValue();

        if (counter >= windowSize.getParameter()) {
            entity.put(Property.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
            return true;
        }

        return false;
    }

    public ControlParameter getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(ControlParameter windowSize) {
        this.windowSize = windowSize;
    }

}
