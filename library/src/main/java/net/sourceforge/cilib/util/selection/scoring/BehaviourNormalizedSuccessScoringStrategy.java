/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.scoring;

import net.sourceforge.cilib.entity.behaviour.Behaviour;

public class BehaviourNormalizedSuccessScoringStrategy<E extends Behaviour> implements ScoringStrategy<E> {

    @Override
    public int getScore(E current, Iterable<E> opponents) {
        int score = 0;
        //if it hasn't been used, or there are no successes, it cannot score better than any other behaviour
        if(current.getIterationCounter() == 0 || current.getSuccessCounter() == 0) return 0;
        double curr = current.getSuccessCounter() / (double)current.getIterationCounter();

        for (E i : opponents) {
        	//if the competitor hasn't been used, I must be better
        	if(i.getIterationCounter() == 0) score++; 
        	else if (curr > (i.getSuccessCounter() / (double)i.getIterationCounter())) score++; 
            
        }
        return score;
    }

}
