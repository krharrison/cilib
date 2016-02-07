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
import cilib.pso.velocityprovider.StandardVelocityProvider;
import cilib.util.selection.recipes.ElitistSelector;
import cilib.util.selection.recipes.Selector;

/**
 * PSO-SAIC
 *
 */
public class IPSOCLLIterationStrategy extends AbstractIterationStrategy<PSO> {

    protected Selector<Particle> elitistSelector;
    protected IterationStrategy<PSO> delegate;
    protected double previousAlpha;
    protected IterationBestFitness iterBestFitness;

    public IPSOCLLIterationStrategy(){
        super();
        delegate = new SynchronousIterationStrategy();
        elitistSelector = new ElitistSelector<Particle>();
        iterBestFitness = new IterationBestFitness();
    }

    public IPSOCLLIterationStrategy(IPSOCLLIterationStrategy copy){
        super(copy);
        this.delegate = copy.delegate.getClone();
        this.elitistSelector = copy.elitistSelector;
        this.iterBestFitness = copy.iterBestFitness.getClone();
    }

    @Override
    public IPSOCLLIterationStrategy getClone() {
        return new IPSOCLLIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {

        if(algorithm.getIterations() == 0){
            previousAlpha = calculateAlpha(algorithm);
        }

        delegate.performIteration(algorithm);

        double alpha = calculateAlpha(algorithm);
        double lambda = previousAlpha == 0 ? 1 : alpha / previousAlpha;
        double inertia = Math.exp(-lambda);

        for(Particle p : algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            p.put(Property.PREVIOUS_PARAMETERS, sp.getParameterSet().asVector());
            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
        }

        previousAlpha = alpha;
    }

    private double calculateAlpha(PSO algorithm){
        double sum = 0;
        int count = 0;

        double iterationBest = iterBestFitness.getValue(algorithm).doubleValue();

        if(Double.isInfinite(iterationBest)) {
            return Double.MAX_VALUE; //TODO: return 0?
        }
        else {
            for(Particle p : algorithm.getTopology()){
                double fitness = p.getFitness().getValue();
                if(!Double.isInfinite(fitness)){
                    sum += Math.abs(fitness - iterationBest);
                    count++;
                }
            }

            return count == 0 ? Double.MAX_VALUE : sum / count;
        }
    }

    public void setDelegate(IterationStrategy<PSO> delegate){
        this.delegate = delegate;
    }
}