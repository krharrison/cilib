/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive.detectionstrategies;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Int;

public class PersonalBestStagnationDetectionStrategy implements ParameterUpdateTriggerDetectionStrategy {

	 private ControlParameter windowSize;

	    /**
	     * Construct a new {@link PersonalBestStagnationDetectionStrategy} with a
	     * default window size of 10.
	     */
	    public PersonalBestStagnationDetectionStrategy() {
	        windowSize = ConstantControlParameter.of(10);
	    }

	    /**
	     * Construct a copy of the given {@link PersonalBestStagnationDetectionStrategy}.
	     *
	     * @param copy the {@link PersonalBestStagnationDetectionStrategy} to copy.
	     */
	    public PersonalBestStagnationDetectionStrategy(PersonalBestStagnationDetectionStrategy copy) {
	        this.windowSize = copy.windowSize;
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public PersonalBestStagnationDetectionStrategy getClone() {
	        return new PersonalBestStagnationDetectionStrategy(this);
	    }

	    /**
	     * Checks whether a {@link Particle}'s personal best fitness has changed
	     * during a recent window of iterations (number of iterations set by
	     * <code>windowSize</code>). If it has not, the particle is assumed to
	     * be stagnating.
	     *
	     * @param entity The {@link Particle} to check for stagnation.
	     * @return True if the {@link Particle} is stagnating. False otherwise.
	     */
	    @Override
	    public boolean detect(Particle entity) {
	        int counter = ((Int)entity.get(Property.PBEST_STAGNATION_COUNTER)).intValue();
	        
	        if (counter > windowSize.getParameter()) {
	            entity.put(Property.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
	            return true;
	        }

	        return false;
	    }

	    public void setWindowSize(ControlParameter windowSize) {
	        this.windowSize = windowSize;
	    }

}
