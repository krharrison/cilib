/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.algorithm.population;

import cilib.algorithm.Algorithm;
import cilib.clustering.DataClusteringPSO;
import cilib.clustering.entity.ClusterParticle;
import cilib.entity.Property;
import cilib.problem.solution.InferiorFitness;
import cilib.type.types.container.CentroidHolder;

/**
 * This class holds the functionality that is common to a number of cooperative iteration strategies
 */
public abstract class AbstractCooperativeIterationStrategy<E extends Algorithm> extends AbstractIterationStrategy<E> {
    protected ClusterParticle contextParticle;
    protected boolean contextinitialised;
    protected boolean elitist;
    /*
     * Default constructor for AbstractCooperativeIterationStrategy
     */
    public AbstractCooperativeIterationStrategy() {
        contextParticle = new ClusterParticle();
        contextinitialised = false;
        elitist = false;
    }

    /*
     * Copy constructor for AbstractCooperativeIterationStrategy
     * @param copy The AbstractCooperativeIterationStrategy to be copied
     */
    public AbstractCooperativeIterationStrategy(AbstractCooperativeIterationStrategy copy) {
        contextParticle = copy.contextParticle;
        contextinitialised = copy.contextinitialised;
        elitist = copy.elitist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract AbstractIterationStrategy<E> getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void performIteration(E algorithm);

    /*
     * Returns the context particle
     * @return contextParticle The context particle
     */
    public ClusterParticle getContextParticle() {
        ((CentroidHolder) contextParticle.getPosition()).clearAllCentroidDataItems();
        contextParticle.updateFitness(contextParticle.getBehaviour().getFitnessCalculator().getFitness(contextParticle));
        return contextParticle;
    }

    /*
     * Initialises the context particle for the first time
     * @param algorithm The algorithm whose context particle needs to be initialised
     */
    public void initialiseContextParticle(MultiPopulationBasedAlgorithm algorithm) {
        int populationIndex = 0;
        CentroidHolder solution = new CentroidHolder();
        CentroidHolder velocity = new CentroidHolder();
        CentroidHolder bestPosition = new CentroidHolder();

        for(SinglePopulationBasedAlgorithm alg : algorithm.getPopulations()) {
            if(!((DataClusteringPSO) alg).isExplorer()) {
                solution.add(((CentroidHolder) alg.getBestSolution().getPosition()).get(populationIndex).getClone());
                velocity.add(((CentroidHolder) alg.getBestSolution().getPosition()).get(populationIndex).getClone());
                bestPosition.add(((CentroidHolder) alg.getBestSolution().getPosition()).get(populationIndex).getClone());

                populationIndex++;
            }
        }

        contextParticle.setPosition(solution);
        contextParticle.put(Property.VELOCITY, velocity);
        contextParticle.put(Property.BEST_POSITION, bestPosition);
        contextParticle.put(Property.FITNESS, InferiorFitness.instance());
        contextParticle.put(Property.BEST_FITNESS, InferiorFitness.instance());
        contextinitialised = true;
    }

    public boolean getIsElitist() {
        return elitist;
    }

    public void setIsElitist(Boolean elitist) {
        this.elitist = elitist;
    }

}
