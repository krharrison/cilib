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
import cilib.entity.Property;
import cilib.pso.PSO;
import cilib.pso.behaviour.StandardParticleBehaviour;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.velocityprovider.SelfAdaptiveVelocityProvider;
import cilib.pso.velocityprovider.StandardVelocityProvider;

public class AIWPSOIterationStrategy extends AbstractIterationStrategy<PSO> {

    protected double inertiaMin;
    protected double inertiaMax;
    protected IterationStrategy<PSO> delegate;

    public AIWPSOIterationStrategy(){
        delegate = new SynchronousIterationStrategy();
        inertiaMin = 0;
        inertiaMax = 1;
    }

    @Override
    public AbstractIterationStrategy<PSO> getClone() {
        return null;
    }

    @Override
    public void performIteration(PSO algorithm) {
        delegate.performIteration(algorithm);

        double successRate = calculateSuccesses(algorithm);

        double inertia = successRate * (inertiaMax - inertiaMin) + inertiaMin;

        for(Particle p : algorithm.getTopology()){
            ((SelfAdaptiveParticle) p).setInertiaWeight(ConstantControlParameter.of(inertia));
        }
    }

    /**
     * Calculate the proportion of particles which improved their personal best.
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
}
