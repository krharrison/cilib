package cilib.pso.velocityprovider;


import cilib.controlparameter.ControlParameter;
import cilib.controlparameter.RandomControlParameter;
import cilib.math.random.GaussianDistribution;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.pso.guideprovider.GuideProvider;
import cilib.pso.guideprovider.NBestGuideProvider;
import cilib.pso.guideprovider.PBestGuideProvider;
import cilib.pso.particle.Particle;
import cilib.pso.selfadaptive.crossover.ParentCentricCrossover;
import cilib.type.types.container.StructuredType;
import cilib.type.types.container.Vector;

import java.util.Arrays;
import java.util.List;

public class PCXGaussianVelocityProvider implements VelocityProvider {

    private ProbabilityDistributionFunction distribution;
    private GuideProvider globalGuideProvider;
    private GuideProvider localGuideProvider;
    private ParentCentricCrossover crossover;

    public PCXGaussianVelocityProvider(){
        this.distribution = new GaussianDistribution();
        this.globalGuideProvider = new NBestGuideProvider();
        this.localGuideProvider = new PBestGuideProvider();
        this.crossover = new ParentCentricCrossover();
        crossover.setSigma1(new RandomControlParameter());
        crossover.setSigma2(new RandomControlParameter());
    }

    public PCXGaussianVelocityProvider(PCXGaussianVelocityProvider copy){
        this.distribution = copy.distribution;
        this.globalGuideProvider = copy.globalGuideProvider.getClone();
        this.localGuideProvider = copy.localGuideProvider.getClone();
        this.crossover = copy.crossover;
    }

    @Override
    public PCXGaussianVelocityProvider getClone() {
        return new PCXGaussianVelocityProvider(this);
    }

    @Override
    public StructuredType get(Particle particle) {
        Vector localGuide = (Vector) localGuideProvider.get(particle);
        Vector globalGuide = (Vector) globalGuideProvider.get(particle);
        Vector position = (Vector) particle.getPosition();

        Vector offspring = crossover.crossover(Arrays.asList(position, localGuide, globalGuide)).get(0);

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); i++) {
            double x = position.get(i).doubleValue();
            double p = offspring.get(i).doubleValue();
            double mean = (x + p) / 2;
            double sigma = Math.abs(x - p);
            builder.add(distribution.getRandomNumber(mean, sigma));
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
}
