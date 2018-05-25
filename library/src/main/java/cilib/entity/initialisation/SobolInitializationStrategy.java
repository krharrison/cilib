package cilib.entity.initialisation;

import cilib.entity.Entity;
import cilib.entity.Property;
import cilib.math.random.generator.Rand;
import cilib.math.random.generator.quasi.Sobol;
import cilib.type.types.container.Vector;
import cilib.type.types.Bounds;

public class SobolInitializationStrategy <E extends Entity> implements InitialisationStrategy<E> {


    static Sobol generator;

    @Override
    public InitialisationStrategy getClone() {
        return this;
    }

    @Override
    public void initialise(Property key, E entity) {
        Vector type = (Vector) entity.get(key);

        int size = type.size();

        if(generator == null){
            generator = new Sobol(Rand.nextLong());
            generator.setDimensions(size);
        }

        double[] point = generator.nextPoint();

        for(int i = 0; i < size; i++){
            Bounds bounds = type.get(i).getBounds();
            type.setReal(i, point[i] * bounds.getRange() + bounds.getLowerBound());
        }
    }
}
