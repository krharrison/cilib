package cilib.pso.selfadaptive.crossover;

import java.util.Arrays;
import java.util.List;
import cilib.controlparameter.ControlParameter;
import cilib.controlparameter.RandomControlParameter;
import cilib.math.random.UniformDistribution;
import cilib.type.types.container.Vector;
import com.google.common.base.Preconditions;

public class ArithmeticCrossover implements VectorCrossover {
    private ControlParameter lambda;

    public ArithmeticCrossover() {
        this.lambda = new RandomControlParameter(new UniformDistribution());
    }

    public ArithmeticCrossover(ArithmeticCrossover copy) {
        this.lambda = copy.lambda.getClone();
    }

    public ArithmeticCrossover getClone() {
        return new ArithmeticCrossover(this);
    }

    @Override
    public List<Vector> crossover(List<Vector> parents) {
        Preconditions.checkArgument(parents.size() == 2, "ArithmeticCrossoverStrategy requires 2 parents.");

        Vector o1 = parents.get(0);
        Vector o2 = parents.get(1);

        double value = lambda.getParameter();

        o1 = o1.multiply(value).plus(o2.multiply(1.0 - value));
        o2 = o2.multiply(value).plus(o1.multiply(1.0 - value));

        //o1.setPosition(o1Vec.multiply(value).plus(o2Vec.multiply(1.0 - value)));
        //o2.setPosition(o2Vec.multiply(value).plus(o1Vec.multiply(1.0 - value)));

        return Arrays.asList(o1, o2);
    }

    public ControlParameter getLambda() {
        return lambda;
    }

    public void setLambda(ControlParameter lambda) {
        this.lambda = lambda;
    }

    @Override
    public int getNumberOfParents() {
        return 2;
    }
}
