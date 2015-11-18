/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.entity.initialisation;

import cilib.controlparameter.ConstantControlParameter;
import cilib.entity.Property;
import cilib.pso.particle.Particle;
import cilib.pso.particle.StandardParticle;
import cilib.type.types.Int;
import cilib.type.types.Numeric;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import org.junit.Assert;
import org.junit.Test;

public class RandomBoundedInitialisationStrategyTest {

    @Test
    public void initialise() {
        Vector vector = Vector.of(Real.valueOf(5.0),
                Real.valueOf(10.0),
                Int.valueOf(7));

        Particle particle = new StandardParticle();
        particle.put(Property.CANDIDATE_SOLUTION, vector);

        RandomBoundedInitialisationStrategy<Particle> strategy = new RandomBoundedInitialisationStrategy<Particle>();
        strategy.lowerBound = (ConstantControlParameter.of(-5.0));
        strategy.upperBound = (ConstantControlParameter.of(5.0));
        strategy.initialise(Property.CANDIDATE_SOLUTION, particle);

        for (int i = 0; i < vector.size(); i++) {
            Numeric numeric = vector.get(i);
            Assert.assertThat(numeric.doubleValue(), is(greaterThanOrEqualTo(-5.0)));
            Assert.assertThat(numeric.doubleValue(), is(lessThanOrEqualTo(5.0)));
        }
    }

}
