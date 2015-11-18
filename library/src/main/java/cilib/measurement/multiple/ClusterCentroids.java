/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.multiple;

import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.type.types.container.CentroidHolder;
import cilib.type.types.container.Vector;

/**
 * A measurement that returns the positions of the centroids held by
 * the best ClusterParticle
 */
public class ClusterCentroids implements Measurement<Vector>{
    int dimension;

    public ClusterCentroids() {
        dimension = 0;
    }

    public ClusterCentroids(ClusterCentroids copy) {
        dimension = copy.dimension;
    }

    @Override
    public ClusterCentroids getClone() {
        return new ClusterCentroids(this);
    }

    @Override
    public Vector getValue(Algorithm algorithm) {
        CentroidHolder holder = (CentroidHolder) algorithm.getBestSolution().getPosition();
        return holder.get(dimension).toVector();
    }

    public void setDimension(int dim) {
        dimension = dim;
    }

}
