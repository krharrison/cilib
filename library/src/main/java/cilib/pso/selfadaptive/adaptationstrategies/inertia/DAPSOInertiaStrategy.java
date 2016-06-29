/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies.inertia;

import cilib.controlparameter.ConstantControlParameter;
import cilib.entity.Property;
import cilib.measurement.single.IterationBestFitness;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.adaptationstrategies.SwarmAdaptationStrategy;

/**
 * X. Yang, J. Yuan, J. Yuan, and H. Mao, “A Modified Particle Swarm Optimizer with Dynamic Adaptation,”
 * Applied Mathematics and Computation, vol. 189, no. 2, pp. 1205–1213, 2007.
 */
public class DAPSOInertiaStrategy implements SwarmAdaptationStrategy {

    protected double initialInertia;
    protected double alpha;
    protected double beta;
    protected IterationBestFitness iterBestFitness;
    protected double[] previousPBest;

    public DAPSOInertiaStrategy(){
        iterBestFitness = new IterationBestFitness();
        initialInertia = 1.0;
        alpha = 1.0;
        beta = 0.1;
        //previousPBest intentionally not initialized
    }

    public DAPSOInertiaStrategy(DAPSOInertiaStrategy copy){
        this.iterBestFitness = copy.iterBestFitness.getClone();
        this.initialInertia = copy.initialInertia;
        this.alpha = copy.alpha;
        this.beta = copy.beta;
        this.previousPBest = copy.previousPBest.clone();
    }


    @Override
    public void adapt(PSO algorithm) {
        //store the personal best of each particle for the first iteration
        if(algorithm.getIterations() == 0){
            previousPBest = new double[algorithm.getTopology().length()];
            for(int i = 0; i < algorithm.getTopology().length(); i++){
                previousPBest[i] = algorithm.getTopology().index(i).getBestFitness().getValue();
            }
        }

        double aggDegree = aggregationDegree(algorithm);

        for(int i = 0; i < algorithm.getTopology().length(); i++){

            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) algorithm.getTopology().index(i);

            double evolSpeed = evolutionarySpeed(sp, previousPBest[i]);
            double inertia = initialInertia - alpha * (1 - evolSpeed) + beta * aggDegree;

            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
        }

        //store the personal best of each particle
        for(int i = 0; i < algorithm.getTopology().length(); i++){
            previousPBest[i] = algorithm.getTopology().index(i).getBestFitness().getValue();
        }

    }

    private double evolutionarySpeed(Particle sp, double prevPBest){
        double pBest = sp.get(Property.BEST_FITNESS).getValue();

        //TODO: this assumes minimization, is this problematic?
        //TODO: offset
        return Math.abs(Math.min(pBest, prevPBest) / Math.max(pBest, prevPBest));
    }

    //TODO: what if all particles have invalid fitness?
    private double aggregationDegree(PSO algorithm){
        double avg = 0;
        double iterationBest = iterBestFitness.getValue(algorithm).doubleValue(); //TODO: offset

        if(Double.isInfinite(iterationBest)) {
            return 1; //TODO: what to return?
        }
        else {
            for(Particle p : algorithm.getTopology()){
                double fitness = p.getFitness().getValue();  //TODO: offset
                if(Double.isInfinite(fitness) || Double.isNaN(fitness)) continue;
                else avg += Math.abs(fitness - iterationBest);
            }

            avg /= algorithm.getTopology().length();
        }

        return Math.abs(Math.min(iterationBest, avg) / Math.max(iterationBest, avg));
    }


    @Override
    public DAPSOInertiaStrategy getClone() {
        return new DAPSOInertiaStrategy(this);
    }

}
