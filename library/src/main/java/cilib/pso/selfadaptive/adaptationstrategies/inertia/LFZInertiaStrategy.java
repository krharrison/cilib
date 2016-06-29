/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies.inertia;

import cilib.controlparameter.ConstantControlParameter;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.adaptationstrategies.SwarmAdaptationStrategy;

/**
 *X. Li, H. Fu, and C. Zhang, “A Self-Adaptive Particle Swarm Optimization Algorithm,” in Proceedings of the
 * 2008 International Conference on Computer Science and Software Engineering, 2008, vol. 5, pp. 186–189.
 */
public class LFZInertiaStrategy implements SwarmAdaptationStrategy {

    protected double alpha;

    public LFZInertiaStrategy(){
        alpha = 0.15;
    }

    public LFZInertiaStrategy(LFZInertiaStrategy copy){
    this.alpha = copy.alpha;
    }


    @Override
    public void adapt(PSO algorithm) {
        double sum = 0;
        //Find the average personal best fitness TODO: fold?
        for(Particle p : algorithm.getTopology()){
            sum += p.getBestFitness().getValue();
        }

        double avgPBestFitness = sum / algorithm.getTopology().length();

        for(Particle p : algorithm.getTopology()){
            double inertia = alpha + (1 / (1 + Math.exp(avgPBestFitness - p.getBestFitness().getValue())));
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
        }

    }

    @Override
    public LFZInertiaStrategy getClone() {
        return new LFZInertiaStrategy(this);
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

}
