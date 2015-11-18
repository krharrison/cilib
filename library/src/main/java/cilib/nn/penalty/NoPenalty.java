/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cilib.nn.penalty;

import cilib.type.types.Type;

/**
 *
 * @author anna
 */
public class NoPenalty extends NNPenalty {

    @Override
    public double calculatePenalty(Type solution) {
        return 0; // no penalty!
    }

    @Override
    public double calculatePenaltyDerivative(double weight) {
        return 0;
    }
    
}
