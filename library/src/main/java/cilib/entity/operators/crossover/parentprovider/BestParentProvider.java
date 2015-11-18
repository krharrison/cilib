/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.entity.operators.crossover.parentprovider;

import java.util.List;
import cilib.entity.Entity;
import cilib.util.selection.recipes.ElitistSelector;

public class BestParentProvider extends ParentProvider {

	@Override
	public ParentProvider getClone() {
		return this;
	}

	public Entity f(List<Entity> a) {
		return new ElitistSelector<Entity>().on(a).select();
	}
}
