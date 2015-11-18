/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.bbob;

import fj.F;
import cilib.functions.ContinuousFunction;
import cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import cilib.functions.continuous.decorators.IllConditionedFunctionDecorator;
import cilib.functions.continuous.decorators.IrregularFunctionDecorator;
import cilib.type.types.Numeric;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;

/*
 * F6: Attractive Sector Function
 */
public class BBOB6 extends AbstractBBOB {
	private RotatedFunctionDecorator r, q;
	private IllConditionedFunctionDecorator ill;

	public BBOB6() {
		this.q = Helper.newRotated(new Sector());
		this.ill = Helper.newIllConditioned(10, q);
		this.r = Helper.newRotated(ill);
	}

	@Override
	public Double f(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);
		return r.f(z) + fOpt;
	}

	private class Sector extends ContinuousFunction {
		F<Numeric, Numeric> irregularMapping;

		public Sector() {
			irregularMapping = new IrregularFunctionDecorator().getMapping();
		}

		@Override
		public Double f(Vector z) {
			double sum = 0;

			for (int i = 0; i < z.size(); i++) {
				double zi = z.doubleValueOf(i);
				double si = zi * xOpt.doubleValueOf(i) > 0 ? 100 : 1;

				sum += si * si * zi * zi;
			}

			return irregularMapping.f(Real.valueOf(Math.pow(sum, 0.9))).doubleValue();
		}
	}
}