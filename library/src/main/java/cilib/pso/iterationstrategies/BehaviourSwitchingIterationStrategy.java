/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.iterationstrategies;

import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.entity.topologies.GBestNeighbourhood;
import cilib.entity.topologies.Neighbourhood;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.behaviour.StandardParticleBehaviour;

public class BehaviourSwitchingIterationStrategy extends AbstractIterationStrategy<PSO> {
    
    private IterationStrategy<PSO> delegate = new SynchronousIterationStrategy();
    private IterationStrategy<PSO> newStrategy = new SynchronousIterationStrategy();
    private ControlParameter switchingIteration = ConstantControlParameter.of(250);
    private StandardParticleBehaviour behaviour;
    private Neighbourhood<Particle> neighbourhood = new GBestNeighbourhood<Particle>();

    @Override
    public AbstractIterationStrategy getClone() {
        return this;
    }

    @Override
    public void performIteration(PSO algorithm) {
        if (algorithm.getIterations() == ((int) switchingIteration.getParameter())) {
            for (Particle p : algorithm.getTopology()) {
                p.setBehaviour(behaviour);
            }
            algorithm.setNeighbourhood(neighbourhood);
            delegate = newStrategy;
        }
        
        delegate.performIteration(algorithm);
    }

    public void setSwitchingIteration(ControlParameter switchingIteration) {
        this.switchingIteration = switchingIteration;
    }

    public void setDelegate(IterationStrategy<PSO> delegate) {
        this.delegate = delegate;
    }

    public void setBehaviour(StandardParticleBehaviour behaviour) {
        this.behaviour = behaviour;
    }
    
}
