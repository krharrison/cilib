/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.multiswarm;

import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.clustering.DataClusteringPSO;
import cilib.clustering.entity.ClusterParticle;
import cilib.type.types.container.CentroidHolder;
import cilib.type.types.container.ClusterCentroid;
import cilib.type.types.container.Vector;
import cilib.util.distancemeasure.DistanceMeasure;
import cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * This class performs an iteration of a clustering multi-swarm iteration strategy where ClusterParticles are used.
 * If all swarms have converged, the weakest is re-initialised.
 * If two swarms are too lose together, one of them is re-initialised.
 * It is based on the algorithm described in Blackwell, but some calculations have been adapted to handle ClusterParticles.
 *
 * {@literal @}article{blackwell51pso,
 *  title={{Particle swarm optimization in dynamic environments}},
 *  author={Blackwell, T.},
 *  journal={Evolutionary Computation in Dynamic and Uncertain Environments},
 *  volume={51},
 *  pages={29--49}
 * }
 */
public class StandardClusteringMultiSwarmIterationStrategy extends AbstractIterationStrategy<MultiPopulationBasedAlgorithm> {

    private double exclusionRadius = 1.0;

    /*
     * Default constructor for StandardClusteringMultiSwarmIterationStrategy
     */
    public StandardClusteringMultiSwarmIterationStrategy() {
        super();
    }

    /*
     * Copy constructor for StandardClusteringMultiSwarmIterationStrategy
     * @param copy The StandardClusteringMultiSwarmIterationStrategy to be copied
     */
    public StandardClusteringMultiSwarmIterationStrategy(StandardClusteringMultiSwarmIterationStrategy copy) {
        super();
        this.exclusionRadius = copy.exclusionRadius;
    }

    /*
     * Clone method for StandardClusteringMultiSwarmIterationStrategy
     */
    @Override
    public StandardClusteringMultiSwarmIterationStrategy getClone() {
        return new StandardClusteringMultiSwarmIterationStrategy(this);
    }

    /*
     * Returns the value of the exclusion radius
     * @return exclusionRadius The exclusion radius
     */
    public double getExclusionRadius() {
        return exclusionRadius;
    }

    /*
     * Sets the value of the exclusion radius
     * @param newRadius The new exclusion radius
     */
    public void setExclusionRadius(double exlusionRadius) {
        this.exclusionRadius = exlusionRadius;
    }

    /*
     * Calculates the radius that is used to determine whether a swarm must be repelled from another swarm
     * @param algorithm The current algorithm
     * @return radius The radius
     */
    double calculateRadius(MultiPopulationBasedAlgorithm algorithm) {
        double dimensions = algorithm.getOptimisationProblem().getDomain().getDimension();
        double X = ((Vector) algorithm.getOptimisationProblem().getDomain().getBuiltRepresentation()).get(0).getBounds().getUpperBound()
                - ((Vector) algorithm.getOptimisationProblem().getDomain().getBuiltRepresentation()).get(0).getBounds().getLowerBound();
        double populationSize = algorithm.getPopulations().size();
        return X / (2 * Math.pow(populationSize, 1 / dimensions));
    }

    /*
     * Checks if a swarm has converged
     * @param algorithm The algorithm holding the swarm being checked
     * @param ca The current multi-population algorithm
     * @return true if the swarm has converged, false otherwise
     */
    boolean isConverged(SinglePopulationBasedAlgorithm algorithm, MultiPopulationBasedAlgorithm ca) {
        double r = calculateRadius(ca);
        DistanceMeasure dm = new EuclideanDistanceMeasure();

        for(ClusterParticle particle : ((DataClusteringPSO) algorithm).getTopology()) {
            for(ClusterParticle particle2 : ((DataClusteringPSO) algorithm).getTopology()) {
                ClusterParticle particleCopy = particle2.getClone();
                if(!particle.getPosition().containsAll(particleCopy.getPosition())) {
                    for(int i = 0; i < particle.getPosition().size(); i++) {
                        ClusterCentroid closestCentroid = getClosestCentroid((((CentroidHolder) particle.getPosition()).get(i)),
                                ((CentroidHolder) particleCopy.getPosition()));
                        particleCopy.getPosition().remove(closestCentroid);
                        if(dm.distance(((CentroidHolder) particle.getPosition()).get(i), closestCentroid) > r) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /*
     * Gets the closest centroid to the one received
     * @param centroid The centroid received
     * @param holder The centroid holder with all the other centroids
     * @return closestCentroid The closest centroid to centroid
     */
    private ClusterCentroid getClosestCentroid(ClusterCentroid centroid, CentroidHolder holder) {
        DistanceMeasure dm = new EuclideanDistanceMeasure();
        ClusterCentroid closestCentroid = holder.get(0);
        double distance = Double.POSITIVE_INFINITY;

        for(int i = 0; i < holder.size(); i++) {
            if(distance > dm.distance(centroid.toVector(), holder.get(i).toVector())) {
                distance = dm.distance(centroid.toVector(), holder.get(i).toVector());
                closestCentroid = holder.get(i);
            }
        }

        return closestCentroid;
    }

    /*
     * Performs an iteration of the Clustering Multi-swarm Iteration Strategy.
     * If all swarms have converged, it re-initialises the weakest one.
     * It performs an iteration of the algorithm holding each swarm.
     * It re-initialises a swarm if two swarms get too close to each other.
     * @param ca The multi-swarm algorithm
     */
    @Override
    public void performIteration(MultiPopulationBasedAlgorithm ca) {
        int converged = 0;
        for (SinglePopulationBasedAlgorithm current : ca.getPopulations()) {
            if (isConverged(current, ca)) {
                converged++;
            }
        }

        if (converged == ca.getPopulations().size()) {
            SinglePopulationBasedAlgorithm weakest = null;
            for (SinglePopulationBasedAlgorithm current : ca.getPopulations()) {
                ((DataClusteringPSO) current).setIsExplorer(false);
                if (weakest == null || weakest.getBestSolution().compareTo(current.getBestSolution()) > 0) {
                    weakest = current;
                    ((DataClusteringPSO) weakest).setIsExplorer(true);
                }
            }
            reInitialise((DataClusteringPSO) weakest);
        }

        for (SinglePopulationBasedAlgorithm current : ca.getPopulations()) {
            current.performIteration();
        }

        for (SinglePopulationBasedAlgorithm current : ca.getPopulations()) {
            for (SinglePopulationBasedAlgorithm other : ca.getPopulations()) {
                CentroidHolder currentPosition, otherPosition;
                if (!current.equals(other)) {
                    currentPosition = (CentroidHolder) current.getBestSolution().getPosition(); //getBestParticle().getPosition();
                    otherPosition = (CentroidHolder) other.getBestSolution().getPosition();
                    boolean aDistanceIsSmallerThanRadius = aDistanceIsSmallerThanRadius(currentPosition, otherPosition);

                    if (aDistanceIsSmallerThanRadius) {
                        if (current.getBestSolution().getFitness().compareTo(other.getBestSolution().getFitness()) > 0) {
                            reInitialise((DataClusteringPSO) current);
                        } else {
                            reInitialise((DataClusteringPSO) other);
                        }
                    }
                }
            }
        }

    }

    /*
     * Checks if the distance between two centroid holders is smaller than the radius.
     * It does it by comparing each centroid of the current position to that of the other position
     * @param currentPosition The current position being checked
     * @param otherPosition The position that currentPosition needs to be checked against
     * @return true if the swarms are too close, false otherwise
     */
    private boolean aDistanceIsSmallerThanRadius(CentroidHolder currentPosition, CentroidHolder otherPosition) {
        DistanceMeasure dm = new EuclideanDistanceMeasure();

        for(int i = 0; i < currentPosition.size(); i++) {
            if(dm.distance(currentPosition.get(i).toVector(), otherPosition.get(i).toVector()) < exclusionRadius)
                return true;
        }
        return false;
    }

    /*
     * Reinitialises a swarm
     * @param algorithm The algorithm holding the swarm
     */
    public void reInitialise(DataClusteringPSO algorithm) {
        for(ClusterParticle particle : algorithm.getTopology()) {
            particle.reinitialise();
            particle.updateFitness(particle.getBehaviour().getFitnessCalculator().getFitness(particle));
        }
    }
}
