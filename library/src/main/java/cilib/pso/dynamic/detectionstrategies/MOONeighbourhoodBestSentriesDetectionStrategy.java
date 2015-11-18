/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.dynamic.detectionstrategies;


import java.util.Set;
import cilib.algorithm.Algorithm;
import cilib.algorithm.population.HasNeighbourhood;
import cilib.algorithm.population.HasTopology;
import cilib.entity.Entity;
import cilib.entity.Topologies;
import cilib.problem.solution.Fitness;
import cilib.problem.solution.MOFitness;

/*
 * This class uses the neighbourhood best as sentry particle to determine 
 * whether a change has occurred in the environment.
 */
public class MOONeighbourhoodBestSentriesDetectionStrategy extends EnvironmentChangeDetectionStrategy {
    private static final long serialVersionUID = 3598067152913033487L;

    public MOONeighbourhoodBestSentriesDetectionStrategy() {
        // super() is automatically called
    }

    public MOONeighbourhoodBestSentriesDetectionStrategy(MOONeighbourhoodBestSentriesDetectionStrategy rhs) {
        super(rhs);
    }

    @Override
    public MOONeighbourhoodBestSentriesDetectionStrategy getClone() {
        return new MOONeighbourhoodBestSentriesDetectionStrategy(this);
    }

    @Override
    public <A extends HasTopology & Algorithm & HasNeighbourhood> boolean detect(A algorithm) {
        if (algorithm.getIterations() % interval == 0) {
            Set<? extends Entity> sentries = Topologies.getNeighbourhoodBestEntities(algorithm.getTopology(), algorithm.getNeighbourhood());

            boolean detectedChange = false;
            for (Entity sentry : sentries) {
                if (sentry.getFitness().getClass().getName().matches("MinimisationFitness")) {
                    Fitness previousFitness = sentry.getFitness();
                    sentry.updateFitness(sentry.getBehaviour().getFitnessCalculator().getFitness(sentry));
                    Fitness currentFitness = sentry.getFitness();

                    if (Math.abs(previousFitness.getValue() - currentFitness.getValue()) >= epsilon) {
                        detectedChange = true;

                    }
                }
                else if (sentry.getFitness().getClass().getName().matches("StandardMOFitness")) {
                        MOFitness previousFitness = (MOFitness)sentry.getFitness();
                        sentry.updateFitness(sentry.getBehaviour().getFitnessCalculator().getFitness(sentry));
                        MOFitness currentFitness = (MOFitness)sentry.getFitness();

                        for (int k=0; k < previousFitness.getDimension(); k++)
                            if (Math.abs(previousFitness.getFitness(k).getValue() -
                		currentFitness.getFitness(k).getValue()) >= epsilon) {
                		detectedChange = true;
                                break;
                            }
                }
                if (detectedChange) {
                    System.out.println("Detected a change");
                    return true;
                }
        }
            }

        return false;
    }
}
