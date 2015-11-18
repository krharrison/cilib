/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.util.selection.recipes;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import cilib.problem.solution.MinimisationFitness;
import cilib.type.types.Bounds;
import cilib.type.types.container.Vector;
import cilib.ec.Individual;
import cilib.type.types.Real;
import cilib.problem.boundaryconstraint.ClampingBoundaryConstraint;
import cilib.entity.Property;

public class FeasibilitySelectorTest {

    @Test
    public void onTest() {
        FeasibilitySelector selector = new FeasibilitySelector();
        selector.setConstraint(new ClampingBoundaryConstraint());

        Individual individual1 = new Individual();
        Bounds bounds = new Bounds(-5.0, 5.0);
        Vector.Builder candidateSolutionBuilder = Vector.newBuilder();
        candidateSolutionBuilder.add(Real.valueOf(-6.0, bounds));
        candidateSolutionBuilder.add(Real.valueOf(7.0, bounds));
        candidateSolutionBuilder.add(Real.valueOf(12.5, bounds));
        individual1.setPosition(candidateSolutionBuilder.build());
        individual1.put(Property.FITNESS, new MinimisationFitness(2.0));

        Individual individual2 = new Individual();
        Bounds bounds2 = new Bounds(-10.0, 7.0);
        Vector.Builder candidateSolutionBuilder2 = Vector.newBuilder();
        candidateSolutionBuilder2.add(Real.valueOf(-6.0, bounds2));
        candidateSolutionBuilder2.add(Real.valueOf(6.0, bounds2));
        candidateSolutionBuilder2.add(Real.valueOf(-5.5, bounds2));
        individual2.setPosition(candidateSolutionBuilder2.build());
        individual2.put(Property.FITNESS, new MinimisationFitness(5.2));

        ArrayList list = new ArrayList();
        list.add(individual1);
        list.add(individual2);

        Individual resultingBest = (Individual) selector.on(list).select();

        Assert.assertTrue(((Vector) individual2.getPosition()).containsAll((Vector) resultingBest.getPosition()));

        Individual individual3 = new Individual();
        Bounds bounds3 = new Bounds(-5.0, 5.0);
        Vector.Builder candidateSolutionBuilder3 = Vector.newBuilder();
        candidateSolutionBuilder3.add(Real.valueOf(-3.0, bounds3));
        candidateSolutionBuilder3.add(Real.valueOf(-2.0, bounds3));
        candidateSolutionBuilder3.add(Real.valueOf(1.5, bounds3));
        individual3.setPosition(candidateSolutionBuilder3.build());
        individual3.put(Property.FITNESS, new MinimisationFitness(2.1));

        ArrayList feasibleList = new ArrayList();
        feasibleList.add(individual2);
        feasibleList.add(individual3);

        Individual resultingFeasible = (Individual) selector.on(feasibleList).select();

        Assert.assertTrue(((Vector) individual3.getPosition()).containsAll((Vector) resultingFeasible.getPosition()));

        Individual individual4 = new Individual();
        Bounds bounds4 = new Bounds(-5.0, 5.0);
        Vector.Builder candidateSolutionBuilder4 = Vector.newBuilder();
        candidateSolutionBuilder4.add(Real.valueOf(-6.0, bounds4));
        candidateSolutionBuilder4.add(Real.valueOf(6.0, bounds4));
        candidateSolutionBuilder4.add(Real.valueOf(-5.5, bounds4));
        individual4.setPosition(candidateSolutionBuilder4.build());
        individual4.put(Property.FITNESS, new MinimisationFitness(2.1));

        ArrayList infeasibleList = new ArrayList();
        infeasibleList.add(individual1);
        infeasibleList.add(individual4);

        Individual resultingInfeasible = (Individual) selector.on(infeasibleList).select();

        Assert.assertTrue(((Vector) individual4.getPosition()).containsAll((Vector) resultingInfeasible.getPosition()));
    }

    @Test
    public void selectBestOfFeasibleTest() {
        FeasibilitySelector selector = new FeasibilitySelector();
        selector.setConstraint(new ClampingBoundaryConstraint());

        Individual individual1 = new Individual();
        Bounds bounds = new Bounds(-5.0, 5.0);
        Vector.Builder candidateSolutionBuilder = Vector.newBuilder();
        candidateSolutionBuilder.add(Real.valueOf(-4.0, bounds));
        candidateSolutionBuilder.add(Real.valueOf(3.0, bounds));
        candidateSolutionBuilder.add(Real.valueOf(3.5, bounds));
        individual1.setPosition(candidateSolutionBuilder.build());
        individual1.put(Property.FITNESS, new MinimisationFitness(2.0));

        Individual individual2 = new Individual();
        Bounds bounds2 = new Bounds(-10.0, 8.0);
        Vector.Builder candidateSolutionBuilder2 = Vector.newBuilder();
        candidateSolutionBuilder2.add(Real.valueOf(-7.0, bounds2));
        candidateSolutionBuilder2.add(Real.valueOf(6.0, bounds2));
        candidateSolutionBuilder2.add(Real.valueOf(-9.5, bounds2));
        individual2.setPosition(candidateSolutionBuilder2.build());
        individual2.put(Property.FITNESS, new MinimisationFitness(5.2));

        ArrayList list = new ArrayList();
        list.add(individual1);
        list.add(individual2);

        Individual resultingBest = selector.selectBestOfFeasible(list);

        Assert.assertEquals(individual1, resultingBest);

    }

    @Test
    public void selectBestOfInfeasibleTest() {
        FeasibilitySelector selector = new FeasibilitySelector();
        selector.setConstraint(new ClampingBoundaryConstraint());

        Individual individual1 = new Individual();
        Bounds bounds = new Bounds(-5.0, 5.0);
        Vector.Builder candidateSolutionBuilder = Vector.newBuilder();
        candidateSolutionBuilder.add(Real.valueOf(-6.0, bounds));
        candidateSolutionBuilder.add(Real.valueOf(7.0, bounds));
        candidateSolutionBuilder.add(Real.valueOf(12.5, bounds));
        individual1.setPosition(candidateSolutionBuilder.build());
        individual1.put(Property.FITNESS, new MinimisationFitness(2.0));

        Individual individual2 = new Individual();
        Bounds bounds2 = new Bounds(-5.0, 5.0);
        Vector.Builder candidateSolutionBuilder2 = Vector.newBuilder();
        candidateSolutionBuilder2.add(Real.valueOf(-6.0, bounds2));
        candidateSolutionBuilder2.add(Real.valueOf(6.0, bounds2));
        candidateSolutionBuilder2.add(Real.valueOf(-5.5, bounds2));
        individual2.setPosition(candidateSolutionBuilder2.build());
        individual2.put(Property.FITNESS, new MinimisationFitness(5.2));

        ArrayList list = new ArrayList();
        list.add(individual1);
        list.add(individual2);

        Individual resultingBest = (Individual) selector.on(list).select();

        Assert.assertTrue(((Vector) individual2.getPosition()).containsAll((Vector) resultingBest.getPosition()));
    }

}
