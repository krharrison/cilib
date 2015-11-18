/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.velocityprovider;


import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.math.random.generator.Rand;
import cilib.pso.guideprovider.GuideProvider;
import cilib.pso.guideprovider.NBestGuideProvider;
import cilib.pso.guideprovider.PBestGuideProvider;
import cilib.pso.particle.Particle;
import cilib.type.types.container.Vector;


/**
 * TODO: test this.
 *
 */
public class LinearVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -1624326615681760823L;

    protected ControlParameter inertiaWeight;
    protected ControlParameter socialAcceleration;
    protected ControlParameter cognitiveAcceleration;
    
    private GuideProvider globalGuideProvider;
    private GuideProvider localGuideProvider;

    /**
     * Create an instance of {@linkplain LinearVelocityProvider}.
     */
    public LinearVelocityProvider() {
        // Resetting the social and cognitive components is required to ensure
        // that during the velocity update process, only 1 random number is used.
        this.inertiaWeight = ConstantControlParameter.of(0.729844);
        this.socialAcceleration = ConstantControlParameter.of(1.496180);
        this.cognitiveAcceleration = ConstantControlParameter.of(1.496180);
        
        this.globalGuideProvider = new NBestGuideProvider();
        this.localGuideProvider = new PBestGuideProvider();
    }

    public LinearVelocityProvider(LinearVelocityProvider copy) {
        this.inertiaWeight = copy.inertiaWeight.getClone();
        this.socialAcceleration = copy.socialAcceleration.getClone();
        this.cognitiveAcceleration = copy.cognitiveAcceleration.getClone();
        this.globalGuideProvider = copy.globalGuideProvider.getClone();
        this.localGuideProvider = copy.localGuideProvider.getClone();
    }

    @Override
    public VelocityProvider getClone() {
        return new LinearVelocityProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector localGuide = (Vector) localGuideProvider.get(particle);
        Vector globalGuide = (Vector) globalGuideProvider.get(particle);

        float social = Rand.nextFloat();
        float cognitive = Rand.nextFloat();

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); ++i) {
            double value = this.inertiaWeight.getParameter()*velocity.doubleValueOf(i) +
                cognitive  * this.cognitiveAcceleration.getParameter() * (localGuide.doubleValueOf(i) - position.doubleValueOf(i)) +
                social * this.socialAcceleration.getParameter() * (globalGuide.doubleValueOf(i) - position.doubleValueOf(i));
            builder.add(value);
        }
        return builder.build();
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
