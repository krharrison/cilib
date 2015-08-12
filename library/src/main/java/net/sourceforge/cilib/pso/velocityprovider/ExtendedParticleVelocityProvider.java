/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import fj.P1;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.guideprovider.GuideProvider;
import net.sourceforge.cilib.pso.guideprovider.NBestGuideProvider;
import net.sourceforge.cilib.pso.guideprovider.PBestGuideProvider;
import net.sourceforge.cilib.pso.particle.ExtendedParticle;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * Implementation of the standard / default velocity update equation for extended particles.
 */
public final class ExtendedParticleVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = 8204479765311251730L;

    private GuideProvider globalGuideProvider;
    private GuideProvider localGuideProvider;

    /** Creates a new instance of ExtendedParticleVelocityProvider. */
    public ExtendedParticleVelocityProvider() {
    	this.globalGuideProvider = new NBestGuideProvider();
        this.localGuideProvider = new PBestGuideProvider();
    }

    /**
     * Copy constructor.
     * @param copy The object to copy.
     */
    public ExtendedParticleVelocityProvider(ExtendedParticleVelocityProvider copy) {
        this.globalGuideProvider = copy.globalGuideProvider.getClone();
        this.localGuideProvider = copy.localGuideProvider.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedParticleVelocityProvider getClone() {
        return new ExtendedParticleVelocityProvider(this);
    }

    private static P1<Number> random() {
        return new P1<Number>() {
            @Override
            public Number _1() {
                return Rand.nextDouble();
            }
        };
    }

    /**
     * Perform the velocity update for the given {@linkplain Particle}.
     * @param particle The {@linkplain Particle} velocity that should be updated.
     */
    @Override
    public Vector get(Particle particle) {
    	ExtendedParticle extParticle = (ExtendedParticle) particle;
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector localGuide = (Vector) localGuideProvider.get(particle);
        Vector globalGuide = (Vector) globalGuideProvider.get(particle);
        
        Vector dampenedVelocity = Vector.copyOf(velocity).multiply(extParticle.getInertiaWeight());
        Vector cognitiveComponent = Vector.copyOf(localGuide).subtract(position).multiply(extParticle.getCognitiveWeight()).multiply(random());
        Vector socialComponent = Vector.copyOf(globalGuide).subtract(position).multiply(extParticle.getSocialWeight()).multiply(random());
        return Vectors.sumOf(dampenedVelocity, cognitiveComponent, socialComponent).valueE("Cannot determine velocity");
    }

    /**
     * Sets the GuideProvider responsible for retrieving a particle's global guide.
     * @param globalGuideProvider The guide provider to set.
     */
    public void setGlobalGuideProvider(GuideProvider globalGuideProvider) {
        this.globalGuideProvider = globalGuideProvider;
    }

    /**
     * Sets the GuideProvider responsible for retrieving a particle's local guide.
     * @param localGuideProvider The guide provider to set.
     */
    public void setLocalGuideProvider(GuideProvider localGuideProvider) {
        this.localGuideProvider = localGuideProvider;
    }
}
