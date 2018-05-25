package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.IterationStrategy;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.math.random.generator.Rand;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.pso.PSO;
import cilib.pso.hpso.pheromoneupdate.ConstantPheromoneUpdateStrategy;
import cilib.pso.hpso.pheromoneupdate.PheromoneUpdateStrategy;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.pso.selfadaptive.detectionstrategies.particle.ParticleUpdateDetectionStrategy;
import cilib.pso.selfadaptive.detectionstrategies.particle.PeriodicDetectionStrategy;
import cilib.pso.selfadaptive.parametersetgenerator.ParameterSetGenerator;
import cilib.pso.selfadaptive.parametersetgenerator.SobolParameterSetGenerator;
import cilib.util.selection.recipes.RouletteWheelSelector;
import cilib.util.selection.recipes.Selector;
import cilib.util.selection.weighting.ParameterSetFitnessWeighting;

import java.util.ArrayList;
import java.util.List;

public class PheromoneIterationStrategy implements IterationStrategy<PSO> {
    protected PheromoneUpdateStrategy pheromoneUpdateStrategy;
    protected int poolSize;
    protected List<ParameterSet> parameterList;
    protected Selector<ParameterSet> selectionRecipe;
    protected IterationStrategy<PSO> iterationStrategy;
    protected ParticleUpdateDetectionStrategy detectionStrategy;
    protected double minPheromone;
    protected ParameterSetGenerator parameterSetGenerator;
    //protected ControlParameter evaporationRate;
    protected double initialPheromone;


    /**
     * Create a new instance of {@linkplain PheromoneIterationStrategy}.
     */
    public PheromoneIterationStrategy() {
        this.minPheromone = 0.01;
        this.parameterList = new ArrayList<>();
        this.poolSize = 50;
        this.pheromoneUpdateStrategy = new ConstantPheromoneUpdateStrategy();

        this.iterationStrategy = new SynchronousIterationStrategy();
        this.detectionStrategy = new PeriodicDetectionStrategy();

        this.parameterSetGenerator = new SobolParameterSetGenerator();

        this.selectionRecipe = new RouletteWheelSelector<>(new ParameterSetFitnessWeighting());
        //this.evaporationRate = ConstantControlParameter.of(0.25);
        this.initialPheromone = 1.0;

    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public PheromoneIterationStrategy(PheromoneIterationStrategy copy) {
        this.minPheromone = copy.minPheromone;
        this.pheromoneUpdateStrategy = copy.pheromoneUpdateStrategy;
        this.poolSize = copy.poolSize;
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.detectionStrategy = copy.detectionStrategy.getClone();
        this.selectionRecipe = copy.selectionRecipe;
        this.parameterList = new ArrayList<>(copy.parameterList);
        this.parameterSetGenerator = copy.parameterSetGenerator.getClone();
        //this.evaporationRate = copy.evaporationRate;
        this.initialPheromone = copy.initialPheromone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PheromoneIterationStrategy getClone() {
        return new PheromoneIterationStrategy(this);
    }

    /**
     * Structure of Dynamic Heterogeneous iteration strategy:
     *
     * <ol>
     *   <li>For each particle:</li>
     *   <li>Check if particle must change its behavior</li>
     *   <li>If particle must change its behavior:</li>
     *   <ol>
     *     <li>Select a new behavior to the particle from the behavior pool</li>
     *   </ol>
     *   <li>Perform normal iteration</li>
     *   <li>Update and evaporate pheromoneConcentration levels</li>
     * </ol>
     */
    @Override
    public void performIteration(PSO algorithm) {

        if(algorithm.getIterations() == 0){
            initializeParameterPool();
            assignInitialParameters(algorithm); //assign random parameter set to each particle

        }

        ParameterSet params;
        for(Particle p : algorithm.getTopology()) {
            if (detectionStrategy.detect(p)) {
                SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
                params = selectionRecipe.on(parameterList).select();
                sp.setParameterSet(params);
            }
        }

        iterationStrategy.performIteration(algorithm);

        //update the pheromone for parameters that were used
        for(Particle p : algorithm.getTopology()) {
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            params = sp.getParameterSet();
            double deltaP = pheromoneUpdateStrategy.updatePheromone(p);
            params.setFitness(Math.max(params.getFitness().getValue() + deltaP, minPheromone));

            //if parameter set has minimum pheromone level, replace using selector
/*            if(params.getFitness().getValue() <= minPheromone.getParameter()){
                //replace in particle, replace in list
                parameterList.remove(params);
                params = parameterSetGenerator.generate();
                sp.setParameterSet(params);
                //params.setFitness(initialPheromone);
                parameterList.add(params);
            }*/

        }

        //TODO: replace unused parameters.
        evaporate();
    }

    public void initializeParameterPool() {

        for(int i = 0; i < poolSize; i++){
            ParameterSet params = parameterSetGenerator.generate();
            params.setFitness(initialPheromone);
            parameterList.add(params);
        }
    }

    private void assignInitialParameters(PSO algorithm){
        for (Particle p : algorithm.getTopology()) {
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            int index = Rand.nextInt(poolSize);
            sp.setParameterSet(parameterList.get(index));
        }
    }


    private void evaporate(){

        double sumPheromone = 0;
        for(ParameterSet p : parameterList) {
            sumPheromone += p.getFitness().getValue();
        }

        //evaporate pheromones levels
        for(ParameterSet param : parameterList) {
            double pb = param.getFitness().getValue();
            //param.setFitness(Math.max(param.getFitness().getValue() * (1 - evaporationRate.getParameter()), minPheromone.getParameter()));
            param.setFitness(Math.max((sumPheromone - pb) * pb / sumPheromone, minPheromone));
            //param.setFitness(Math.max(param.getFitness().getValue() * (1 - param.getFitness().getValue() / sumPheromone), minPheromone.getParameter()));

        }

    }
    /**
     * Sets the PheromoneUpdateStrategy
     *
     * @param strat The strategy to change to.
     */
    public void setPheromoneUpdateStrategy(PheromoneUpdateStrategy strat) {
        this.pheromoneUpdateStrategy = strat;
    }


    public List<ParameterSet> getParameterList() {
        return parameterList;
    }

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return this.iterationStrategy.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        this.iterationStrategy.setBoundaryConstraint(boundaryConstraint);
    }

    public void setIterationStrategy(IterationStrategy<PSO> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    public void setDetectionStrategy(ParticleUpdateDetectionStrategy detectionStrategy) {
        this.detectionStrategy = detectionStrategy;
    }

    public void setSelectionRecipe(Selector<ParameterSet> selectionRecipe) {
        this.selectionRecipe = selectionRecipe;
    }

    public void setParameterSetGenerator(ParameterSetGenerator parameterSetGenerator) {
        this.parameterSetGenerator = parameterSetGenerator;
    }


    public PheromoneUpdateStrategy getPheromoneUpdateStrategy() {
        return pheromoneUpdateStrategy;
    }

    public IterationStrategy<PSO> getIterationStrategy() {
        return iterationStrategy;
    }

    public ParticleUpdateDetectionStrategy getDetectionStrategy() {
        return detectionStrategy;
    }

    public Selector<ParameterSet> getSelectionRecipe() {
        return selectionRecipe;
    }

    public void setMinPheromone(double minPheromone) {
        this.minPheromone = minPheromone;
    }

    public double getMinPheromone() {
        return minPheromone;
    }

    public void setPoolSize(int poolSize){
        this.poolSize = poolSize;
    }

    //public void setEvaporationRate(ControlParameter evaporationRate){
    //    this.evaporationRate = evaporationRate;
    //}

    public void setInitialPheromone(double initialPheromone) {
        this.initialPheromone = initialPheromone;
    }
}
