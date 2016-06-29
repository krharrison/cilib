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
 * Improved PSO by Li and Tan
 *
 * Z. Li and G. Tan, “A Self-Adaptive Mutation-Particle Swarm Optimization Algorithm,”
 * in Fourth International Conference on Natural Computation, 2008, vol. 1, pp. 30–34.
 */

public class IPSOLTInertiaStrategy implements SwarmAdaptationStrategy {

    protected double[] previousPBest;
    protected double alpha;
    protected double beta;

    public IPSOLTInertiaStrategy(){
        alpha = 0.5;
        beta = 0.5;
    }

    @Override
    public void adapt(PSO algorithm) {
        if(algorithm.getIterations() == 0){
            previousPBest = new double[algorithm.getTopology().length()];
            retainPersonalBests(algorithm);
            return;
        }

        double gBest = algorithm.getBestSolution().getFitness().getValue();

        for(int i = 0; i < algorithm.getTopology().length(); i++){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) algorithm.getTopology().index(i);

            double c = convergenceFactor(sp, previousPBest[i]);
            double d = diffusionFactor(sp, gBest);
            double inertia = 1 - Math.abs((alpha * (1 - c)) / ((1 + d) * (1 + beta)));

            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
        }

        retainPersonalBests(algorithm);
    }

    private void retainPersonalBests(PSO algorithm){
        for(int i = 0; i < algorithm.getTopology().length(); i++){
            previousPBest[i] = algorithm.getTopology().index(i).getBestFitness().getValue();
        }
    }

    private double convergenceFactor(Particle sp, double prevPBest){
        double pBest = sp.getBestFitness().getValue();
        return Math.abs(prevPBest - pBest) / (prevPBest + pBest);
    }

    private double diffusionFactor(Particle sp, double gBest){
        double pBest = sp.getBestFitness().getValue();
        return Math.abs(pBest - gBest) / (pBest + gBest);
    }

    @Override
    public IPSOLTInertiaStrategy getClone() {
        return this;
    }
}
