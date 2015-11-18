/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.problem.nn;

import cilib.algorithm.AbstractAlgorithm;
import cilib.io.DataTable;
import cilib.io.DataTableBuilder;
import cilib.io.DelimitedTextFileReader;
import cilib.io.StandardPatternDataTable;
import cilib.io.exception.CIlibIOException;
import cilib.io.pattern.StandardPattern;
import cilib.io.transform.ShuffleOperator;
import cilib.io.transform.TypeConversionOperator;
import cilib.nn.architecture.visitors.OutputErrorVisitor;
import cilib.nn.domain.*;
import cilib.problem.AbstractProblem;
import cilib.problem.solution.Fitness;
import cilib.type.DomainRegistry;
import cilib.type.types.Numeric;
import cilib.type.types.Type;
import cilib.type.types.container.Vector;

/**
 * Class represents a {@link NNTrainingProblem} where the goal is to optimize
 * the set of weights of a neural network to best fit a given static dataset (either
 * regression, classification etc.).
 */
public class NNDataTrainingProblem extends NNTrainingProblem {
    private static final long serialVersionUID = -8765101028460476990L;

    private DataTableBuilder dataTableBuilder;
    private SolutionConversionStrategy solutionConversionStrategy;
    private int previousShuffleIteration;
    private boolean initialised;

    /**
     * Default constructor.
     */
    public NNDataTrainingProblem() {
        super();
        dataTableBuilder = new DataTableBuilder(new DelimitedTextFileReader());
        solutionConversionStrategy = new WeightSolutionConversionStrategy();
        previousShuffleIteration = -1;
        initialised = false;
    }

    /**
     * Initialises the problem by reading in the data and constructing the training
     * and generalisation sets. Also initialises (constructs) the neural network.
     */
    @Override
    public void initialise() {
        if (initialised) {
            return;
        }
        try {
            dataTableBuilder.addDataOperator(new TypeConversionOperator());
            dataTableBuilder.addDataOperator(patternConversionOperator);
            dataTableBuilder.buildDataTable();
            DataTable dataTable = dataTableBuilder.getDataTable();

            ShuffleOperator initialShuffler = new ShuffleOperator();
            initialShuffler.operate(dataTable);

            int trainingSize = (int) (dataTable.size() * trainingSetPercentage);
            int validationSize = (int) (dataTable.size() * validationSetPercentage);
            int generalisationSize = dataTable.size() - trainingSize - validationSize;

            trainingSet = new StandardPatternDataTable();
            validationSet = new StandardPatternDataTable();
            generalisationSet = new StandardPatternDataTable();

            for (int i = 0; i < trainingSize; i++) {
                trainingSet.addRow((StandardPattern) dataTable.getRow(i));
            }

            for (int i = trainingSize; i < validationSize + trainingSize; i++) {
                validationSet.addRow((StandardPattern) dataTable.getRow(i));
            }

            for (int i = validationSize + trainingSize; i < generalisationSize + validationSize + trainingSize; i++) {
                generalisationSet.addRow((StandardPattern) dataTable.getRow(i));
            }

            neuralNetwork.initialise();
            
        } catch (CIlibIOException exception) {
            exception.printStackTrace();
        }
        initialised = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractProblem getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Calculates the fitness of the given solution by setting the neural network
     * weights to the solution and evaluating the training set in order to calculate
     * the MSE (which is minimized).
     *
     * @param solution the weights representing a solution.
     * @return a new MinimisationFitness wrapping the MSE training error.
     */
    @Override
    protected Fitness calculateFitness(Type solution) {
        if (trainingSet == null) {
            this.initialise();
        }

        int currentIteration = AbstractAlgorithm.get().getIterations();
        if (currentIteration != previousShuffleIteration) {
            try {
                shuffler.operate(trainingSet);
            } catch (CIlibIOException exception) {
                exception.printStackTrace();
            }
        }

        neuralNetwork.getArchitecture().accept(solutionConversionStrategy.interpretSolution(solution));

        double errorTraining = 0.0;
        OutputErrorVisitor visitor = new OutputErrorVisitor();
        Vector error = null;
        for (StandardPattern pattern : trainingSet) {
            Vector output = neuralNetwork.evaluatePattern(pattern);
            visitor.setInput(pattern);
            neuralNetwork.getArchitecture().accept(visitor);
            error = visitor.getOutput();
            for (Numeric real : error) {
                errorTraining += real.doubleValue() * real.doubleValue();
            }
        }
        errorTraining /= trainingSet.getNumRows() * error.size();

        return objective.evaluate(errorTraining);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainRegistry getDomain() {
        if (!initialised) {
            this.initialise();
        }
        return neuralNetwork.getArchitecture().getDomain();
    }

    /**
     * Gets the datatable builder.
     *
     * @return the datatable builder.
     */
    public DataTableBuilder getDataTableBuilder() {
        return dataTableBuilder;
    }

    /**
     * Sets the datatable builder.
     *
     * @param dataTableBuilder the new datatable builder.
     */
    public void setDataTableBuilder(DataTableBuilder dataTableBuilder) {
        this.dataTableBuilder = dataTableBuilder;
    }

    /**
     * Gets the source URL of the the datatable builder.
     *
     * @return the source URL of the the datatable builder.
     */
    public String getSourceURL() {
        return dataTableBuilder.getSourceURL();
    }

    /**
     * Sets the source URL of the the datatable builder.
     *
     * @param sourceURL the new source URL of the the datatable builder.
     */
    public void setSourceURL(String sourceURL) {
        dataTableBuilder.setSourceURL(sourceURL);
    }

    public SolutionConversionStrategy getSolutionConversionStrategy() {
        return solutionConversionStrategy;
    }

    public void setSolutionConversionStrategy(SolutionConversionStrategy solutionConversionStrategy) {
        this.solutionConversionStrategy = solutionConversionStrategy;
    }
}
