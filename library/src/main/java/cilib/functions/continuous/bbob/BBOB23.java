/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.bbob;

import cilib.functions.continuous.unconstrained.Katsuura;
import cilib.functions.continuous.decorators.AsymmetricFunctionDecorator;
import cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import cilib.type.types.container.Vector;

/*
 * F23 Katsuura Function
 */
public class BBOB23 extends AbstractBBOB {
	private Katsuura katsuura;
	private AsymmetricFunctionDecorator asymetric;
	private RotatedFunctionDecorator r, q;
	private Penalty pen;

	public BBOB23() {
		katsuura = new Katsuura();
		q = Helper.newRotated(katsuura);
		asymetric = Helper.newAsymmetric(100, q);
		r = Helper.newRotated(asymetric);
		pen = new Penalty();
	}

	@Override
	public Double f(Vector input) {
		initialise(input.size());

		return r.f(input.subtract(xOpt)) + pen.f(input) + fOpt;
	}
}