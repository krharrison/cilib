/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.entity.operators.crossover.parentprovider;

import com.google.common.base.Preconditions;
import java.util.List;
import cilib.entity.Entity;

public class FirstParentProvider extends ParentProvider {

	@Override
	public ParentProvider getClone() {
		return this;
	}

	public Entity f(List<Entity> a) {
		Preconditions.checkArgument(!a.isEmpty(), "Cannot select parent from empty list.");
		return a.get(0);
	}
}
