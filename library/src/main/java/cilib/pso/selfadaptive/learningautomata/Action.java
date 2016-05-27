/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.learningautomata;

public class Action<T extends Comparable> implements Comparable<Action<T>>{

    protected T action;
    protected double weight;

    public Action(T action, double probability){
        this.action = action;
        this.weight = probability;
    }

    public T getAction() {
        return action;
    }

    public void setAction(T action) {
        this.action = action;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(Action<T> other) {
        return this.action.compareTo(other.action);
    }
}
