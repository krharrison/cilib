package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.IterationStrategy;
import cilib.entity.Property;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.problem.solution.Fitness;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.adaptationstrategies.FireflyAdaptationStrategy;
import cilib.pso.selfadaptive.adaptationstrategies.SwarmAdaptationStrategy;
import cilib.pso.selfadaptive.adaptationstrategies.particle.CrossoverAdaptationStrategy;
import cilib.pso.selfadaptive.adaptationstrategies.particle.ParticleAdaptationStrategy;
import cilib.pso.selfadaptive.detectionstrategies.particle.ParticleUpdateDetectionStrategy;
import cilib.pso.selfadaptive.detectionstrategies.particle.AlwaysTrueDetectionStrategy;
import cilib.pso.selfadaptive.detectionstrategies.swarm.SwarmUpdateDetectionStrategy;

import java.util.HashMap;

public class LudwigParameterFitnessIterationStrategy implements IterationStrategy<PSO> {

    protected ParticleAdaptationStrategy adaptationStrategy;
    protected HashMap<Particle, Integer> gBestUpdates;
    protected HashMap<Particle, Integer> pBestUpdates;
    protected ParticleUpdateDetectionStrategy detectionStrategy;
    protected IterationStrategy<PSO> iterationStrategy;
    protected double pBestUpdateWeight;
    protected double gBestUpdateWeight;


    public LudwigParameterFitnessIterationStrategy(){
        adaptationStrategy = new CrossoverAdaptationStrategy();
        detectionStrategy = new AlwaysTrueDetectionStrategy();
        gBestUpdates = new HashMap<>();
        pBestUpdates = new HashMap<>();
        iterationStrategy = new SynchronousIterationStrategy();
        pBestUpdateWeight = 1.0;
        gBestUpdateWeight = 6.0;
    }

    @Override
    public IterationStrategy<PSO> getClone() {
        return null;
    }

    @Override
    public void performIteration(PSO algorithm) {

        if(algorithm.getIterations() == 0){
            for(Particle p : algorithm.getTopology()){
                gBestUpdates.put(p, 0);
                pBestUpdates.put(p, 0);
            }
        }
        else {
            Fitness gBest = algorithm.getBestSolution().getFitness();

            for (Particle p : algorithm.getTopology()) {
                if (p.get(Property.PBEST_STAGNATION_COUNTER).intValue() == 0) {
                    int count = pBestUpdates.get(p);
                    pBestUpdates.put(p, count + 1);
                    //only check for gbest if pbest was updated
                    if (p.getFitness().compareTo(gBest) == 0) {
                        count = gBestUpdates.get(p);
                        gBestUpdates.put(p, count + 1);
                    }
                }
            }

            double sigma = 0;

            //calculate fitnesses
            for (Particle p : algorithm.getTopology()) {
                //double fit = p.getFitness().getValue();
                //double prevFit = p.get(Property.PREVIOUS_FITNESS).getValue();
                double delta = p.getFitness().getValue() - p.get(Property.PREVIOUS_FITNESS).getValue();
                if (delta < 0) sigma += -delta;
                else sigma++; //TODO: value to increment when delta positive?
            }

            for (Particle p : algorithm.getTopology()) {
                double fit = p.getFitness().getValue();
                double prevFit = p.get(Property.PREVIOUS_FITNESS).getValue();
                //TODO: if fit NaN and prevFit not, bad -> if prev NaN and fit not, good :s
                double delta = p.getFitness().getValue() - p.get(Property.PREVIOUS_FITNESS).getValue();

                double e = delta / sigma;

                double paramFitness = e * (1 + pBestUpdateWeight * pBestUpdates.get(p) + gBestUpdateWeight * gBestUpdates.get(p));
                SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;

                sp.getParameterSet().setFitness(paramFitness);//negate since parameter set has maximization fitness
            }

            for (Particle part : algorithm.getTopology()) {
                if (detectionStrategy.detect(part)) {
                    adaptationStrategy.adapt(part, algorithm);
                }
            }

            iterationStrategy.performIteration(algorithm);
        }


    }

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return iterationStrategy.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        iterationStrategy.setBoundaryConstraint(boundaryConstraint);
    }

    public ParticleAdaptationStrategy getAdaptationStrategy() {
        return adaptationStrategy;
    }

    public void setAdaptationStrategy(ParticleAdaptationStrategy adaptationStrategy) {
        this.adaptationStrategy = adaptationStrategy;
    }

    public ParticleUpdateDetectionStrategy getDetectionStrategy() {
        return detectionStrategy;
    }

    public void setDetectionStrategy(ParticleUpdateDetectionStrategy detectionStrategy) {
        this.detectionStrategy = detectionStrategy;
    }
}
