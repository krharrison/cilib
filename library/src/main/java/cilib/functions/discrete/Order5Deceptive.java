/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.discrete;

import cilib.functions.ContinuousFunction;
import cilib.type.types.container.Vector;


/**
 * Implementation of Mulhenbein's order-5 problem.
 * Intended for bit strings that are multiples of length 5.
 * <p>
 * References:
 * </p>
 * <ul><li>
 * Al-kazemi, Buthainah Sabeeh No'man and Mohan, Chilukuri K., "Multi-phase
 * discrete particle swarm optimization" (2000). Electrical Engineering and
 * Computer Science. Paper 54.
 * </li></ul>
 */
public class Order5Deceptive extends ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double result = 0.0;

        for (int i = 0; i < input.size()-4; i+=5) {
            result += getValue(input.copyOfRange(i, i+5));
        }

        return result;
    }

    /**
     * Maps strings in the following manner.
     * 00000 = 4.0
     * 00001 = 3.0
     * 00011 = 2.0
     * 00111 = 1.0
     * 11111 = 3.5
     * other = 0.0
     */
    private Double getValue(Vector input) {

        int decimalValue = 0;
        for(int i = 0; i < input.size(); i++) {
            if (input.booleanValueOf(i)) {
                decimalValue += Math.pow(2, input.size() - i - 1);
            }
        }

        switch (decimalValue) {
            case 0: return 4.0;
            case 1: return 3.0;
            case 3: return 2.0;
            case 7: return 1.0;
            case 31: return 3.5;
            default: return 0.0;

        }
    }
}
