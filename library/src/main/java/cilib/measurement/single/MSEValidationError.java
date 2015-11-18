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
 * Calculates the MSE generalisation error of the best solution of an
 * {@link Algorithm} optimising a {@link NNTrainingProblem}.
 */
public class MSEValidationError implements Measurement {

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
        StandardPatternDataTable validationSet = problem.getValidationSet();
        NeuralNetwork neuralNetwork = problem.getNeuralNetwork();
        neuralNetwork.setWeights(solution);

        double errorValidation = 0.0;
        OutputErrorVisitor visitor = new OutputErrorVisitor();
        Vector error = null;
        for (StandardPattern pattern : validationSet) {
            neuralNetwork.evaluatePattern(pattern);
            visitor.setInput(pattern);
            neuralNetwork.getArchitecture().accept(visitor);
            error = visitor.getOutput();
            for (Numeric real : error) {
                errorValidation += real.doubleValue() * real.doubleValue();
            }
        }
        errorValidation /= validationSet.getNumRows() * error.size();
        return Real.valueOf(errorValidation);
    }
}
