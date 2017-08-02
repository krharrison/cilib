/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.detectionstrategies.particle;

import cilib.entity.Property;
import cilib.problem.solution.Fitness;
import cilib.pso.particle.Particle;

public class FitnessImprovementDetectionStrategy implements ParticleUpdateDetectionStrategy {

    public FitnessImprovementDetectionStrategy(){ }

    public FitnessImprovementDetectionStrategy(FitnessImprovementDetectionStrategy copy){

    }
    /**
     * {@inheritDoc}
     */
    @Override
    public FitnessImprovementDetectionStrategy getClone() {
        return new FitnessImprovementDetectionStrategy(this);
    }

    @Override
    public boolean detect(Particle entity) {

        Fitness previousFitness = entity.get(Property.PREVIOUS_FITNESS);

        //if current fitness is worse than the previous fitness, return true
        return previousFitness.compareTo(entity.getFitness()) < 0;
    }
}
