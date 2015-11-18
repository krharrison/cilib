/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.discontinuous;

import cilib.controlparameter.ConstantControlParameter;
import cilib.functions.ContinuousFunction;
import cilib.math.random.UniformDistribution;
import cilib.type.types.container.Vector;

import java.util.HashMap;

public class ConvergenceAnalysis extends ContinuousFunction {

    HashMap<Integer, Double> values;
    UniformDistribution uniformDistribution;

    public ConvergenceAnalysis(){
        values = new HashMap<Integer, Double>();
        uniformDistribution = new UniformDistribution(ConstantControlParameter.of(-1000), ConstantControlParameter.of(1000));
    }

    @Override
    public Double f(Vector input) {
        int hash = input.hashCode();
        if(values.containsKey(hash)){
            return values.get(hash);
        }
        else {
            double value = uniformDistribution.getRandomNumber();
            values.put(hash, value);
            return value;
        }
    }
}
