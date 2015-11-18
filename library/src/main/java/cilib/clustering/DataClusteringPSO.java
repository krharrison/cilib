/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.clustering;

import java.util.List;

import cilib.algorithm.initialisation.DataDependantPopulationInitialisationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.clustering.entity.ClusterParticle;
import cilib.clustering.iterationstrategies.StandardDataClusteringIterationStrategy;
import cilib.entity.Topologies;
import cilib.entity.comparator.SocialBestFitnessComparator;
import cilib.problem.ClusteringProblem;
import cilib.problem.solution.OptimisationSolution;

import com.google.common.collect.Lists;

/**
 * This class holds the functionality of the Standard Data Clustering PSO described in:
 * <pre>
 * {@literal @}article{vanDerMerwe03,
 *  title={{Data Clustering using Particle Swarm Optimization }},
 *  author={van der Merwe, D.W.; Engelhrecht, A.P.},
 *  year={2003},
 *  journal={Congress on Evolutionary Computation},
 *  volume={1},
 *  pages={215-220}
 * }
 * </pre>
 *
 * This is so if the StandardDataClusteringIterationStrategy is used. Variations of the algorithm
 * in the article above can then be tested by using other iteration strategies, such as the
 * ReinitialisingDataClusteringIterationStrategy
 */
public class DataClusteringPSO extends SinglePopulationBasedAlgorithm<ClusterParticle> {

    private IterationStrategy<DataClusteringPSO> iterationStrategy;
    private boolean isExplorer;
    private final int numberOfCentroids;

    /*
     * Default Constructor for DataClusteringPSO
     */
    public DataClusteringPSO() {
        initialisationStrategy = new DataDependantPopulationInitialisationStrategy();
        iterationStrategy = new StandardDataClusteringIterationStrategy();
        isExplorer = false;
        numberOfCentroids = 1;
    }

    /*
     * copy constructor for DataClusteringPSO
     * @param copy Th DataClusteringPSO to be copied
     */
    public DataClusteringPSO(DataClusteringPSO copy) {
        super(copy);
        iterationStrategy = copy.iterationStrategy.getClone();
        isExplorer = copy.isExplorer;
        numberOfCentroids = copy.numberOfCentroids;
    }

    /*
     * Clone method for the DataClusteringPSO
     * @return new instance of the DataClusteringPSO
     */
    @Override
    public DataClusteringPSO getClone() {
        return new DataClusteringPSO(this);
    }

    /*
     * Calls the IterationStrategy's performIteration method
     */
    @Override
    protected void algorithmIteration() {
        iterationStrategy.performIteration(this);
    }

    /*
     * Initialises the algorithm. This includes the SlidingWindow and topology
     */
    @Override
    public void algorithmInitialisation() {
        ClusteringProblem problem = (ClusteringProblem) this.optimisationProblem;
        
        ((DataDependantPopulationInitialisationStrategy) initialisationStrategy).setDataset(problem.getWindow().getCompleteDataset());
        Iterable<ClusterParticle> particles = this.initialisationStrategy.initialise(this.getOptimisationProblem());

        topology = fj.data.List.iterableList(particles);

        for(ClusterParticle particle : topology) {
            particle.updateFitness(particle.getBehaviour().getFitnessCalculator().getFitness(particle));
        }
    }

    /*
     * Returns the global best solution found by the algorithm so far
     * @return solution The global best solution
     */
    @Override
    public OptimisationSolution getBestSolution() {
        ClusterParticle bestEntity = Topologies.getBestEntity(topology, new SocialBestFitnessComparator<ClusterParticle>());
        return new OptimisationSolution(bestEntity.getBestPosition(), bestEntity.getBestFitness());
    }

    /*
     * Returns a list with each particle's best solution
     * @return solutions The list of solutions held by the Topology
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> solutions = Lists.newLinkedList();
        for (ClusterParticle e : Topologies.getNeighbourhoodBestEntities(topology, getNeighbourhood(), new SocialBestFitnessComparator<ClusterParticle>())) {
            solutions.add(new OptimisationSolution(e.getBestPosition(), e.getBestFitness()));
        }
        return solutions;

    }

    /*
     * Sets the iteration strategy to the one provided as a parameter
     * @param strategy The new iterations strategy
     */
    public void setIterationStrategy(IterationStrategy strategy) {
        iterationStrategy = strategy;
    }

    /*
     * Returns the current iteration strategy
     * @return iterationStrategy The current iteration strategy
     */
    public IterationStrategy getIterationStrategy() {
        return iterationStrategy;
    }

    /*
     * Sets the boolean value of isExplorer to the one provided as a parameter.
     * This is used in the co-operative multi-swarm as one of the swarms must be
     * an explorer.
     * @param value The new boolean value of isExplorer
     */
    public void setIsExplorer(boolean value) {
        isExplorer = value;
    }

    /*
     * Returns the value of isExplorer, i.e. it checks if the algorithm is currently an explorer
     * @return isExplorer The value of isExplorer
     */
    public boolean isExplorer() {
        return isExplorer;
    }

}
