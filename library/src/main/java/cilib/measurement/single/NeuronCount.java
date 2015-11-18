/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.nn.architecture.Layer;
import cilib.problem.nn.NNTrainingProblem;
import cilib.type.types.Int;

/**
 * Counts the number of neurons in a neural network.
 */
public class NeuronCount implements Measurement {

    private boolean includeBias;

    public NeuronCount() {
        includeBias = true;
    }

    public NeuronCount(NeuronCount rhs) {
        includeBias = rhs.includeBias;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public NeuronCount getClone() {
        return new NeuronCount(this);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Int getValue(Algorithm algorithm) {
        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();

        int count = 0;
        for (Layer curLayer : problem.getNeuralNetwork().getArchitecture().getLayers()) {
            count += curLayer.size();
            if (!includeBias && curLayer.isBias()) {
                --count;
            }
        }

        return Int.valueOf(count);
    }

    /**
     * Sets whether bias units should be included in the count.
     * @param include True to include bias units.
     */
    public void setIncludeBias(boolean include) {
        includeBias = include;
    }
}
