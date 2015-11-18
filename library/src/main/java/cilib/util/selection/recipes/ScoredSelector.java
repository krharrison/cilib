/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.util.selection.recipes;

import java.util.ArrayList;
import java.util.List;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.util.selection.*;
import cilib.util.selection.scoring.*;

public class ScoredSelector<E> implements Selector<E> {
    
    private Selector<Score<E>> selector;
    private Selector<E> opponentSelector;
    private ControlParameter opponentCount;
    private ScoringStrategy<E> scoringStrategy;
    
    public ScoredSelector() {
        selector = new ElitistSelector();
        opponentSelector = new RandomSelector();
        opponentCount = ConstantControlParameter.of(10.0);
        scoringStrategy = new SuccessCountScoringStrategy();
    }

    @Override
    public PartialSelection<E> on(Iterable<E> iterable) {
        List<E> newPopulation = new ArrayList();

        List<Score<E>> scores = new ArrayList();
        for (E e : iterable) {
            int score = scoringStrategy.getScore(e, opponentSelector
                    .on(iterable)
                    .exclude(e)
                    .select(Samples.first((int) opponentCount.getParameter())));
            scores.add(new Score(e, score));
        }

        for (Score<E> s : selector.on(scores).select(Samples.all())) {
            newPopulation.add(s.getEntity());
        }
        
        return Selection.copyOf(newPopulation);
    }

    public void setSelector(Selector<Score<E>> selector) {
        this.selector = selector;
    }

    public Selector<Score<E>> getSelector() {
        return selector;
    }

    public void setScoringStrategy(ScoringStrategy<E> scoringStrategy) {
        this.scoringStrategy = scoringStrategy;
    }

    public ScoringStrategy<E> getScoringStrategy() {
        return scoringStrategy;
    }

    public void setOpponentSelector(Selector<E> opponentSelector) {
        this.opponentSelector = opponentSelector;
    }

    public Selector<E> getOpponentSelector() {
        return opponentSelector;
    }

    public void setOpponentCount(ControlParameter opponentCount) {
        this.opponentCount = opponentCount;
    }

    public ControlParameter getOpponentCount() {
        return opponentCount;
    }
    
}
