/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import cilib.algorithm.Algorithm;
import cilib.measurement.Measurement;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.type.types.StringType;
import cilib.type.types.container.Vector;

/**
 */
public class ParticlePositions implements Measurement<StringType> {
    private static final long serialVersionUID = -7891715753767819344L;

    /**
     * {@inheritDoc}
     */
    @Override
    public ParticlePositions getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringType getValue(Algorithm algorithm) {
        final StringBuilder tmp = new StringBuilder();
        final PSO pso = (PSO) algorithm;
        for (Particle particle : pso.getTopology()) {
            tmp.append("\nParticle: ");
            tmp.append(" Current Fitness: ");
            tmp.append(particle.getFitness().getValue());
            tmp.append(" Best Fitness: ");
            tmp.append(particle.getBestFitness().getValue());
            tmp.append(" Position: ");

            Vector v = (Vector) particle.getPosition();
            for (int j = 0; j < particle.getDimension(); ++j) {
                tmp.append(v.doubleValueOf(j));
                tmp.append(" ");
            }
        }

        return new StringType(tmp.toString());
    }

}
