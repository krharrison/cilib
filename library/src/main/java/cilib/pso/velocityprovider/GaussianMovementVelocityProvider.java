package cilib.pso.velocityprovider;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.math.random.GaussianDistribution;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.generator.Rand;
import cilib.pso.guideprovider.GuideProvider;
import cilib.pso.guideprovider.NBestGuideProvider;
import cilib.pso.guideprovider.PBestGuideProvider;
import cilib.pso.particle.Particle;
import cilib.type.types.container.StructuredType;
import cilib.type.types.container.Vector;

public class GaussianMovementVelocityProvider implements VelocityProvider {

    private ProbabilityDistributionFunction distribution;
    private GuideProvider globalGuideProvider;
    private GuideProvider localGuideProvider;
    private ControlParameter exploitProbability;


    public GaussianMovementVelocityProvider(){
        this.distribution = new GaussianDistribution();
        this.globalGuideProvider = new NBestGuideProvider();
        this.localGuideProvider = new PBestGuideProvider();
        this.exploitProbability = ConstantControlParameter.of(0.5);
    }

    public GaussianMovementVelocityProvider(GaussianMovementVelocityProvider copy){
        this.distribution = copy.distribution;
        this.globalGuideProvider = copy.globalGuideProvider.getClone();
        this.localGuideProvider = copy.localGuideProvider.getClone();
        this.exploitProbability = copy.exploitProbability.getClone();
    }

    @Override
    public GaussianMovementVelocityProvider getClone() {
        return new GaussianMovementVelocityProvider(this);
    }

    @Override
    public StructuredType get(Particle particle) {
        Vector localGuide = (Vector) localGuideProvider.get(particle);
        Vector globalGuide = (Vector) globalGuideProvider.get(particle);
        Vector position = (Vector) particle.getPosition();

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); i++) {

            if (Rand.nextDouble() < exploitProbability.getParameter()) {
                builder.add(localGuide.doubleValueOf(i));
            } else {

                double x = position.get(i).doubleValue();
                double cog = Rand.nextDouble() * (localGuide.doubleValueOf(i) - position.doubleValueOf(i));
                double soc = Rand.nextDouble() * (globalGuide.doubleValueOf(i) - position.doubleValueOf(i));

                double newPos = x + cog + soc;

                double mean = (x + newPos) / 2;
                double sigma = Math.abs(x - newPos);//TODO: is this just cog + soc?
                builder.add(distribution.getRandomNumber(mean, sigma));
            }
        }

        return builder.build();
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

    public ControlParameter getExploitProbability() {
        return exploitProbability;
    }

    public void setExploitProbability(ControlParameter exploitProbability) {
        this.exploitProbability = exploitProbability;
    }
}
