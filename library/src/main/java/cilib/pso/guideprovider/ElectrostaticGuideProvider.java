/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.guideprovider;

import fj.F2;
import fj.Ord;
import fj.Ordering;
import cilib.algorithm.AbstractAlgorithm;
import cilib.problem.solution.Fitness;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.type.types.container.Vector;
import cilib.type.types.container.StructuredType;
import cilib.util.Vectors;

public class ElectrostaticGuideProvider implements GuideProvider {

    @Override
    public ElectrostaticGuideProvider getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StructuredType get(Particle particle) {
        Vector position = (Vector) particle.getBestPosition();
        double ssLength = Vectors.upperBoundVector(position).subtract(Vectors.lowerBoundVector(position)).length();

        fj.data.List<Particle> topology = ((PSO) AbstractAlgorithm.get()).getTopology()
            .sort(Ord.<Particle>ord(new F2<Particle, Particle, Ordering>() {
                @Override
                public Ordering f(Particle p1, Particle p2) {
                    return Ordering.values()[-p1.getBestFitness().compareTo(p2.getBestFitness()) + 1];
                }
            }.curry()));

        Fitness bestFitness = topology.head().getBestFitness();
        Fitness worstFitness = topology.last().getBestFitness();
        double alpha = ssLength / Math.abs(bestFitness.getValue() - worstFitness.getValue()) * 
                       bestFitness.compareTo(worstFitness);

        Particle ferMaximizer = topology.head();
        double maxFER = Double.NEGATIVE_INFINITY;

        for (Particle p : topology) {
            if (p != particle) {
                Fitness f = p.getBestFitness();
                Vector pos = (Vector) p.getBestPosition();

                double testFER = alpha * f.getValue() * particle.getBestFitness().getValue() / 
                                 Math.pow(pos.subtract(position).length(), 2);

                if (testFER > maxFER && pos.subtract(position).length() > 0) {
                    maxFER = testFER;
                    ferMaximizer = p;
                }
            }
        }

        return ferMaximizer.getBestPosition();
    }

}
