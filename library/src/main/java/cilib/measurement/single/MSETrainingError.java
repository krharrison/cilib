/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.io.StandardPatternDataTable;
import cilib.io.pattern.StandardPattern;
import cilib.measurement.Measurement;
import cilib.nn.NeuralNetwork;
import cilib.nn.architecture.visitors.OutputErrorVisitor;
import cilib.problem.nn.NNTrainingProblem;
import cilib.type.types.Numeric;
import cilib.type.types.Real;
import cilib.type.types.Type;
import cilib.type.types.container.Vector;

/**
 * Calculates the MSE training error of the best solution of an {@link Algorithm}
 * optimising a {@link NNTrainingProblem}.
 */
public class MSETrainingError implements Measurement {

    private static final long serialVersionUID = 426053308416839866L;

    /**
     * {@inheritDoc }
     */
    @Override
    public Measurement getClone() {
        return this;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Type getValue(Algorithm algorithm) {
        Vector solution = (Vector) algorithm.getBestSolution().getPosition();
        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();
        StandardPatternDataTable trainingSet = problem.getTrainingSet();
        NeuralNetwork neuralNetwork = problem.getNeuralNetwork();
        neuralNetwork.setWeights(solution);

        double errorTraining = 0.0;
        OutputErrorVisitor visitor = new OutputErrorVisitor();
        Vector error = null;
        for (StandardPattern pattern : trainingSet) {
            neuralNetwork.evaluatePattern(pattern);
            visitor.setInput(pattern);
            neuralNetwork.getArchitecture().accept(visitor);
            error = visitor.getOutput();
            for (Numeric real : error) {
                errorTraining += real.doubleValue() * real.doubleValue();
            }
        }
        errorTraining /= trainingSet.getNumRows() * error.size();
        return Real.valueOf(errorTraining);
    }
}
