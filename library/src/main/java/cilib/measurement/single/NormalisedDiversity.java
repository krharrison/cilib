/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.single;

import java.util.Iterator;
import cilib.algorithm.Algorithm;
import cilib.entity.visitor.DiameterVisitor;
import cilib.measurement.Measurement;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;

/**
 * <p>
 * Function to calculate the normalised diversity of a swarm according to the definition of Riget et al
 * </p><p>
 * References:
 * </p><p><ul><li>
 * J Riget, J.S. Vesterstrom, "A Diversity-Guided Particle Swarm Optimizer -- The ARPSO",
 * Technical Report, Department of Computer Science, University of Aarhus, 2002.
 * </li><li>
 * AP Engelbrecht, "Fundamentals of Computational Swarm Intelligence",
 * Wiley & Sons, pages 125, 2005.
 * </li></ul></p>
 */
/**
 * TODO: Rather let this extend Diversity, call super.getValue, but how
 * to type-cast this back to a double?
 */
public class NormalisedDiversity implements Measurement<Real> {

    private static final long serialVersionUID = 93751729329230145L;

    /**
     * {@inheritDoc}
     */
    @Override
    public NormalisedDiversity getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Real getValue(Algorithm algorithm) {
        PSO pso = (PSO) algorithm;

        int numberParticles = pso.getTopology().length();

        Iterator<Particle> k = pso.getTopology().iterator();
        Particle particle = k.next();
        Vector averageParticlePosition = (Vector) particle.getPosition().getClone();
        while (k.hasNext()) {
            particle = k.next();
            Vector v = (Vector) particle.getPosition();
            for (int j = 0; j < averageParticlePosition.size(); ++j) {
                averageParticlePosition.setReal(j, averageParticlePosition.doubleValueOf(j) + v.doubleValueOf(j));
            }
        }
        for (int j = 0; j < averageParticlePosition.size(); ++j) {
            averageParticlePosition.setReal(j, averageParticlePosition.doubleValueOf(j) / numberParticles);
        }

        Iterator<Particle> i = pso.getTopology().iterator();
        double particleSum = 0.0;
        while (i.hasNext()) {
            particle = i.next();

            double dimensionSum = 0.0;
            Vector v = (Vector) particle.getPosition();
            for (int j = 0; j < particle.getDimension(); ++j) {
                dimensionSum += (v.doubleValueOf(j) - averageParticlePosition.doubleValueOf(j)) * (v.doubleValueOf(j) - averageParticlePosition.doubleValueOf(j));
            }
            particleSum += Math.sqrt(dimensionSum);
        }

        double diversity = particleSum / numberParticles;

        DiameterVisitor diameterVisitor = new DiameterVisitor();
        double diameter = diameterVisitor.f(pso.getTopology());

        return Real.valueOf(diversity / diameter);
    }
}
