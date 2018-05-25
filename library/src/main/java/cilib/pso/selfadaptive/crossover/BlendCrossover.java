package cilib.pso.selfadaptive.crossover;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.UniformDistribution;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;
import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.List;

public class BlendCrossover implements VectorCrossover {

    private ControlParameter alpha;
    private ProbabilityDistributionFunction random;

    public BlendCrossover() {
        this.alpha = ConstantControlParameter.of(0.5);
        this.random = new UniformDistribution();
    }

    public BlendCrossover(BlendCrossover copy) {
        this.alpha = copy.alpha.getClone();
        this.random = new UniformDistribution();
    }

    public BlendCrossover getClone() {
        return new BlendCrossover(this);
    }

    @Override
    public List<Vector> crossover(List<Vector> parents) {
        Preconditions.checkArgument(parents.size() == 2, "BlendCrossoverStrategy requires 2 parents.");

        Vector p1 = parents.get(0);
        Vector p2 = parents.get(1);
        Vector.Builder o1Builder = Vector.newBuilder();
        Vector.Builder o2Builder = Vector.newBuilder();

        int sizeParent1 = p1.size();
        int sizeParent2 = p2.size();

        int minDimension = Math.min(sizeParent1, sizeParent2);

        for (int i = 0; i < minDimension; i++) {
            double gamma = (1 + 2 * alpha.getParameter()) * random.getRandomNumber() - alpha.getParameter();
            double value1 = (1 - gamma) * p1.doubleValueOf(i) + gamma * p2.doubleValueOf(i);
            double value2 = (1 - gamma) * p2.doubleValueOf(i) + gamma * p1.doubleValueOf(i);

            o1Builder.add(Real.valueOf(value1));
            o2Builder.add(Real.valueOf(value2));
        }


        return Arrays.asList(o1Builder.build(), o2Builder.build());
    }

    public ControlParameter getAlpha() {
        return alpha;
    }

    public void setAlpha(ControlParameter alpha) {
        this.alpha = alpha;
    }

    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }

    public ProbabilityDistributionFunction getRandom() {
        return random;
    }

    @Override
    public int getNumberOfParents() {
        return 2;
    }


}
