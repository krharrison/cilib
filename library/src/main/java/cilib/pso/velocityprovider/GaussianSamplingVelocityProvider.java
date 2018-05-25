package cilib.pso.velocityprovider;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.controlparameter.LinearlyVaryingControlParameter;
import cilib.entity.Property;
import cilib.math.random.GaussianDistribution;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.generator.Rand;
import cilib.pso.guideprovider.GuideProvider;
import cilib.pso.guideprovider.NBestGuideProvider;
import cilib.pso.guideprovider.PBestGuideProvider;
import cilib.pso.particle.Particle;
import cilib.type.types.container.StructuredType;
import cilib.type.types.container.Vector;

public class GaussianSamplingVelocityProvider implements VelocityProvider {

    private ProbabilityDistributionFunction distribution;
    private ControlParameter localGuideProbability;
    private ControlParameter exploitProbability;
    private GuideProvider globalGuideProvider;
    private GuideProvider localGuideProvider;

    public GaussianSamplingVelocityProvider(){
        this.distribution = new GaussianDistribution();
        this.localGuideProbability = new LinearlyVaryingControlParameter(1, 0);
        this.exploitProbability = ConstantControlParameter.of(0.5);
        this.globalGuideProvider = new NBestGuideProvider();
        this.localGuideProvider = new PBestGuideProvider();
    }

    public GaussianSamplingVelocityProvider(GaussianSamplingVelocityProvider copy){
        this.distribution = copy.distribution;
        this.localGuideProbability = copy.localGuideProbability.getClone();
        this.exploitProbability = copy.exploitProbability.getClone();
        this.globalGuideProvider = copy.globalGuideProvider.getClone();
        this.localGuideProvider = copy.localGuideProvider.getClone();
    }

    @Override
    public GaussianSamplingVelocityProvider getClone() {
        return new GaussianSamplingVelocityProvider(this);
    }

    @Override
    public StructuredType get(Particle particle) {
        Vector localGuide = (Vector) localGuideProvider.get(particle);
        Vector globalGuide = (Vector) globalGuideProvider.get(particle);
        Vector position = (Vector) particle.getPosition();

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); i++) {
            boolean useLBest = Rand.nextDouble() < localGuideProbability.getParameter();

            //if pos = pBest, always use gBest, regardless of probability
            //TODO: is this safe to assume pos = pBest?
            if(particle.get(Property.PBEST_STAGNATION_COUNTER).intValue() == 0){
                useLBest = false;
            }

            Vector guide = useLBest ? localGuide : globalGuide;

            if (Rand.nextDouble() < exploitProbability.getParameter()) {
                builder.add(localGuide.doubleValueOf(i));
            } else {
                double mean = (guide.doubleValueOf(i) + position.doubleValueOf(i)) / 2;
                double sigma = Math.abs(guide.doubleValueOf(i) - position.doubleValueOf(i));
                builder.add(this.distribution.getRandomNumber(mean, sigma));
            }

        }

        return builder.build();
    }

    public ControlParameter getLocalGuideProbability() {
        return localGuideProbability;
    }

    public void setLocalGuideProbability(ControlParameter localGuideProbability) {
        this.localGuideProbability = localGuideProbability;
    }

    public ControlParameter getExploitProbability() {
        return exploitProbability;
    }

    public void setExploitProbability(ControlParameter exploitProbability) {
        this.exploitProbability = exploitProbability;
    }

    public GuideProvider getGlobalGuideProvider() {
        return globalGuideProvider;
    }

    public void setGlobalGuideProvider(GuideProvider globalGuideProvider) {
        this.globalGuideProvider = globalGuideProvider;
    }

    public GuideProvider getLocalGuideProvider() {
        return localGuideProvider;
    }

    public void setLocalGuideProvider(GuideProvider localGuideProvider) {
        this.localGuideProvider = localGuideProvider;
    }
}
