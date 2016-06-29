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
import fj.data.List;

/**
 * Inertia weight strategy from Self-Regulating PSO
 *
 * M. R. Tanweer, S. Suresh, and N. Sundararajan, “Self Regulating Particle Swarm Optimization Algorithm,”
 * Information Sciences, vol. 294, pp. 182–202, 2015.
 */

public class SelfRegulatingInertiaStrategy implements SwarmAdaptationStrategy {

    protected double eta;
    protected double maxIters;
    protected double initialInertia;
    protected double finalInertia;

    public SelfRegulatingInertiaStrategy(){
        initialInertia = 0.9;
        finalInertia = 0.4;
        eta = 1;
        maxIters = 5000;
    }

    public SelfRegulatingInertiaStrategy(SelfRegulatingInertiaStrategy copy){
        this.eta = copy.eta;
        this.maxIters = copy.maxIters;
        this.initialInertia = copy.initialInertia;
        this.finalInertia = copy.finalInertia;
    }

    @Override
    public void adapt(PSO algorithm) {

        double deltaInertia = (initialInertia - finalInertia) / maxIters;

        int bestIndex = bestIndex(algorithm);
        double inertia;

        List<Particle> particles = algorithm.getTopology();
        for (int i = 0; i < algorithm.getTopology().length(); i++) {
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) particles.index(i);

            if (i == bestIndex) {
                inertia = sp.getInertiaWeight().getParameter() + eta * deltaInertia;
                sp.setInertiaWeight(ConstantControlParameter.of(inertia));
            } else {
                inertia = sp.getInertiaWeight().getParameter() - deltaInertia;
                sp.setInertiaWeight(ConstantControlParameter.of(inertia));
            }
        }
    }

    private int bestIndex(PSO algorithm) {
        Fitness bestFitness = InferiorFitness.instance();
        int bestIndex = 0;
        List<Particle> particles = algorithm.getTopology();
        for (int i = 0; i < algorithm.getTopology().length(); i++){
            if (particles.index(i).getFitness().compareTo(bestFitness) > 0) {
                bestFitness = particles.index(i).getFitness();
                bestIndex = i;
            }
        }
        return bestIndex;
    }

    @Override
    public SelfRegulatingInertiaStrategy getClone() {
        return new SelfRegulatingInertiaStrategy(this);
    }

    public double getFinalInertia() {
        return finalInertia;
    }

    public void setFinalInertia(double finalInertia) {
        this.finalInertia = finalInertia;
    }

    public double getMaxIterations() {
        return maxIters;
    }

    public void setMaxIterations(double maxIters) {
        this.maxIters = maxIters;
    }

    public double getInitialInertia() {
        return initialInertia;
    }

    public void setInitialInertia(double initialInertia) {
        this.initialInertia = initialInertia;
    }

    public double getEta() {
        return eta;
    }

    public void setEta(double eta) {
        this.eta = eta;
    }

}
