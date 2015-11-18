/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.util.selection.scoring;

import cilib.entity.Entity;
import cilib.math.random.generator.Rand;
import cilib.problem.solution.MinimisationFitness;

public class FitnessBasedScoringStrategy<E extends Entity> implements ScoringStrategy<E> {

    @Override
    public int getScore(E current, Iterable<E> opponents) {
        int score = 0;
        for (E i : opponents) {
            double p;
            if (i.getFitness() instanceof MinimisationFitness) {
                p = i.getFitness().getValue() / (i.getFitness().getValue() + current.getFitness().getValue());
            } else {
                p = current.getFitness().getValue() / (i.getFitness().getValue() + current.getFitness().getValue());
            }
            
            if (Rand.nextDouble() < p) {
                score++;
            }
        }
        return score;
    }

}
