package cilib.pso.iterationstrategies.selfadaptive.classifier;

import cilib.algorithm.population.IterationStrategy;
import cilib.problem.Problem;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.pso.selfadaptive.detectionstrategies.particle.ParticleUpdateDetectionStrategy;
import cilib.pso.selfadaptive.detectionstrategies.particle.PeriodicDetectionStrategy;
import cilib.pso.selfadaptive.parametersetgenerator.ClassifierModelParameterSetGenerator;
import cilib.type.types.container.Vector;

import java.util.ArrayList;
import java.util.List;

public class AdaptiveClassifierParameterSelectionIterationStrategy implements IterationStrategy<PSO> {

    protected String modelPath;
    protected int samplingIterations;
    List<Sample> samples;
    protected IterationStrategy<PSO> delegate;
    protected ClassifierModelParameterSetGenerator generator;
    protected double dispersionSampleSize;
    protected ParticleUpdateDetectionStrategy detectionStrategy;

    public AdaptiveClassifierParameterSelectionIterationStrategy(){
        samplingIterations = 33; //this makes 990 samples with 30 particles
        samples = new ArrayList<>();
        delegate = new SynchronousIterationStrategy();
        dispersionSampleSize = 0.1;
        detectionStrategy = new PeriodicDetectionStrategy();
    }

    @Override
    public IterationStrategy<PSO> getClone() {
        return null;
    }

    @Override
    public void performIteration(PSO algorithm) {

        //generate the samples
        if(algorithm.getIterations() < samplingIterations){
            Problem problem = algorithm.getOptimisationProblem();
            Vector domain = (Vector)problem.getDomain().getBuiltRepresentation();

            //don't actually need the particles, but we want to perform one sample per particle
            for (Particle p : algorithm.getTopology()){
                Vector.Builder builder;
                builder = Vector.newBuilder();
                builder.addAll(domain);
                Vector sample = builder.buildRandom();

                double fitness = problem.getFitness(sample).getValue();

                samples.add(new Sample(sample, fitness));
            }
        }
        else { //done random sampling
            if (algorithm.getIterations() == samplingIterations) { //calculate measures on first iteration after random sample

                double fdc = FitnessDistanceCorrelation.calculate(samples);
                double dm = Dispersion.calculate(samples, dispersionSampleSize);

                generator = new ClassifierModelParameterSetGenerator();
                generator.setDispersion(dm);
                generator.setFitnessDistanceCorrelation(fdc);
                generator.setModelPath(modelPath);

                //generate parameters using the model and measures
                for (Particle p : algorithm.getTopology()) {
                    setParameters(p);
                }
            }
            else { //check for parameter update
                for(Particle p : algorithm.getTopology()){
                    if(detectionStrategy.detect(p)){
                        setParameters(p);
                    }
                }
            }

            delegate.performIteration(algorithm);
        }

    }

    private void setParameters(Particle p){
        ParameterSet parameterSet;

        do{
            parameterSet = generator.generate();
        } while (!parameterSet.isConvergent());

        SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
        sp.setParameterSet(parameterSet);
    }

    public ParticleUpdateDetectionStrategy getDetectionStrategy() {
        return detectionStrategy;
    }

    public void setDetectionStrategy(ParticleUpdateDetectionStrategy detectionStrategy) {
        this.detectionStrategy = detectionStrategy;
    }

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return delegate.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        delegate.setBoundaryConstraint(boundaryConstraint);
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public int getSamplingIterations() {
        return samplingIterations;
    }

    public void setSamplingIterations(int samplingIterations) {
        this.samplingIterations = samplingIterations;
    }

    public IterationStrategy<PSO> getDelegate() {
        return delegate;
    }

    public void setDelegate(IterationStrategy<PSO> delegate) {
        this.delegate = delegate;
    }

    public double getDispersionSampleSize() {
        return dispersionSampleSize;
    }

    public void setDispersionSampleSize(double dispersionSampleSize) {
        this.dispersionSampleSize = dispersionSampleSize;
    }
}
