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
import cilib.math.random.generator.Rand;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.adaptationstrategies.AlgorithmAdaptationStrategy;
import fj.F2;

/**
 * Adaptive Inertia Weight Factor PSO
 *
 * A. Nickabadi, M. M. Ebadzadeh, and R. Safabakhsh, “A Novel Particle Swarm Optimization Algorithm with
 * Adaptive Inertia Weight,” Applied Soft Computing, vol. 11, no. 4, pp. 3658–3670, 2011.
 */
public class AIWPSOInertiaStrategy implements AlgorithmAdaptationStrategy{

    protected double minValue;
    protected double maxValue;

    public AIWPSOInertiaStrategy(){
        minValue = 0.0;
        maxValue = 1.0;
    }

    @Override
    public void adapt(PSO algorithm) {

        if(algorithm.getIterations() == 0 ) return; //no adaptation during first iteration

        double successRate = calculateSuccesses(algorithm);
        double inertia = minValue + successRate * (maxValue - minValue);

        for (Particle p : algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
        }
    }

    @Override
    public AIWPSOInertiaStrategy getClone() {
        return this;
    }

    /**
     * Calculate the proportion of particles which improved their personal best during the last iteration.
     * @param algorithm
     * @return
     */
    private double calculateSuccesses(PSO algorithm){
        int count = 0;

        //A success is recorded if it has been 0 iterations since the personal best was updated.
        for(Particle p : algorithm.getTopology()){
            if(p.get(Property.PBEST_STAGNATION_COUNTER).intValue() == 0){
                count++;
            }
        }

        return (double)count / algorithm.getTopology().length();
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

}
