/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.util.selection.weighing.entity;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import cilib.ec.Individual;
import cilib.entity.Property;
import cilib.problem.solution.Fitness;
import cilib.problem.solution.InferiorFitness;
import cilib.problem.solution.MaximisationFitness;
import cilib.problem.solution.MinimisationFitness;
import cilib.util.selection.WeightedObject;
import cilib.util.selection.weighting.EntityWeighting;
import org.junit.Assert;
import org.junit.Test;

/**
 */
public class EntityWeighingTest {

    @Test
    public void entityWeighingMaximise() {
        Individual i1 = createIndividual(new MaximisationFitness(1.0));
        Individual i2 = createIndividual(new MaximisationFitness(2.0));
        Individual i3 = createIndividual(new MaximisationFitness(3.0));

        List<Individual> individuals = Arrays.asList(i1, i2, i3);
        EntityWeighting weighing = new EntityWeighting();
        List<WeightedObject> result = Lists.newArrayList(weighing.weigh(individuals));

        Assert.assertEquals(0.0, result.get(0).getWeight(), 0.0001);
        Assert.assertEquals(0.5, result.get(1).getWeight(), 0.0001);
        Assert.assertEquals(1.0, result.get(2).getWeight(), 0.0001);
    }

    @Test
    public void entityWeighingMinimise() {
        Individual i1 = createIndividual(new MinimisationFitness(1.0));
        Individual i2 = createIndividual(new MinimisationFitness(2.0));
        Individual i3 = createIndividual(new MinimisationFitness(3.0));

        List<Individual> individuals = Arrays.asList(i1, i2, i3);
        EntityWeighting weighing = new EntityWeighting();
        List<WeightedObject> result = Lists.newArrayList(weighing.weigh(individuals));

        Assert.assertEquals(1.0, result.get(0).getWeight(), 0.0001);
        Assert.assertEquals(0.5, result.get(1).getWeight(), 0.0001);
        Assert.assertEquals(0.0, result.get(2).getWeight(), 0.0001);
    }

    @Test
    public void entityWeighingAllNaN() {
        Individual i1 = createIndividual(InferiorFitness.instance());
        Individual i2 = createIndividual(InferiorFitness.instance());
        Individual i3 = createIndividual(InferiorFitness.instance());

        List<Individual> individuals = Arrays.asList(i1, i2, i3);
        EntityWeighting weighing = new EntityWeighting();
        List<WeightedObject> result = Lists.newArrayList(weighing.weigh(individuals));

        Assert.assertEquals(1.0, result.get(0).getWeight(), 0.0001);
        Assert.assertEquals(1.0, result.get(1).getWeight(), 0.0001);
        Assert.assertEquals(1.0, result.get(2).getWeight(), 0.0001);
    }

    @Test
    public void entityWeighingSomeNaN() {
        Individual i1 = createIndividual(new MinimisationFitness(1.0));
        Individual i2 = createIndividual(new MinimisationFitness(2.0));
        Individual i3 = createIndividual(new MinimisationFitness(3.0));
        Individual i4 = createIndividual(InferiorFitness.instance());

        List<Individual> individuals = Arrays.asList(i1, i2, i3, i4);
        EntityWeighting weighing = new EntityWeighting();
        List<WeightedObject> result = Lists.newArrayList(weighing.weigh(individuals));

        Assert.assertEquals(1.0, result.get(0).getWeight(), 0.0001);
        Assert.assertEquals(0.5, result.get(1).getWeight(), 0.0001);
        Assert.assertEquals(0.0, result.get(2).getWeight(), 0.0001);
        Assert.assertEquals(0.0, result.get(3).getWeight(), 0.0001);
    }

    private Individual createIndividual(Fitness fitness) {
        Individual i = new Individual();
        i.put(Property.FITNESS, fitness);
        return i;
    }
}
