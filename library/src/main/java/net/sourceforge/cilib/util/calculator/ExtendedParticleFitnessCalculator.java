/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.calculator;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.particle.ExtendedParticle;

/**
 * A fitness calculator that is specialised to determine the fitness of
 * an Entity instance.
 */
public class ExtendedParticleFitnessCalculator implements FitnessCalculator<Entity> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedParticleFitnessCalculator getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fitness getFitness(Entity entity) {
    	ExtendedParticle extParticle = (ExtendedParticle) entity;
        Algorithm algorithm = AbstractAlgorithm.get();
        return algorithm.getOptimisationProblem().getFitness(extParticle.getSolution());
    }

}