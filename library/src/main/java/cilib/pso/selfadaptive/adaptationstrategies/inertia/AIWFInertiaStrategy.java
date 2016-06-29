/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies.inertia;

import cilib.controlparameter.ConstantControlParameter;
import cilib.problem.solution.Fitness;
import cilib.problem.solution.InferiorFitness;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.adaptationstrategies.SwarmAdaptationStrategy;

/**
 * B. Liu, L. Wang, Y.-H. Jin, F. Tang, and D.-X. Huang, “Improved Particle Swarm Optimization Combined with Chaos,”
 * Chaos, Solitons & Fractals, vol. 25, no. 5, pp. 1261–1271, 2005.
 */
public class AIWFInertiaStrategy implements SwarmAdaptationStrategy {

    protected double inertiaMin;
    protected double inertiaMax;

    public AIWFInertiaStrategy(){
        super();
        inertiaMin = 0.2;
        inertiaMax = 1.2;
    }

    public AIWFInertiaStrategy(AIWFInertiaStrategy copy){
        this.inertiaMin = copy.inertiaMin;
        this.inertiaMax = copy.inertiaMax;
    }


    @Override
    public void adapt(PSO algorithm) {
        double sum = 0;
        Fitness bestFitness = InferiorFitness.instance();
        Fitness particleFitness;

        //Find the minimum and average fitness values
        for(Particle p : algorithm.getTopology()){
            particleFitness = p.getFitness();

            if(particleFitness.compareTo(bestFitness) > 0) {
                bestFitness = particleFitness;
            }
            //only add fitness values which are valid
            if(!particleFitness.getValue().isInfinite() && !particleFitness.getValue().isNaN()){
                sum += particleFitness.getValue();
            }
        }

        double avgFitness = sum / algorithm.getTopology().length();

        for(Particle p : algorithm.getTopology()){
            updateInertia((SelfAdaptiveParticle) p, bestFitness.getValue(), avgFitness);
        }
    }

    private void updateInertia(SelfAdaptiveParticle p, double minFitness, double avgFitness){

        double fitness = p.getFitness().getValue();
        double inertia;

        if(fitness <= avgFitness){
            double numerator = (inertiaMax - inertiaMin) * (fitness - minFitness);
            double denominator = (avgFitness - minFitness);

            inertia = inertiaMin + (numerator / denominator);
        }
        else{
            inertia = inertiaMax;
        }

        p.setInertiaWeight(ConstantControlParameter.of(inertia));
    }

    @Override
    public AIWFInertiaStrategy getClone() {
        return new AIWFInertiaStrategy(this);
    }

    public void setInertiaMin(double inertiaMin){
        this.inertiaMin = inertiaMin;
    }

    public double getInertiaMin(){
        return this.inertiaMin;
    }

    public void setInertiaMax(double inertiaMax){
        this.inertiaMax = inertiaMax;
    }

    public double getInertiaMax(){
        return this.inertiaMax;
    }
}
