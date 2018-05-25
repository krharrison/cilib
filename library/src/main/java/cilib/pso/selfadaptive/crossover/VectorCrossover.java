package cilib.pso.selfadaptive.crossover;

import java.util.List;
import cilib.type.types.container.Vector;

public interface VectorCrossover {

    public List<Vector> crossover(List<Vector> parents);

    public int getNumberOfParents();
}
