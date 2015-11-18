/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.util.selection.arrangement;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import cilib.math.random.generator.Rand;
import cilib.util.selection.WeightedObject;

public class ProportionalArrangement<T> implements Arrangement<WeightedObject> {

    @Override
    public Iterable<WeightedObject> arrange(Iterable<WeightedObject> elements) {
        List<WeightedObject> weightedObjects = (List<WeightedObject>) Lists.newArrayList(elements);
        
        double total = 0.0;
        for (WeightedObject weighedObject : weightedObjects) {
            total += weighedObject.getWeight();
        }

        if (Double.compare(total, 0.0) == 0) {
            return Lists.newArrayList();
        }

        List<WeightedObject> temp = Lists.newArrayList();
        while (weightedObjects.size() > 0) {
            double randomValue = Rand.nextDouble() * total;
            double marker = 0.0;
            int i = 0;
            do {
                marker += weightedObjects.get(i++).getWeight();
            } while (i < weightedObjects.size() && marker < randomValue);

            WeightedObject selected = weightedObjects.get(i - 1);
            temp.add(selected);
            weightedObjects.remove(i - 1);
            total -= selected.getWeight();
        }

        // The reverse is needed as largest
        // elements were added to the front.
        Collections.reverse(temp);
        weightedObjects.addAll(temp);
        return weightedObjects;
    }
}
