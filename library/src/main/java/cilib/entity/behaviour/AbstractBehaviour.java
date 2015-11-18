/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.entity.behaviour;

import cilib.entity.Entity;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.problem.boundaryconstraint.UnconstrainedBoundary;
import cilib.util.calculator.EntityBasedFitnessCalculator;
import cilib.util.calculator.FitnessCalculator;

/**
 * Implementation of the synchronous iteration strategy for PSO.
 */
public abstract class AbstractBehaviour implements Behaviour {
    protected BoundaryConstraint boundaryConstraint;
    protected FitnessCalculator<Entity> fitnessCalculator;

    private int successCounter;
    private int selectedCounter;
    private int iterationCounter;

    /**
     * Default constructor assigns an UnconstrainedBoundary, and sets the success
     * and selected counters to zero.
     */
    public AbstractBehaviour() {
        this.boundaryConstraint = new UnconstrainedBoundary();
        this.fitnessCalculator = new EntityBasedFitnessCalculator();
        
        this.successCounter = 0;
        this.selectedCounter = 0;
        this.iterationCounter = 0;
    }

    /**
     * Copy Constructor.
     *
     * @param copy The {@link AbstractBehaviour} object to copy.
     */
    public AbstractBehaviour(AbstractBehaviour copy) {
        this.boundaryConstraint = copy.boundaryConstraint.getClone();
        this.fitnessCalculator = copy.fitnessCalculator.getClone();
        
        this.selectedCounter = copy.selectedCounter;
        this.successCounter = copy.successCounter;
        this.iterationCounter = copy.iterationCounter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Entity performIteration(Entity current);

    /**
     * Get the currently associated {@linkplain BoundaryConstraint}.
     * @return The current {@linkplain BoundaryConstraint}.
     */
    public BoundaryConstraint getBoundaryConstraint() {
        return boundaryConstraint;
    }

    /**
     * Set the {@linkplain BoundaryConstraint} to maintain within this {@linkplain IterationStrategy}.
     * @param boundaryConstraint The {@linkplain BoundaryConstraint} to set.
     */
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        this.boundaryConstraint = boundaryConstraint;
    }

    /**
     * {@inheritDoc}
     */
    public FitnessCalculator getFitnessCalculator() {
        return fitnessCalculator;
    }

    /**
     * Sets the fitness calculator.
     * 
     * @param fitnessCalculator The fitness calculator to set.
     */
    public void setFitnessCalculator(FitnessCalculator fitnessCalculator) {
        this.fitnessCalculator = fitnessCalculator;
    }

    /**
     * {@inheritDoc}
     */
    public void incrementIterationCounter() {
        iterationCounter++;
    }
    
    /**
     * {@inheritDoc}
     */
    public void incrementSuccessCounter() {
        successCounter++;
    }

    /**
     * {@inheritDoc}
     */
    public void incrementSelectedCounter() {
        selectedCounter++;
    }

    /**
     * {@inheritDoc}
     */
    public int getIterationCounter() {
        return iterationCounter;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getSelectedCounter() {
        return selectedCounter;
    }

    /**
     * {@inheritDoc}
     */
    public int getSuccessCounter() {
        return successCounter;
    }

    /**
     * {@inheritDoc}
     */
    public void resetSelectedCounter() {
        selectedCounter = 0;
    }

    /**
     * {@inheritDoc}
     */
    public void resetSuccessCounter() {
        successCounter = 0;
    }
    
    /**
     * {@inheritDoc}
     */
    public void resetIterationCounter() {
        iterationCounter = 0;
    }

    /**
     * {@inheritDoc}
     */
    public void setSelectedCounter(int n) {
        selectedCounter = n;
    }

    /**
     * {@inheritDoc}
     */
    public void setSuccessCounter(int n) {
        successCounter = n;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setIterationCounter(int n) {
        iterationCounter = n;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Behaviour o) {
        int mySuccesses = this.successCounter;
        int otherSuccesses = o.getSuccessCounter();
        return(mySuccesses < otherSuccesses ? -1 : (mySuccesses == otherSuccesses ? 0 : 1));
    }
}
