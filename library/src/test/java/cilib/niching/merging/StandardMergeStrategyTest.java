/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.niching.merging;

import cilib.niching.NichingFunctionsTest;
import cilib.problem.solution.MinimisationFitness;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.type.types.container.Vector;

import org.junit.Assert;
import org.junit.Test;

public class StandardMergeStrategyTest {

    @Test
    public void testStandardMerge() {
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();

        Particle p1 = NichingFunctionsTest.createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle p2 = NichingFunctionsTest.createParticle(new MinimisationFitness(0.0), Vector.of(Math.sqrt(0.6), Math.sqrt(0.6)));
        Particle p3 = NichingFunctionsTest.createParticle(new MinimisationFitness(2.0), Vector.of(Math.sqrt(0.3), Math.sqrt(0.3)));
        Particle p4 = NichingFunctionsTest.createParticle(new MinimisationFitness(3.0), Vector.of(1.0, 1.0));

        pso1.setTopology(fj.data.List.list(p1));
        pso2.setTopology(fj.data.List.list(p2, p3, p4));

        StandardMergeStrategy merge = new StandardMergeStrategy();

        Assert.assertEquals(4, merge.f(pso1, pso2).getTopology().length());
        Assert.assertEquals(merge.f(pso1, pso2).getBestSolution().getPosition(), pso2.getBestSolution().getPosition());
    }
}
