/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.problem.nn.NNTrainingProblem;
import cilib.type.types.Int;

/**
 * Counts the number of layers in a neural network.
 */
public class NNLayerCount implements Measurement {

    /**
     * {@inheritDoc }
     */
    @Override
    public NNLayerCount getClone() {
        return new NNLayerCount();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Int getValue(Algorithm algorithm) {
        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();
        return Int.valueOf(problem.getNeuralNetwork().getArchitecture().getLayers().size());
    }
}
