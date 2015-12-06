/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.velocityprovider;

import cilib.pso.particle.SelfAdaptiveParticle;
import fj.P1;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.math.random.generator.Rand;
import cilib.pso.guideprovider.GuideProvider;
import cilib.pso.guideprovider.NBestGuideProvider;
import cilib.pso.guideprovider.PBestGuideProvider;
import cilib.pso.particle.Particle;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.type.types.Numeric;
import cilib.type.types.container.StructuredType;
import cilib.type.types.container.Vector;
import cilib.util.Vectors;

/**
 * Use the standard velocity update equation using the parameters extracted from
 * the particle
 */
public class SelfAdaptiveVelocityProvider implements VelocityProvider {

    //private ControlParameter vMax;

    private GuideProvider globalGuideProvider;
    private GuideProvider localGuideProvider;

    public SelfAdaptiveVelocityProvider(){
        //this.vMax = ConstantControlParameter.of(Double.NaN);
        this.globalGuideProvider = new NBestGuideProvider();
        this.localGuideProvider = new PBestGuideProvider();

    }

    public SelfAdaptiveVelocityProvider(SelfAdaptiveVelocityProvider copy){
        //this.vMax = copy.vMax.getClone();
        this.globalGuideProvider = copy.globalGuideProvider.getClone();
        this.localGuideProvider = copy.localGuideProvider.getClone();
    }

    @Override
    public SelfAdaptiveVelocityProvider getClone() {
        return new SelfAdaptiveVelocityProvider(this);
    }

    private static P1<Number> random() {
        return new P1<Number>() {
            @Override
            public Number _1() {
                return Rand.nextDouble();
            }
        };
    }

    private static P1<Number> cp(final ControlParameter r) {
        return new P1<Number>() {
            @Override
            public Number _1() {
                return r.getParameter();
            }
        };
    }

    @Override
    public StructuredType get(Particle particle) {
        SelfAdaptiveParticle p = (SelfAdaptiveParticle) particle;

        Vector velocity = p.getVelocity();
        Vector position = (Vector) p.getPosition();
        Vector localGuide = (Vector) localGuideProvider.get(p);
        Vector globalGuide = (Vector) globalGuideProvider.get(p);

        Vector dampenedVelocity = Vector.copyOf(velocity).multiply(cp(p.getInertiaWeight()));
        Vector cognitiveComponent = Vector.copyOf(localGuide).subtract(position).multiply(cp(p.getCognitiveAcceleration())).multiply(random());
        Vector socialComponent = Vector.copyOf(globalGuide).subtract(position).multiply(cp(p.getSocialAcceleration())).multiply(random());
        velocity =  Vectors.sumOf(dampenedVelocity, cognitiveComponent, socialComponent).valueE("Cannot determine velocity");

        return velocity;
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