/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.pbestupdate;

import cilib.entity.Property;
import cilib.pso.particle.Particle;
import cilib.problem.solution.InferiorFitness;
import cilib.problem.solution.MinimisationFitness;
import cilib.pso.particle.StandardParticle;
import cilib.type.types.Bounds;
import cilib.type.types.Real;
import cilib.type.types.Type;
import cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 *
 */
public class BoundedPersonalBestUpdateStrategyTest {

    @Test
    public void updatePersonalBest() {
        Real real = Real.valueOf(0.0, new Bounds(-5.0, 5.0));
        Particle particle = new StandardParticle();

        particle.put(Property.FITNESS, new MinimisationFitness(200.0));
        particle.put(Property.BEST_FITNESS, new MinimisationFitness(300.0));
        particle.put(Property.CANDIDATE_SOLUTION, Vector.of(real));

        BoundedPersonalBestUpdateStrategy strategy = new BoundedPersonalBestUpdateStrategy();
        strategy.updatePersonalBest(particle);

        Assert.assertThat(particle.getBestFitness(), is(particle.getFitness()));
        Assert.assertThat(particle.getBestPosition(), is(particle.getPosition()));
    }

    @Test
    public void updatePersonalBestFails() {
        Real real = Real.valueOf(-10.0, new Bounds(-5.0, 5.0));
        Particle particle = new StandardParticle();

        particle.put(Property.FITNESS, new MinimisationFitness(200.0));
        particle.put(Property.BEST_FITNESS, new MinimisationFitness(300.0));
        particle.put(Property.CANDIDATE_SOLUTION, Vector.of(real));

        Type previousBestPosition = particle.getBestPosition();

        BoundedPersonalBestUpdateStrategy strategy = new BoundedPersonalBestUpdateStrategy();
        strategy.updatePersonalBest(particle);

        Assert.assertThat(particle.getBestFitness(), is(not(particle.getFitness())));
        Assert.assertThat(particle.getBestPosition(), is(previousBestPosition));
        Assert.assertThat(particle.getFitness(), is(InferiorFitness.instance()));
    }
}
