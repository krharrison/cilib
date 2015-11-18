/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.bbob;

import cilib.functions.continuous.unconstrained.Spherical;
import cilib.type.types.container.Vector;

/*
 * F1: Sphere Function
 */
public class BBOB1 extends AbstractBBOB {
	private Spherical sphere;

	public BBOB1() {
		this.sphere = new Spherical();
	}

	@Override
	public Double f(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);
		return sphere.f(z) + fOpt;
	}
}