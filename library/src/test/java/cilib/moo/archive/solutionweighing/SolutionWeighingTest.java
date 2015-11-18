/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.moo.archive.solutionweighing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import cilib.problem.MOOptimisationProblem;
import cilib.problem.Problem;
import cilib.problem.solution.Fitness;
import cilib.problem.solution.MinimisationFitness;
import cilib.problem.solution.OptimisationSolution;
import cilib.type.DomainRegistry;
import cilib.type.types.Type;
import cilib.type.types.container.Vector;
import cilib.util.selection.Selection;
import cilib.util.selection.WeightedObject;
import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class SolutionWeighingTest {

    private static final double EPSILON = 0.00000000001;

    private static class DummyOptimisationProblem implements Problem {

        private static final long serialVersionUID = -2955989686805159033L;
        private int index;

        public DummyOptimisationProblem(int index) {
            this.index = index;
        }

        public DummyOptimisationProblem(DummyOptimisationProblem copy) {
        }

        @Override
        public DummyOptimisationProblem getClone() {
            return new DummyOptimisationProblem(this);
        }

        @Override
        public Fitness getFitness(Type solution) {
            Vector position = (Vector) solution;
            return new MinimisationFitness(position.doubleValueOf(index));
        }

        @Override
        public int getFitnessEvaluations() {
            return 0;
        }

        @Override
        public DomainRegistry getDomain() {
            return null;
        }
        
        @Override
        public void setDomain(String domain) {
        }
    }

    @Test
    public void testAntiClusteringWeighingStrategy() {
        MOOptimisationProblem moProblem = new MOOptimisationProblem();
        for (int i = 0; i < 2; i++) {
            moProblem.add(new DummyOptimisationProblem(i));
        }

        List<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>();

        Vector position = Vector.of(1.0, 1.0);
        solutions.add(new OptimisationSolution(position, moProblem.getFitness(position)));

        position = Vector.of(2.0, 4.0);
        solutions.add(new OptimisationSolution(position, moProblem.getFitness(position)));

        position = Vector.of(3.0, 2.0);
        solutions.add(new OptimisationSolution(position, moProblem.getFitness(position)));

        position = Vector.of(4.0, 6.0);
        solutions.add(new OptimisationSolution(position, moProblem.getFitness(position)));

        position = Vector.of(5.0, 1.0);
        solutions.add(new OptimisationSolution(position, moProblem.getFitness(position)));

        List<WeightedObject> weighedSolutions = Selection.copyOf(solutions).weigh(new AntiClusterWeighing()).weightedElements();

        Iterator<WeightedObject> weighedSolutionIterator = weighedSolutions.iterator();
        WeightedObject weighedSolution = weighedSolutionIterator.next();
        Assert.assertEquals(0.290823093508, weighedSolution.getWeight(), EPSILON);

        weighedSolution = weighedSolutionIterator.next();
        Assert.assertEquals(0.367312140917, weighedSolution.getWeight(), EPSILON);

        weighedSolution = weighedSolutionIterator.next();
        Assert.assertEquals(0.418956131219, weighedSolution.getWeight(), EPSILON);

        weighedSolution = weighedSolutionIterator.next();
        Assert.assertEquals(0.266011104583, weighedSolution.getWeight(), EPSILON);

        weighedSolution = weighedSolutionIterator.next();
        Assert.assertEquals(0.283305258318, weighedSolution.getWeight(), EPSILON);

        assertThat(weighedSolutionIterator.hasNext(), is(false));
    }
}
