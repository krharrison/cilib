/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.controlparameter.ConstantControlParameter;
import cilib.entity.Property;
import cilib.measurement.single.IterationBestFitness;
import cilib.pso.PSO;
import cilib.pso.behaviour.StandardParticleBehaviour;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.velocityprovider.ClampingVelocityProvider;
import cilib.pso.velocityprovider.SelfAdaptiveVelocityProvider;
import cilib.pso.velocityprovider.StandardVelocityProvider;
import cilib.util.selection.recipes.ElitistSelector;
import cilib.util.selection.recipes.Selector;

/**
 * Dynamic Adaptation Particle Swarm Optimization
 *
 */
public class DAPSOIterationStrategy extends AbstractIterationStrategy<PSO> {

    //protected Selector<Particle> elitistSelector;
    protected IterationStrategy<PSO> delegate;
    //protected SelfAdaptiveVelocityProvider velocityProvider;
    protected double initialInertia;
    protected double alpha;
    protected double beta;
    protected IterationBestFitness iterBestFitness;

    public DAPSOIterationStrategy(){
        super();
        delegate = new SynchronousIterationStrategy();
        //velocityProvider = new SelfAdaptiveVelocityProvider();
        //elitistSelector = new ElitistSelector<Particle>();
        iterBestFitness = new IterationBestFitness();
        initialInertia = 1.0;
        alpha = 1.0;
        beta = 0.1;
    }

    public DAPSOIterationStrategy(DAPSOIterationStrategy copy){
        super(copy);
        this.delegate = copy.delegate.getClone();
        //this.velocityProvider = copy.velocityProvider.getClone();
       // this.elitistSelector = copy.elitistSelector;
        this.iterBestFitness = copy.iterBestFitness.getClone();
    }

    @Override
    public DAPSOIterationStrategy getClone() {
        return new DAPSOIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {

        //ensure each entity has their own behaviour/velocity provider
      //  if(algorithm.getIterations() == 0){
      //      for(Particle p : algorithm.getTopology()) {
      //          StandardParticleBehaviour behaviour = (StandardParticleBehaviour) p.getBehaviour().getClone();
      //          behaviour.setVelocityProvider(velocityProvider.getClone());
      //          p.setBehaviour(behaviour);
      //      }
      //  }

        delegate.performIteration(algorithm);

        double aggDegree = aggregationDegree(algorithm);

        for(Particle p : algorithm.getTopology()){
            //StandardParticleBehaviour behaviour = (StandardParticleBehaviour) p.getBehaviour();
            //SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider) behaviour.getVelocityProvider();

            double evolSpeed = evolutionarySpeed(p);
            double inertia = initialInertia - alpha * (1 - evolSpeed) + beta * aggDegree;

            ((SelfAdaptiveParticle) p).setInertiaWeight(ConstantControlParameter.of(inertia));
        }
    }

    private double evolutionarySpeed(Particle p){
        double pBest = p.get(Property.BEST_FITNESS).getValue();
        //double prevFit = p.get(Property.PREVIOUS_FITNESS).getValue();
        double prevPBest = 1.0;         //TODO: how to get the previous personal best

        //TODO: this assumes minimization, is this problematic?
        //TODO: positive offset
        return Math.abs(Math.min(pBest, prevPBest) / Math.max(pBest, prevPBest));
    }


    //TODO: what if all particles have invalid fitness?
    private double aggregationDegree(PSO algorithm){
        double avg = 0;
        double iterationBest = iterBestFitness.getValue(algorithm).doubleValue(); //TODO: positive offset

        if(Double.isInfinite(iterationBest)) {
            return 1; //TODO: return?
        }
        else {
            for(Particle p : algorithm.getTopology()){
                double fitness = p.getFitness().getValue();  //TODO: positive offset
                if(!Double.isInfinite(fitness)){
                    avg += Math.abs(fitness - iterationBest);
                }
            }

            avg /= algorithm.getTopology().length();
        }

        return Math.abs(Math.min(iterationBest, avg) / Math.max(iterationBest, avg));
    }

  //  public void setVelocityProvider(SelfAdaptiveVelocityProvider velocityProvider){
  //      this.velocityProvider = velocityProvider;
  //  }

    public void setDelegate(IterationStrategy<PSO> delegate){
        this.delegate = delegate;
    }
}