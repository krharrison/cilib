/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.entity.operators.crossover.real;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.List;
import cilib.controlparameter.ControlParameter;
import cilib.controlparameter.RandomControlParameter;
import cilib.entity.Entity;
import cilib.entity.operators.crossover.CrossoverStrategy;
import cilib.math.random.UniformDistribution;
import cilib.type.types.container.Vector;

public class RootProbabilityCrossoverStrategy implements CrossoverStrategy {
    
    private ControlParameter lambda;
    
    public RootProbabilityCrossoverStrategy() {
        this.lambda = new RandomControlParameter(new UniformDistribution());
    }
    
    public RootProbabilityCrossoverStrategy(RootProbabilityCrossoverStrategy copy) {
        this.lambda = copy.lambda.getClone();
    }

    @Override
    public RootProbabilityCrossoverStrategy getClone() {
        return new RootProbabilityCrossoverStrategy(this);
    }

    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() == 2, "RootProbabilityCrossoverStrategy requires 2 parents.");

        E o1 = (E) parentCollection.get(0).getClone();
        E o2 = (E) parentCollection.get(1).getClone();
        
        Vector o1Vec = (Vector) o1.getPosition();
        Vector o2Vec = (Vector) o2.getPosition();
        
        double value = Math.sqrt(lambda.getParameter());

        o1.setPosition(o1Vec.multiply(value).plus(o2Vec.multiply(1.0 - value)));
        o2.setPosition(o2Vec.multiply(value).plus(o1Vec.multiply(1.0 - value)));

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
