/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.controlparameter.ConstantControlParameter;
import cilib.entity.Property;
import cilib.measurement.single.IterationBestFitness;
import cilib.pso.PSO;
import cilib.pso.behaviour.StandardParticleBehaviour;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.velocityprovider.ClampingVelocityProvider;
import cilib.pso.velocityprovider.SelfAdaptiveVelocityProvider;
import cilib.pso.velocityprovider.StandardVelocityProvider;
import cilib.util.selection.recipes.ElitistSelector;
import cilib.util.selection.recipes.Selector;

/**
 * Dynamic Adaptation Particle Swarm Optimization
 *
 */
public class DAPSOIterationStrategy extends AbstractIterationStrategy<PSO> {

    protected IterationStrategy<PSO> delegate;
    protected double initialInertia;
    protected double alpha;
    protected double beta;
    protected IterationBestFitness iterBestFitness;
    protected double[] previousPBest;

    public DAPSOIterationStrategy(){
        super();
        delegate = new SynchronousIterationStrategy();
        iterBestFitness = new IterationBestFitness();
        initialInertia = 1.0;
        alpha = 1.0;
        beta = 0.1;
    }

    public DAPSOIterationStrategy(DAPSOIterationStrategy copy){
        super(copy);
        this.delegate = copy.delegate.getClone();
        this.iterBestFitness = copy.iterBestFitness.getClone();
    }

    @Override
    public DAPSOIterationStrategy getClone() {
        return new DAPSOIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {

        if(algorithm.getIterations() == 0){
            previousPBest = new double[algorithm.getTopology().length()];
        }

        //store the personal best of each particle
        for(int i = 0; i < algorithm.getTopology().length(); i++){
            previousPBest[i] = algorithm.getTopology().index(i).getBestFitness().getValue();
        }

        double aggDegree = aggregationDegree(algorithm);

        for(int i = 0; i < algorithm.getTopology().length(); i++){

            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) algorithm.getTopology().index(i);

            double evolSpeed = evolutionarySpeed(sp, previousPBest[i]);
            double inertia = initialInertia - alpha * (1 - evolSpeed) + beta * aggDegree;

            sp.put(Property.PREVIOUS_PARAMETERS, sp.getParameterSet().asVector());
            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
        }

        delegate.performIteration(algorithm);
    }

    private double evolutionarySpeed(Particle sp, double prevPBest){
        double pBest = sp.get(Property.BEST_FITNESS).getValue();

        //TODO: this assumes minimization, is this problematic?
        //TODO: positive offset
        return Math.abs(Math.min(pBest, prevPBest) / Math.max(pBest, prevPBest));
    }

    //TODO: what if all particles have invalid fitness?
    private double aggregationDegree(PSO algorithm){
        double avg = 0;
        double iterationBest = iterBestFitness.getValue(algorithm).doubleValue(); //TODO: positive offset

        if(Double.isInfinite(iterationBest)) {
            return 1; //TODO: return?
        }
        else {
            for(Particle p : algorithm.getTopology()){
                double fitness = p.getFitness().getValue();  //TODO: positive offset
                if(Double.isInfinite(fitness) || Double.isNaN(fitness)) continue;
                else avg += Math.abs(fitness - iterationBest);

            }

            avg /= algorithm.getTopology().length();
        }

        return Math.abs(Math.min(iterationBest, avg) / Math.max(iterationBest, avg));
    }

    public void setDelegate(IterationStrategy<PSO> delegate){
        this.delegate = delegate;
    }
}