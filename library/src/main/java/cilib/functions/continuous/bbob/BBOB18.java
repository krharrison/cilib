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
 * F18: Schaffers F7 Function, moderately ill-conditioned
 */
public class BBOB18 extends AbstractBBOB {
	private AsymmetricFunctionDecorator asymmetric;
	private IllConditionedFunctionDecorator ill;
	private RotatedFunctionDecorator r, q;
	private Penalty pen;

	public BBOB18() {
		this.ill = Helper.newIllConditioned(1000, new Schaffer7());
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