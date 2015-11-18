/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.bbob;

import cilib.functions.continuous.decorators.IrregularFunctionDecorator;
import cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import cilib.functions.continuous.unconstrained.Elliptic;
import cilib.type.types.container.Vector;

/*
 * F10: Ellipsoidal Function
 */
public class BBOB10 extends AbstractBBOB {
	private RotatedFunctionDecorator r;
	private IrregularFunctionDecorator irregular;

	public BBOB10() {
		this.irregular = Helper.newIrregular(new Elliptic());
		this.r = Helper.newRotated(irregular);
	}

	@Override
	public Double f(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);
		return r.f(z) + fOpt;
	}
}