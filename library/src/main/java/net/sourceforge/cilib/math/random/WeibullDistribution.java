/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random;

import static com.google.common.base.Preconditions.checkArgument;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.random.generator.Rand;

public class WeibullDistribution implements ProbabilityDistributionFunction {

	private ControlParameter shape;
	private ControlParameter scale;
	
	public WeibullDistribution(){
		shape = ConstantControlParameter.of(1.0);
		scale = ConstantControlParameter.of(1.0);
	}
	
	@Override
	public double getRandomNumber() {
		return getRandomNumber(shape.getParameter(), scale.getParameter());
	}

    /**
     * Return a random number sampled from the Weibull distribution.
     * Two parameters are required. The first specifies the shape, the second specifies the scale.
     * @param parameters the shape and scale of the distribution.
     * @return A Weibull distributed number specified by {@code shape} and {@code scale}.
     */
	@Override
	public double getRandomNumber(double... parameters) {
		checkArgument(parameters.length == 2, "The Weibull distribution requires two parameters.");
		
		return parameters[1] * Math.pow(-Math.log(Rand.nextDouble()), parameters[0]);
	}
	
    public void setScale(ControlParameter scale) {
        this.scale = scale;
    }

    public ControlParameter getScale() {
        return scale;
    }

    public void setShape(ControlParameter shape) {
        this.shape = shape;
    }

    public ControlParameter getShape() {
        return shape;
    }

}
