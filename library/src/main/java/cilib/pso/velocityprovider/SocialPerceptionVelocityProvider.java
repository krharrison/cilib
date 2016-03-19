/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.velocityprovider;

import cilib.controlparameter.ControlParameter;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.UniformDistribution;
import cilib.math.random.generator.Rand;
import cilib.pso.guideprovider.GuideProvider;
import cilib.pso.guideprovider.NBestGuideProvider;
import cilib.pso.guideprovider.PBestGuideProvider;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.type.types.container.StructuredType;
import cilib.type.types.container.Vector;
import cilib.util.Vectors;
import fj.P1;

public class SocialPerceptionVelocityProvider implements VelocityProvider{

    protected double lambda;

    private GuideProvider globalGuideProvider;
    private GuideProvider localGuideProvider;
    private ProbabilityDistributionFunction distribution;

    public SocialPerceptionVelocityProvider(){
        lambda = 0.5;
        globalGuideProvider = new NBestGuideProvider();
        localGuideProvider = new PBestGuideProvider();
        distribution = new UniformDistribution();
    }

    @Override
    public VelocityProvider getClone() {
        return null;
    }

    private static P1<Number> cp(final ControlParameter r) {
        return new P1<Number>() {
            @Override
            public Number _1() {
                return r.getParameter();
            }
        };
    }

    private static P1<Number> random() {
        return new P1<Number>() {
            @Override
            public Number _1() {
                return Rand.nextDouble();
            }
        };
    }

    private P1<Number> socialPerception() {
        return new P1<Number>() {
            @Override
            public Number _1() {
                if (distribution.getRandomNumber() > lambda) return 1;
                else return 0;
            }
        };
    }

    @Override
    public StructuredType get(Particle particle) {
        SelfAdaptiveParticle sp = (SelfAdaptiveParticle) particle;
        Vector velocity = sp.getVelocity();
        Vector position = (Vector) sp.getPosition();
        Vector localGuide = (Vector) localGuideProvider.get(sp);
        Vector globalGuide = (Vector) globalGuideProvider.get(sp);

        Vector dampenedVelocity = Vector.copyOf(velocity).multiply(cp(sp.getInertiaWeight()));
        Vector cognitiveComponent = Vector.copyOf(localGuide).subtract(position).multiply(cp(sp.getCognitiveAcceleration())).multiply(random());
        Vector socialComponent = Vector.copyOf(globalGuide).subtract(position).multiply(cp(sp.getSocialAcceleration())).multiply(socialPerception()).multiply(random());
        return Vectors.sumOf(dampenedVelocity, cognitiveComponent, socialComponent).valueE("Cannot determine velocity");
    }
}
