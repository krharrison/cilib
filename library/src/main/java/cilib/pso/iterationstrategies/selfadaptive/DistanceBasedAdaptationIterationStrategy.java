package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.IterationStrategy;
import cilib.entity.Property;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.adaptationstrategies.particle.ParticleAdaptationStrategy;
import cilib.pso.selfadaptive.adaptationstrategies.particle.RandomRegenerationParticleAdaptationStrategy;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;
import cilib.util.distancemeasure.DistanceMeasure;
import cilib.util.distancemeasure.EuclideanDistanceMeasure;

import java.util.Arrays;

public class DistanceBasedAdaptationIterationStrategy implements IterationStrategy<PSO> {

    protected IterationStrategy<PSO> delegate;
    protected DistanceMeasure measure;
    protected ParticleAdaptationStrategy adaptationStrategy;

    public DistanceBasedAdaptationIterationStrategy(){
        delegate = new SynchronousIterationStrategy();
        measure = new EuclideanDistanceMeasure();
        adaptationStrategy = new RandomRegenerationParticleAdaptationStrategy();
    }

    public DistanceBasedAdaptationIterationStrategy(DistanceBasedAdaptationIterationStrategy copy){
        this.delegate = copy.delegate.getClone();
        this.measure = copy.measure;
        this.adaptationStrategy = copy.adaptationStrategy.getClone();
    }

    @Override
    public DistanceBasedAdaptationIterationStrategy getClone() {
        return new DistanceBasedAdaptationIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {

        double[] distances = new double[algorithm.getTopology().length()];

        int index = 0;
        for(Particle p : algorithm.getTopology()){
            if(p instanceof SelfAdaptiveParticle){
                SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
                sp.put(Property.PREVIOUS_PARAMETERS, sp.getParameterSet().asVector());
                double distance = measure.distance((Vector)algorithm.getBestSolution().getPosition(), sp.getPosition());
                sp.put(Property.DISTANCE_TO_BEST, Real.valueOf(distance));
                distances[index++] = distance;
            }
        }


        //find the median distance
        Arrays.sort(distances);
        double median;
        if(distances.length % 2 == 0)  median = (distances[distances.length/2] + distances[distances.length/2 - 1])/2;
        else median = distances[distances.length/2];

        //regenerate parameters for particles that are further than the median distance
        for(Particle p : algorithm.getTopology()){
            if(p instanceof SelfAdaptiveParticle){
                SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
                if(sp.get(Property.DISTANCE_TO_BEST).doubleValue() > median){
                    adaptationStrategy.adapt(sp, algorithm);
                }
            }
        }

        delegate.performIteration(algorithm);
    }

    public void setAdaptationStrategy(ParticleAdaptationStrategy strategy){
        this.adaptationStrategy = strategy;
    }


    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return delegate.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        delegate.setBoundaryConstraint(boundaryConstraint);
    }
}
