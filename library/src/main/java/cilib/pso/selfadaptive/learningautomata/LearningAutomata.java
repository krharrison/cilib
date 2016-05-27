/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.learningautomata;

import cilib.util.selection.WeightedObject;
import cilib.util.selection.recipes.RouletteWheelSelector;
import com.google.common.collect.Lists;

import java.util.List;

public class LearningAutomata<T extends Comparable<T>> {
    protected double reward;
    protected double penalty;
    protected List<Action<T>> actions;
    protected RouletteWheelSelector<WeightedObject> selector;

    public LearningAutomata(){
        reward = 0.01;
        penalty = 0.01;
        this.actions = Lists.newArrayList();
        this.selector = new RouletteWheelSelector<>();
    }

    public Action<T> select(){

        List<WeightedObject> objects = Lists.newArrayListWithCapacity(actions.size());

        for(Action action : actions){
            objects.add(new WeightedObject(action, action.weight));
        }

        //TODO: will this properly return the appropriate Action object?
        return (Action<T>)selector.on(objects).select().getObject();
    }

    //update the automata after a successful iteration
    public void updateSuccessful(Action current){
        for(Action action : actions){
            double weight = action.getWeight();
            if(action.compareTo(current) == 0){
                action.setWeight(weight + reward * (1- weight));
            }
            else{
                action.setWeight(weight * (1 - reward));
            }
        }
    }

    //update the automata after an unsuccessful iteration
    public void updateUnsuccessful(Action current) {
        for (Action action : actions) {
            double weight = action.getWeight();
            if (action.compareTo(current) == 0) {
                action.setWeight(weight * (1 - penalty));
            } else {
                action.setWeight((penalty / (actions.size() - 1)) + (1 - penalty) * weight);
            }
        }
    }

    public void addAction(Action<T> action){
        this.actions.add(action);
    }
}
