/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import fj.F;

/**
 * Implementation to create an irregular function.
 * <p>
 * Reference:
 * </p>
 * <p>
 * Liang, J. J., B. Y. Qu, and P. N. Suganthan.
 * "Problem Definitions and Evaluation Criteria for the CEC 2013 Special Session
 * on Real-Parameter Optimization." (2013).
 * </p>
 */
public class IrregularFunctionDecorator extends ContinuousFunction {

    private ContinuousFunction function;
    private F<Numeric, Numeric> mapping;


    @Override
    public Double f(Vector input) {
    	Vector.Builder builder = Vector.newBuilder();
    	for (int i = 0; i < input.size(); i++) {
            double x = input.doubleValueOf(i);
            if (i == 0 || i == input.size() - 1) {
            	double xHat = x == 0.0 ? 0.0 : Math.log(Math.abs(x));
                double c1 = x > 0 ? 10 : 5.5;
                double c2 = x > 0 ? 7.9 : 3.1;
                double result = Math.signum(x) * Math.exp(xHat + 0.049 * (Math.sin(xHat * c1) + Math.sin(xHat * c2)));
                builder.add(result);
            } else {
                builder.add(x);
            }
        }
    	
    	return function.f(builder.build());
    }

    public void setFunction(ContinuousFunction function) {
        this.function = function;
    }

    public ContinuousFunction getFunction() {
        return function;
    }

    public F<Numeric, Numeric> getMapping() {
        return mapping;
    }

}
