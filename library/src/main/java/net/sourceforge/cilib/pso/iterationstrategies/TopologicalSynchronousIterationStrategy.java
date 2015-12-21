/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.iterationstrategies;

import fj.F;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.behaviour.TopologicalStandardParticleBehaviour;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * Implementation of the synchronous iteration strategy for PSO.
 */
public class TopologicalSynchronousIterationStrategy extends AbstractIterationStrategy<PSO> {

    private static final long serialVersionUID = 6617737228912852220L;

    /**
     * {@inheritDoc}
     */
    @Override
        public TopologicalSynchronousIterationStrategy getClone() {
        return this;
    }

    /**
     * This is an Synchronous strategy:
     *
     * <ol>
     *   <li>For all particles:</li>
     *   <ol>
     *     <li>Update the particle velocity</li>
     *     <li>Update the particle position</li>
     *   </ol>
     *   <li>For all particles:</li>
     *   <ol>
     *     <li>Calculate the particle fitness</li>
     *     <li>For all particles in the current particle's neighbourhood:</li>
     *     <ol>
     *       <li>Update the neighbourhood best</li>
     *     </ol>
     *   </ol>
     * </ol>
     *
     * @param pso The {@link PSO} to have an iteration applied.
     */
    @Override
        public void performIteration(final PSO pso) {
        final fj.data.List<Particle> topology = pso.getTopology();

        final F<Particle, Particle> first = new F<Particle, Particle>() {
			@Override
			public Particle f(Particle current) {
				current.getBehaviour().performIteration(current);
	            return current;
			}
        };

        final F<Particle, Particle> second = new F<Particle, Particle>() {
        	public Particle f(Particle current) {
        		TopologicalStandardParticleBehaviour behaviour = (TopologicalStandardParticleBehaviour) current.getBehaviour();

                for (Particle other : behaviour.getNeighbourhood().f(topology, current)) {
        			if (current.getSocialFitness().compareTo(other.getNeighbourhoodBest().getSocialFitness()) > 0) {
        				other.setNeighbourhoodBest(current);
        			}
        		}

        		return current;
        	}
        };

        pso.setTopology(topology.map(first).map(second));
    }
}
