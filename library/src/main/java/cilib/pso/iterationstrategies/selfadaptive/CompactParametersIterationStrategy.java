package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.IterationStrategy;
import cilib.math.truncatednormal.TruncatedNormal;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.problem.solution.OptimisationSolution;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.pso.selfadaptive.detectionstrategies.swarm.AlwaysTrueDetectionStrategy;
import cilib.pso.selfadaptive.detectionstrategies.swarm.PeriodicDetectionStrategy;
import cilib.pso.selfadaptive.detectionstrategies.swarm.SwarmUpdateDetectionStrategy;
import cilib.type.types.Bounds;



import java.util.*;

public class CompactParametersIterationStrategy implements IterationStrategy<PSO> {

    protected SwarmUpdateDetectionStrategy detectionStrategy;
    protected IterationStrategy<PSO> iterationStrategy;

    protected Bounds inertiaBounds;
    protected Bounds cognitiveBounds;
    protected Bounds socialBounds;

    protected double[] means;
    protected double[] devs;

    protected TruncatedNormal inertiaDistribution;
    protected TruncatedNormal cognitiveDistribution;
    protected TruncatedNormal socialDistribution;

    protected ParameterSet elite;
    protected OptimisationSolution best;
    protected int popSize;

    public CompactParametersIterationStrategy(){
        detectionStrategy = new PeriodicDetectionStrategy();
        iterationStrategy = new SynchronousIterationStrategy();
        inertiaBounds = new Bounds(0,1);
        cognitiveBounds = new Bounds(0, 4);
        socialBounds = new Bounds(0, 4);

        means = new double[3];
        devs = new double[3];
        Arrays.fill(devs, 1);

        inertiaDistribution = new TruncatedNormal(0, 1, -1, 1);
        cognitiveDistribution = new TruncatedNormal(0, 1, -1, 1);
        socialDistribution = new TruncatedNormal(0, 1, -1, 1);

        elite = new ParameterSet();

        popSize = 30;
    }

    public CompactParametersIterationStrategy(CompactParametersIterationStrategy copy){

    }

    @Override
    public CompactParametersIterationStrategy getClone() {
        return new CompactParametersIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {

        SelfAdaptiveParticle sp;
        if(algorithm.getIterations() == 0){

            for(Particle p : algorithm.getTopology()){
                sp = (SelfAdaptiveParticle) p;
                sp.setParameterSet(elite);
            }

            best = algorithm.getBestSolution().getClone();
        }
        else if(detectionStrategy.detect(algorithm)) {

            sp = (SelfAdaptiveParticle)algorithm.getTopology().head();
            ParameterSet current = sp.getParameterSet();

            if(algorithm.getBestSolution().compareTo(best) > 0){
                best = algorithm.getBestSolution().getClone(); //update best solution
                updateDistributions(popSize, current, elite);
                elite = current;    //update elite parameters
            }
            else{
                updateDistributions(popSize, elite, current);
            }

            ParameterSet params;

            do {

                double w = inertiaDistribution.sample(inertiaBounds);
                double c1 = cognitiveDistribution.sample(cognitiveBounds);
                double c2 = socialDistribution.sample(socialBounds);

                params = ParameterSet.fromValues(w, c1, c2);
            } while (!params.isConvergent());

            for(Particle p : algorithm.getTopology()){
                sp = (SelfAdaptiveParticle) p;
                sp.setParameterSet(params);
            }
        }

        iterationStrategy.performIteration(algorithm);

    }

    public void updateDistributions(int popSize, ParameterSet winner, ParameterSet loser){
        double[] win = winner.asArray();
        double[] los = loser.asArray();

        double[] newMeans = new double[3];

        for(int i = 0 ; i < 3; i++){
            newMeans[i] = means[i] + (1.0/popSize) * (win[i] - los[i]);
            double var =  devs[i]*devs[i];
            double meanSquare = means[i] * means[i];
            double newMeanSquare = newMeans[i] * newMeans[i];
            double temp = (1.0 / popSize) * (win[i] * win[i] - los[i] * los[i]);

            devs[i] = Math.sqrt(var + meanSquare - newMeanSquare + temp);
            means[i] = newMeans[i];
        }

        inertiaDistribution.updateParameters(means[0], devs[0]);
        cognitiveDistribution.updateParameters(means[1], devs[1]);
        socialDistribution.updateParameters(means[2], devs[2]);
    }



    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return iterationStrategy.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        iterationStrategy.setBoundaryConstraint(boundaryConstraint);
    }


    public void setIterationStrategy(IterationStrategy<PSO> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    public void setDetectionStrategy(SwarmUpdateDetectionStrategy detectionStrategy) {
        this.detectionStrategy = detectionStrategy;
    }

}
