/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.bbob;

import cilib.functions.continuous.decorators.AsymmetricFunctionDecorator;
import cilib.functions.continuous.decorators.IllConditionedFunctionDecorator;
import cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import cilib.functions.continuous.unconstrained.Schaffer7;
import cilib.type.types.container.Vector;

/*
 * F17: Schaffers F7 Function
 */
public class BBOB17 extends AbstractBBOB {
	private AsymmetricFunctionDecorator asymmetric;
	private IllConditionedFunctionDecorator ill;
	private RotatedFunctionDecorator r, q;
	private Penalty pen;

	public BBOB17() {
		this.ill = Helper.newIllConditioned(10, new Schaffer7());
		this.q = Helper.newRotated(ill);
		this.asymmetric = Helper.newAsymmetric(0.5, q);
		this.r = Helper.newRotated(asymmetric);
		this.pen = Helper.newPenalty(5);
	}

	@Override
	public Double f(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);
		return r.f(z) + 10.0 * pen.f(input) + fOpt;
	}
}