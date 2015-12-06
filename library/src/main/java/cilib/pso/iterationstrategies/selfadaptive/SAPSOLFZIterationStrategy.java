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
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;

/**
 * Self-Adaptive Particle Swarm Optimization by LFZ?
 */
public class SAPSOLFZIterationStrategy extends AbstractIterationStrategy<PSO> {
    protected IterationStrategy<PSO> delegate;

    protected double alpha;

    public SAPSOLFZIterationStrategy(){
        super();
        delegate = new SynchronousIterationStrategy();
        alpha = 0.15;
    }

    public SAPSOLFZIterationStrategy(SAPSOLFZIterationStrategy copy){
        super(copy);
        this.alpha = copy.alpha;
    }

    @Override
    public SAPSOLFZIterationStrategy getClone() {
        return new SAPSOLFZIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        double sum = 0;
        //Find the average personal best fitness TODO: fold?
        for(Particle p : algorithm.getTopology()){
            sum += p.getBestFitness().getValue();
        }

        double avgPBestFitness = sum / algorithm.getTopology().length();

        for(Particle p : algorithm.getTopology()){
            double inertia = alpha + (1 / (1 + Math.exp(avgPBestFitness - p.getBestFitness().getValue())));

            ((SelfAdaptiveParticle) p).setInertiaWeight(ConstantControlParameter.of(inertia));
        }

        delegate.performIteration(algorithm);
    }

    public void setAlpha(double alpha){
        this.alpha = alpha;
    }
}
