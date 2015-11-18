/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.ec.iterationstrategies;

import java.util.Arrays;
import java.util.List;

import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.ec.EC;
import cilib.ec.SaDEIndividual;
import cilib.entity.operators.creation.SaDECreationStrategy;
import cilib.type.types.container.Vector;
import cilib.util.selection.recipes.RandomSelector;
import cilib.util.selection.recipes.Selector;
import fj.F;

/**
 * This is the Self Adaptive Iteration Strategy described by A. K. Qin
 * and P. N. Suganthan in their 2005 IEEE paper "Self-adaptive Differential
 * Evolution Algorithm for Numerical Optimization".
 */
public class SaDEIterationStrategy extends AbstractIterationStrategy<EC> {

    private static final long serialVersionUID = 8019668923312811974L;

    private Selector<SaDEIndividual> targetVectorSelectionStrategy;
    private int frequencyOfChange;
    private int frequencyOfAdaptiveVarialeRecalculation;
    private int nextChange;
    private int nextAdaptiveVariableRecalculation;
    private double totalAcceptedWithStrategy1;
    private double totalAcceptedWithStrategy2;
    private double totalRejectedWithStrategy1;
    private double totalRejectedWithStrategy2;

    /**
     * Create an instance of the {@linkplain DifferentialEvolutionIterationStrategy}.
     */
    public SaDEIterationStrategy() {
        this.targetVectorSelectionStrategy = new RandomSelector();
        frequencyOfChange = 1;
        frequencyOfAdaptiveVarialeRecalculation = 1;
        nextChange = 1;
        nextAdaptiveVariableRecalculation = frequencyOfAdaptiveVarialeRecalculation;
        totalAcceptedWithStrategy1 = 0;
        totalAcceptedWithStrategy2 = 0;
        totalRejectedWithStrategy1 = 0;
        totalRejectedWithStrategy2 = 0;
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public SaDEIterationStrategy(SaDEIterationStrategy copy) {
        this.targetVectorSelectionStrategy = copy.targetVectorSelectionStrategy;
        frequencyOfChange = copy.frequencyOfChange;
        frequencyOfAdaptiveVarialeRecalculation = copy.frequencyOfAdaptiveVarialeRecalculation;
        nextChange = copy.nextChange;
        nextAdaptiveVariableRecalculation = copy.nextAdaptiveVariableRecalculation;
        totalAcceptedWithStrategy1 = copy.totalAcceptedWithStrategy1;
        totalAcceptedWithStrategy2 = copy.totalAcceptedWithStrategy2;
        totalRejectedWithStrategy1 = copy.totalRejectedWithStrategy1;
        totalRejectedWithStrategy2 = copy.totalRejectedWithStrategy2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SaDEIterationStrategy getClone() {
        return new SaDEIterationStrategy(this);
    }

    /**
     * Perform an iteration of the DE algorithm defined as the DE/x/y/z implementation.
     * @param ec The {@linkplain EC} on which to perform this iteration.
     */
    @Override
    public void performIteration(final EC ec) {
        final fj.data.List<SaDEIndividual> topology = ec.getTopology();

        if(((SaDECreationStrategy)topology.head().getTrialVectorCreationStrategy()).probabilitiesChanged()) {
            totalAcceptedWithStrategy1 = 0;
            totalAcceptedWithStrategy2 = 0;
            totalRejectedWithStrategy1 = 0;
            totalRejectedWithStrategy2 = 0;
        }

        ec.setTopology(topology.map(new F<SaDEIndividual, SaDEIndividual>() {
        	@Override
            public SaDEIndividual f(SaDEIndividual current) {
        		// Create the trial vector by applying mutation
                SaDEIndividual targetEntity = targetVectorSelectionStrategy.on(topology).exclude(current).select();

                // Create the trial vector / entity
                SaDEIndividual trialEntity = current.getTrialVectorCreationStrategy().create(targetEntity, current, topology);

                // Create the offspring by applying cross-over
                List<SaDEIndividual> offspring = current.getCrossoverStrategy().crossover(Arrays.asList(current, trialEntity)); // Order is VERY important here!!

                // Replace the parent (current) if the offspring is better
                SaDEIndividual offspringEntity = offspring.get(0);
                offspringEntity.setPreviousFitness(current.getFitness().getClone());
                boundaryConstraint.enforce(offspringEntity);
                offspringEntity.updateFitness(offspringEntity.getBehaviour().getFitnessCalculator().getFitness(offspringEntity));

                boolean acceptedOffspring = false;
                if (offspringEntity.getFitness().compareTo(current.getFitness()) > 0) { // the trial vector is better than the parent
                    acceptedOffspring = true;
                }

                String strategyResult = ((SaDECreationStrategy) current.getTrialVectorCreationStrategy()).accepted(acceptedOffspring);

                current.acceptParameters(acceptedOffspring, offspringEntity);

                if(strategyResult.equalsIgnoreCase("Strategy 1 Accepted")) {
                    totalAcceptedWithStrategy1++;
                    //these are updated because the individual currently holds the creationStrategy, so it can only keep track of how many times its
                    //creationStrategy has been accepted and not how many times the creationStrategy itself has been chosen
                    ((SaDECreationStrategy) current.getTrialVectorCreationStrategy()).setTotalAcceptedWithStrategy1(totalAcceptedWithStrategy1);
                } else if(strategyResult.equalsIgnoreCase("Strategy 2 Accepted")) {
                    totalAcceptedWithStrategy2++;
                    ((SaDECreationStrategy) current.getTrialVectorCreationStrategy()).setTotalAcceptedWithStrategy2(totalAcceptedWithStrategy2);
                } else if(strategyResult.equalsIgnoreCase("Strategy 1 Rejected")) {
                    totalRejectedWithStrategy1++;
                    ((SaDECreationStrategy) current.getTrialVectorCreationStrategy()).setTotalRejectedWithStrategy1(totalRejectedWithStrategy1);
                } else if(strategyResult.equalsIgnoreCase("Strategy 2 Rejected")) {
                    totalRejectedWithStrategy2++;
                    ((SaDECreationStrategy) current.getTrialVectorCreationStrategy()).setTotalRejectedWithStrategy2(totalRejectedWithStrategy2);
                }

                if (acceptedOffspring) { //Give the offspring Entity all properties of the parent
                    Vector offspringVector = (Vector) offspringEntity.getPosition();
                    offspringEntity = current.getClone();
                    offspringEntity.setPosition(offspringVector);
                    offspringEntity.updateFitness(offspringEntity.getBehaviour().getFitnessCalculator().getFitness(offspringEntity));
                    return offspringEntity;
                } else {
                	return current;
                }
        	}
        }));

        if (ec.getIterations() == nextAdaptiveVariableRecalculation) {
            for (SaDEIndividual current : topology) {
                current.getCrossoverProbabilityParameterAdaptationStrategy().recalculateAdaptiveVariables();
                current.getScalingFactorParameterAdaptationStrategy().recalculateAdaptiveVariables();
            }

           nextAdaptiveVariableRecalculation += frequencyOfAdaptiveVarialeRecalculation;
        }

        if(ec.getIterations() == nextChange) {
            for (SaDEIndividual current : topology) {
                current.updateParameters();
            }

            nextChange += frequencyOfChange;
        }
    }

    /**
     * Obtain the {@linkplain Selector} used to select the target vector.
     * @return The {@linkplain Selector} of the target vector.
     */
    public Selector getTargetVectorSelectionStrategy() {
        return targetVectorSelectionStrategy;
    }

    /**
     * Set the {@linkplain Selector} used to select the target vector within the DE.
     * @param targetVectorSelectionStrategy The {@linkplain Selector} to use for the
     *        selection of the target vector.
     */
    public void setTargetVectorSelectionStrategy(Selector targetVectorSelectionStrategy) {
        this.targetVectorSelectionStrategy = targetVectorSelectionStrategy;
    }

    public int getFrequencyOfChange() {
        return frequencyOfChange;
    }

    public void setFrequencyOfChange(int frequencyOfChange) {
        this.nextChange = frequencyOfChange;
        this.frequencyOfChange = frequencyOfChange;
    }

    public int getFrequencyOfMeanRecalculation() {
        return frequencyOfAdaptiveVarialeRecalculation;
    }

    public void setFrequencyOfMeanRecalculation(int frequencyOfMeanRecalculation) {
        this.nextAdaptiveVariableRecalculation = frequencyOfMeanRecalculation;
        this.frequencyOfAdaptiveVarialeRecalculation = frequencyOfMeanRecalculation;
    }

    public int getNextChange() {
        return nextChange;
    }

    public void setNextChange(int nextChange) {
        this.nextChange = nextChange;
    }

    public int getNextMeanRecalculation() {
        return nextAdaptiveVariableRecalculation;
    }

    public void setNextMeanRecalculation(int nextMeanRecalculation) {
        this.nextAdaptiveVariableRecalculation = nextMeanRecalculation;
    }

}
