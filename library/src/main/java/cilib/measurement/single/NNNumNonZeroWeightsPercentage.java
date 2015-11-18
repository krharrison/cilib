/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.controlparameter.ControlParameter;
import cilib.controlparameter.ConstantControlParameter;
import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.nn.architecture.Layer;
import cilib.nn.components.Neuron;
import cilib.problem.nn.NNTrainingProblem;
import cilib.type.types.Type;
import cilib.type.types.Numeric;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;

/**
 * Counts the number of non-zero weights (weights close to zero determined
 * by the epsilon value), and measures as a percentage in terms of the 
 * total number of NN weights.
 */
public class NNNumNonZeroWeightsPercentage implements Measurement {

    private ControlParameter epsilon;

    public NNNumNonZeroWeightsPercentage() {
        epsilon = ConstantControlParameter.of(0.05);
    }

    public NNNumNonZeroWeightsPercentage(NNNumNonZeroWeightsPercentage rhs) {
        epsilon = rhs.epsilon.getClone();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public NNNumNonZeroWeightsPercentage getClone() {
        return new NNNumNonZeroWeightsPercentage(this);
    }

    /**
     * Returns the percentage of non-zero weights to the total number of weights
     * as a number in R(0:1)
     */
    @Override
    public Type getValue(Algorithm algorithm) {
        Vector solution = (Vector) algorithm.getBestSolution().getPosition();
        int count = 0;

        for (Numeric n : solution){
            if (Math.abs(n.doubleValue()) > epsilon.getParameter()){
                ++count;
            }
        }

        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();

        int total = 0;
        for (Layer curLayer : problem.getNeuralNetwork().getArchitecture().getLayers()) {
            for (Neuron curNeuron : curLayer) {
                total += curNeuron.getNumWeights();
            }
        }

        return Real.valueOf((double) count / (double) total);
    }

    public void setEpsilon(Double epsilon){
        ((ConstantControlParameter) this.epsilon).setParameter(epsilon);
    }

    public ControlParameter getEpsilon(){
        return this.epsilon;
    }
}
