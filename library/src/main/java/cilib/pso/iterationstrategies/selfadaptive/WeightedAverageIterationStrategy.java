package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.IterationStrategy;
import cilib.math.random.generator.Rand;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.pso.selfadaptive.detectionstrategies.swarm.SwarmUpdateDetectionStrategy;
import cilib.pso.selfadaptive.parametersetgenerator.ParameterSetGenerator;

import java.util.List;
import java.util.ArrayList;

public class WeightedAverageIterationStrategy implements IterationStrategy<PSO> {

    protected ParameterSetGenerator parameterSetGenerator;
    protected int poolSize;
    protected List<ParameterSet> parameterPool;
    protected SwarmUpdateDetectionStrategy detectionStrategy;
    protected IterationStrategy<PSO> iterationStrategy;

    @Override
    public IterationStrategy<PSO> getClone() {
        return null;
    }

    @Override
    public void performIteration(PSO algorithm) {
        if(algorithm.getIterations() == 0){
            initializeParameterPool();
            assignParameters(algorithm);
        }

        iterationStrategy.performIteration(algorithm); //run iteration
        //TODO: what indicates a success?
        double delta = 1;
        updateWeights(delta);

        if(detectionStrategy.detect(algorithm)){
            assignParameters(algorithm);
        }
    }

    private void initializeParameterPool(){
        parameterPool = new ArrayList<>(poolSize);
        for(int i = 0; i < poolSize; i++){
            ParameterSet parameterSet = parameterSetGenerator.generate();
            parameterSet.setFitness(Rand.nextDouble());
            parameterPool.add(parameterSet);
        }
    }

    private void assignParameters(PSO algorithm){
        ParameterSet params = calculateParameters();

        for (Particle p : algorithm.getTopology()) {
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            sp.setParameterSet(params);
        }
    }

    private ParameterSet calculateParameters(){
        double sumW = 0;
        double sumC1 = 0;
        double sumC2 = 0;
        double sumWeights = 0;

        for(ParameterSet p : parameterPool){
            double weight = p.getFitness().getValue();

            sumW += weight * p.getInertiaWeight().getParameter();
            sumC1 += weight * p.getCognitiveAcceleration().getParameter();
            sumC2 += weight * p.getSocialAcceleration().getParameter();

            sumWeights += weight;
        }

        return ParameterSet.fromValues(sumW / sumWeights, sumC1 / sumWeights, sumC2/sumWeights);
    }

    private void updateWeights(double delta){
        double sumWeights = 0;

        for(ParameterSet p : parameterPool){
            sumWeights += p.getFitness().getValue();
        }

        for(ParameterSet p : parameterPool){
            double proportion = p.getFitness().getValue() / sumWeights;
            p.incrementFitness(delta * proportion);

            //if(p.getFitness().getValue() < minWeight, )
        }
    }

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return null;
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {

    }
}
