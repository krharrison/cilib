/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.controlparameter.ConstantControlParameter;
import cilib.measurement.single.IterationBestFitness;
import cilib.problem.solution.Fitness;
import cilib.problem.solution.InferiorFitness;
import cilib.pso.PSO;
import cilib.pso.behaviour.StandardParticleBehaviour;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.velocityprovider.ClampingVelocityProvider;
import cilib.pso.velocityprovider.SelfAdaptiveVelocityProvider;
import cilib.pso.velocityprovider.StandardVelocityProvider;
import cilib.util.selection.recipes.ElitistSelector;
import cilib.util.selection.recipes.Selector;

/**
 * Particle Swarm Optimization with Adaptive Inertia Weight Factor
 */
public class PSOAIWFIterationStrategy extends AbstractIterationStrategy<PSO> {
    protected IterationStrategy<PSO> delegate;
    protected SelfAdaptiveVelocityProvider velocityProvider;

    protected double inertiaMin;
    protected double inertiaMax;

    public PSOAIWFIterationStrategy(){
        delegate = new SynchronousIterationStrategy();
        velocityProvider = new SelfAdaptiveVelocityProvider();

        inertiaMin = 0.4;
        inertiaMax = 0.9;
    }

    @Override
    public PSOAIWFIterationStrategy getClone() {
        return null;
    }

    @Override
    public void performIteration(PSO algorithm) {
        //ensure each entity has their own behaviour/velocity provider
        if(algorithm.getIterations() == 0){
            for(Particle p : algorithm.getTopology()){
                StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour().getClone();
                behaviour.setVelocityProvider(velocityProvider.getClone());
                p.setBehaviour(behaviour);
            }
        }

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
            if(!particleFitness.getValue().isInfinite() && ! particleFitness.getValue().isNaN()){
                sum += particleFitness.getValue();
            }
        }

        double avgFitness = sum / algorithm.getTopology().length();

        for(Particle p : algorithm.getTopology()){
            updateInertia(p, bestFitness.getValue(), avgFitness);
        }

        delegate.performIteration(algorithm);

    }

    private void updateInertia(Particle p, double minFitness, double avgFitness){

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

        StandardParticleBehaviour behaviour = (StandardParticleBehaviour) p.getBehaviour();
        SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider) behaviour.getVelocityProvider();
        provider.setInertiaWeight(ConstantControlParameter.of(inertia));
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
