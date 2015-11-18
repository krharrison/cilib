/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.bbob;

import cilib.functions.ContinuousFunction;
import cilib.functions.continuous.unconstrained.Rosenbrock;
import cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import cilib.type.types.container.Vector;

/*
 * F9: Rosenbrock Function, rotated
 */
public class BBOB9 extends AbstractBBOB {
	private RotatedFunctionDecorator r;

	public BBOB9() {
		this.r = Helper.newRotated(new Inner());
	}

	@Override
	public Double f(Vector input) {
		initialise(input.size());

		return r.f(input) + fOpt;
	}

	private class Inner extends ContinuousFunction {
		private Rosenbrock rosenbrock;

		public Inner() {
			this.rosenbrock = new Rosenbrock();
		}

		@Override
		public Double f(Vector x) {
			double factor = Math.max(1, Math.sqrt(x.size()) / 8);

			Vector z = x.multiply(factor).plus(Vector.fill(0.5, x.size()));
			return rosenbrock.f(z);
		}
	}
}