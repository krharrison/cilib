/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.util.selection.scoring;


import cilib.entity.behaviour.Behaviour;

public class BehaviourSuccessCountScoringStrategy<E extends Behaviour> implements ScoringStrategy<E> {

    @Override
    public int getScore(E current, Iterable<E> opponents) {
        int score = 0;
        for (E i : opponents) {
            if (current.getSuccessCounter() > i.getSuccessCounter()) {
                score++;
            }
        }
        return score;
    }

}
