package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.IterationStrategy;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.entity.Property;
import cilib.math.random.generator.Rand;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.pso.selfadaptive.detectionstrategies.particle.PBestStagnationDetectionStrategy;
import cilib.pso.selfadaptive.detectionstrategies.particle.ParticleUpdateDetectionStrategy;
import cilib.pso.selfadaptive.parametersetgenerator.ConvergentParameterSetGenerator;
import cilib.pso.selfadaptive.parametersetgenerator.ParameterSetGenerator;
import cilib.util.selection.recipes.Selector;
import cilib.util.selection.recipes.TournamentSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

public class RandomPoolIterationStrategy implements IterationStrategy<PSO> {
    protected IterationStrategy<PSO> iterationStrategy;
    protected ParticleUpdateDetectionStrategy detectionStrategy;
    protected Selector<ParameterSet> parameterSelector;
    protected List<ParameterSet> parameterPool;
    protected int poolSize;
    protected Map<ParameterSet, List<Integer>> successCounters;
    protected ControlParameter windowSize;
    protected ParameterSetGenerator parameterSetGenerator;


    public RandomPoolIterationStrategy(){
        this.iterationStrategy = new SynchronousIterationStrategy();
        this.detectionStrategy = new PBestStagnationDetectionStrategy();
        this.parameterSelector = new TournamentSelector<>();
        this.poolSize = 50;
        this.successCounters = new HashMap<>();
        this.windowSize = ConstantControlParameter.of(10);
        this.parameterSetGenerator = new ConvergentParameterSetGenerator();
        ((TournamentSelector<ParameterSet>) this.parameterSelector).setTournamentSize(ConstantControlParameter.of(0.1));
    }

    public RandomPoolIterationStrategy(RandomPoolIterationStrategy copy){
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.detectionStrategy = copy.detectionStrategy.getClone();
        this.parameterSelector = copy.parameterSelector;
        this.poolSize = copy.poolSize;
        this.parameterPool = new ArrayList<>(copy.parameterPool);
        this.successCounters = new HashMap<>(copy.successCounters);
        this.windowSize = copy.windowSize.getClone();
        this.parameterSetGenerator = copy.parameterSetGenerator.getClone();
    }

    @Override
    public RandomPoolIterationStrategy getClone() {
        return new RandomPoolIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        checkState((int) windowSize.getParameter() > 0, "windowSize must be bigger than 0.");
        checkState(poolSize > 0, "poolSize must be bigger than 0.");

        //on first iteration, generate pool of parameter sets and assign randomly to each particle
        if(algorithm.getIterations() == 0){
            initializeParameterPool();
            assignInitialParameters(algorithm); //assign random parameter set to each particle
        }

        int iterationIndex = algorithm.getIterations()%(int)windowSize.getParameter();

        updateFitnesses(); //update fitness of each parameter set
        updateParticleParameters(algorithm, iterationIndex); //select parameter set for each particle, if needed
        iterationStrategy.performIteration(algorithm); //run iteration
        updateSuccessCounters(algorithm, iterationIndex); //update success counters
    }

    private void initializeParameterPool(){
        parameterPool = new ArrayList<>(poolSize);
        for(int i = 0; i < poolSize; i++){
            ParameterSet parameterSet = parameterSetGenerator.generate();
            parameterPool.add(parameterSet);
            addToSuccessCounters(parameterSet);
        }
    }

    private void assignInitialParameters(PSO algorithm){
        for (Particle p : algorithm.getTopology()) {
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            int index = Rand.nextInt(poolSize);
            sp.setParameterSet(parameterPool.get(index));
        }
    }

    private void updateFitnesses(){
        for(ParameterSet param : parameterPool){
            int sum = 0;

            for(int i = 0; i < (int) windowSize.getParameter(); i++) {
                sum += successCounters.get(param).get(i);
            }
            param.setFitness(sum);
        }
    }

    private void updateParticleParameters(PSO algorithm, int iterationIndex){
        for(Particle p : algorithm.getTopology()){
            if(detectionStrategy.detect(p)){
                SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;

                ParameterSet selected = parameterSelector.on(parameterPool).select();
                sp.setParameterSet(selected);
            }
        }

        //reset success counter for this iteration
        for(ParameterSet param : parameterPool){
            successCounters.get(param).set(iterationIndex, 0);
        }
    }

    private void updateSuccessCounters(PSO algorithm, int iterationIndex){
        for (Particle p : algorithm.getTopology()) {
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;

            //if the stagnation counter is 0, then pbest must have been updated this iteration -> success!
            if(p.get(Property.PBEST_STAGNATION_COUNTER).intValue() == 0){
                //increment success counter for this parameter set
                int value = successCounters.get(sp.getParameterSet()).get(iterationIndex);
                successCounters.get(sp.getParameterSet()).set(iterationIndex, value + 1);
            }
        }
    }

    private void addToSuccessCounters(ParameterSet parameterSet) {
        ArrayList<Integer> zeroList = new ArrayList<>((int)windowSize.getParameter());
        for(int i = 0; i < (int) windowSize.getParameter(); i++) {
            zeroList.add(0);
        }

        successCounters.put(parameterSet, zeroList);
    }

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return this.iterationStrategy.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        this.iterationStrategy.setBoundaryConstraint(boundaryConstraint);
    }

    public IterationStrategy<PSO> getIterationStrategy() {
        return iterationStrategy;
    }

    public void setIterationStrategy(IterationStrategy<PSO> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    public ParticleUpdateDetectionStrategy getDetectionStrategy() {
        return detectionStrategy;
    }

    public void setDetectionStrategy(ParticleUpdateDetectionStrategy detectionStrategy) {
        this.detectionStrategy = detectionStrategy;
    }

    public Selector<ParameterSet> getParameterSelector() {
        return parameterSelector;
    }

    public void setParameterSelector(Selector<ParameterSet> parameterSelector) {
        this.parameterSelector = parameterSelector;
    }

    public List<ParameterSet> getParameterPool() {
        return parameterPool;
    }

    public void setParameterPool(List<ParameterSet> parameterPool) {
        this.parameterPool = parameterPool;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public Map<ParameterSet, List<Integer>> getSuccessCounters() {
        return successCounters;
    }

    public void setSuccessCounters(Map<ParameterSet, List<Integer>> successCounters) {
        this.successCounters = successCounters;
    }

    public ControlParameter getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(ControlParameter windowSize) {
        this.windowSize = windowSize;
    }

    public ParameterSetGenerator getParameterSetGenerator() {
        return parameterSetGenerator;
    }

    public void setParameterSetGenerator(ParameterSetGenerator parameterSetGenerator) {
        this.parameterSetGenerator = parameterSetGenerator;
    }
}
