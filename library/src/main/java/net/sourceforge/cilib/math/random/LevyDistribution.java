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

public class LevyDistribution implements ProbabilityDistributionFunction {

	private ControlParameter shape;
	private ControlParameter scale;
	
	public LevyDistribution(){
		shape = ConstantControlParameter.of(1.0);
		scale = ConstantControlParameter.of(1.0);
	}
	
	@Override
	public double getRandomNumber() {
		return getRandomNumber(shape.getParameter(), scale.getParameter());
	}

    /**
     * Return a random number sampled from the Levy distribution.
     * Two parameters are required. The first specifies the shape, the second specifies the scale.
     * @param parameters the shape and scale of the distribution.
     * @return A Levy distributed number specified by {@code shape} and {@code scale}.
     */
	@Override
	public double getRandomNumber(double... parameters) {
		checkArgument(parameters.length == 2, "The Levy distribution requires two parameters.");
		checkArgument(parameters[0] > 0, "The shape parameter must be greater than zero.");
        checkArgument(parameters[1] > 0, "The scale parameter must be greater than zero.");
		
		double alpha = parameters[0]; //shape
		double c = parameters[1]; 	  //scale

		double u, v, t, s;

		u = Math.PI * (Rand.nextDouble() - 0.5);

		if (alpha == 1) // CAUCHY
		{
			t = Math.tan(u);

			return c * t;
		}

		do {
			v = -Math.log(Rand.nextDouble());
		} while (v == 0);

		if (alpha == 2) // GAUSSIAN
		{
			t = 2 * Math.sin(u) * Math.sqrt(v);

			return c * t;
		}

		// GENERAL CASE
		t = Math.sin(alpha * u) / Math.pow(Math.cos(u), 1 / alpha);
		s = Math.pow(Math.cos((1 - alpha) * u) / v, (1 - alpha) / alpha);

		return c * t * s;
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
