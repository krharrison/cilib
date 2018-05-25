package cilib.pso.iterationstrategies.selfadaptive.classifier;

import cilib.type.types.container.Vector;

import java.util.List;
import java.util.ListIterator;

public class Sample implements Comparable<Sample> {
    private Vector values;
    private double fitness;

    public Sample(Vector values, double fitness) {
        this.values = values;
        this.fitness = fitness;
    }

    public Vector getValues() {
        return values;
    }

    public double getFitness() {
        return fitness;
    }

    @Override
    public int compareTo(Sample other) {
        return Double.compare(this.fitness, other.fitness);
    }

    public static Sample findBest(List<Sample> samples) {
        Sample best;
        ListIterator<Sample> itr = samples.listIterator();
        best = itr.next(); // first element as the current minimum

        //find minFitness
        while (itr.hasNext()) {
            final Sample curr = itr.next();
            if (curr.compareTo(best) < 0) {
                best = curr;
            }
        }

        return best;
    }
}
