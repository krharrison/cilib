/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.problem;

import java.util.Collection;
import java.util.List;

import cilib.functions.continuous.derating.DeratingFunction;
import cilib.functions.continuous.derating.PowerDeratingFunction;
import cilib.problem.objective.Maximise;
import cilib.problem.solution.Fitness;
import cilib.type.types.Type;
import cilib.type.types.container.Vector;
import cilib.util.distancemeasure.DistanceMeasure;
import cilib.util.distancemeasure.EuclideanDistanceMeasure;

import com.google.common.collect.Lists;

/**
 * <p>Title: SequentialNichingProblem </p>
 * <p>Description: This class provides a way to modify the search
 * space such that a solution could be removed from the search
 * space and a new solution can be searched for.
 * </p>
 * <p>
 * The interested reader is referred to:
 * Beasley, D., Bull, D. R. & Martin R. R. (1993).
 * A Sequential Niche Technique for Multimodal Function Optimization.
 * Evolutionary Computation 1(2), MIT Press, pp.101-125.
 * </p>
 */
public class DeratingOptimisationProblem extends FunctionOptimisationProblem {

    private final List<Vector> solutions;
    private DeratingFunction deratingFunction;
    private DistanceMeasure distanceMeasure;

    /**
     * The default constructor.
     */
    public DeratingOptimisationProblem() {
        this.objective = new Maximise();
        this.distanceMeasure = new EuclideanDistanceMeasure();
        this.deratingFunction = new PowerDeratingFunction();
        this.solutions = Lists.<Vector>newLinkedList();
    }

    /**
     * Copy constructor.
     */
    public DeratingOptimisationProblem(DeratingOptimisationProblem copy) {
        super(copy);
        this.distanceMeasure = copy.distanceMeasure;
        this.deratingFunction = copy.deratingFunction;
        this.solutions = Lists.<Vector>newLinkedList(copy.solutions);
    }

    @Override
    public DeratingOptimisationProblem getClone() {
        return new DeratingOptimisationProblem(this);
    }

    @Override
    protected Fitness calculateFitness(Type solution) {
        Vector input = (Vector) solution;
        double fitness = super.calculateFitness(input).getValue();

        for (Vector v : solutions) {
            double distance = distanceMeasure.distance(input, v);

            if (distance < deratingFunction.getRadius()) {
                if (getObjective() instanceof Maximise)
                    fitness *= getDeratingFunction().f(Vector.of(distance));
                else
                    fitness /= getDeratingFunction().f(Vector.of(distance));
            }
        }

        return objective.evaluate(fitness);
    }

    public void addSolution(Vector solution) {
        solutions.add(solution);
    }

    public void addSolutions(Collection<Vector> solutions) {
        this.solutions.addAll(solutions);
    }

    public List<Vector> getSolutions() {
        return solutions;
    }

    public void clearSolutions() {
        solutions.clear();
    }

    public DeratingFunction getDeratingFunction() {
        return deratingFunction;
    }

    public void setDeratingFunction(DeratingFunction deratingFunction) {
        this.deratingFunction = deratingFunction;
    }

    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }
}
