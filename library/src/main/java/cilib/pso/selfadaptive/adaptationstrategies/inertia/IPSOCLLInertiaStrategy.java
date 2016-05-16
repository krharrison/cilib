/**
 * __  __
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
import cilib.pso.selfadaptive.adaptationstrategies.AlgorithmAdaptationStrategy;
import cilib.util.selection.recipes.ElitistSelector;

/**
 * Improved PSO by Chen et al.
 *
 * H. Chen, G. Li, and H. Liao, “A Self-Adaptive Improved Particle Swarm Optimization Algorithm and Its
 * Application in Available Transfer Capability Calculation,” in Natural Computation, 2009.
 * ICNC ’09. Fifth International Conference on, 2009, vol. 3, pp. 200–205.
 */
public class IPSOCLLInertiaStrategy implements AlgorithmAdaptationStrategy{

    protected double previousAlpha;
    protected IterationBestFitness iterBestFitness;

    public IPSOCLLInertiaStrategy(){
        iterBestFitness = new IterationBestFitness();
    }

    @Override
    public void adapt(PSO algorithm) {

        if(algorithm.getIterations() == 0){
            previousAlpha = calculateAlpha(algorithm);
            return;
        }

        double alpha = calculateAlpha(algorithm);
        double lambda = alpha / previousAlpha;
        double inertia = Math.exp(-lambda);

        for (Particle p : algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
        }

        previousAlpha = alpha;
    }

    @Override
    public IPSOCLLInertiaStrategy getClone() {
        return this;
    }

    private double calculateAlpha(PSO algorithm){
        double sum = 0;
        int count = 0;

        double iterationBest = iterBestFitness.getValue(algorithm).doubleValue();

        if(Double.isInfinite(iterationBest) || Double.isNaN(iterationBest)) {
            return Double.MAX_VALUE; //TODO: is there a better return value here?
        }
        else {
            for(Particle p : algorithm.getTopology()){
                double fitness = p.getFitness().getValue();
                //TODO: NaN fitness from particle's leaving the search space...
                if(!Double.isInfinite(fitness) && !Double.isNaN(fitness)){
                    sum += Math.abs(fitness - iterationBest);
                    count++;
                }
            }

            return count == 0 ? Double.MAX_VALUE : sum / count;
        }
    }
}
