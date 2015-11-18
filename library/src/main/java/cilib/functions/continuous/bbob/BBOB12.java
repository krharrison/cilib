/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.bbob;

import cilib.functions.ContinuousFunction;
import cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import cilib.functions.continuous.decorators.AsymmetricFunctionDecorator;
import cilib.functions.continuous.unconstrained.BentCigar;
import cilib.type.types.container.Vector;

/*
 * F12: Bent Cigar Function
 */
public class BBOB12 extends AbstractBBOB {
	private RotatedFunctionDecorator r;
	private AsymmetricFunctionDecorator asymmetric;

	public BBOB12() {
		this.r = new RotatedFunctionDecorator();
		this.asymmetric = Helper.newAsymmetric(0.5, new Inner());
	}

	@Override
	public Double f(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);

		r.setFunction(asymmetric);
		return r.f(z) + fOpt;
	}

	private class Inner extends ContinuousFunction {
		private BentCigar bentCigar;

		public Inner() {
			this.bentCigar = new BentCigar();
		}

		@Override
		public Double f(Vector input) {
			r.setFunction(bentCigar);
			return r.f(input);
		}
	}
}