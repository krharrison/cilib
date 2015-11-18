/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.crossover.operations;

import fj.F;
import java.util.Arrays;
import java.util.List;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.entity.operators.crossover.real.MultiParentCrossoverStrategy;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.UniformDistribution;
import cilib.pso.PSO;
import cilib.pso.crossover.ParticleCrossoverStrategy;
import cilib.pso.crossover.parentupdate.ElitistParentReplacementStrategy;
import cilib.pso.crossover.parentupdate.ParentReplacementStrategy;
import cilib.pso.particle.Particle;
import cilib.util.selection.Samples;
import cilib.util.selection.recipes.RandomSelector;
import cilib.util.selection.recipes.Selector;

/**
 * The crossover portion of the Novel Multi-Parent Crossover PSO.
 * <p>
 * H Wang, Z Wu, Y Liu and S. Zeng, "Particle Swarm Optimization with a Novel
 * Multi-Parent Crossover operator", Fourth International Conference on Natural
 * Computation, pp 664--668, 2008, doi:10.1109/ICNC.2008.643
 * </p>
 */
public class MultiParentCrossoverOperation extends PSOCrossoverOperation {

    private ParentReplacementStrategy parentReplacementStrategy;
    private ParticleCrossoverStrategy crossover;
    private ControlParameter crossoverProbability;
    private ProbabilityDistributionFunction random;
    private Selector<Particle> selector;

    public MultiParentCrossoverOperation() {
        this.crossover = new ParticleCrossoverStrategy();
        this.crossover.setCrossoverStrategy(new MultiParentCrossoverStrategy());

        this.selector = new RandomSelector();
        this.crossoverProbability = ConstantControlParameter.of(0.8);
        this.random = new UniformDistribution();
        this.parentReplacementStrategy = new ElitistParentReplacementStrategy();
    }

    public MultiParentCrossoverOperation(MultiParentCrossoverOperation copy) {
        this.crossover = copy.crossover.getClone();
        this.random = copy.random;
        this.crossoverProbability = copy.crossoverProbability.getClone();
        this.parentReplacementStrategy = copy.parentReplacementStrategy;
        this.selector = copy.selector;
    }

    @Override
    public MultiParentCrossoverOperation getClone() {
        return new MultiParentCrossoverOperation(this);
    }

    @Override
    public fj.data.List<Particle> f(PSO pso) {
        final fj.data.List<Particle> topology = pso.getTopology();

        return topology.map(new F<Particle, Particle>() {
			@Override
			public Particle f(Particle p) {
				if (random.getRandomNumber() < crossoverProbability.getParameter()) {
	                List<Particle> parents = selector.on(topology).select(Samples.first(crossover.getNumberOfParents() - 1));
	                parents.add(0, p);

	                Particle offspring = crossover.crossover(parents).get(0);
	                offspring.setNeighbourhoodBest(offspring);

	                return parentReplacementStrategy.f(Arrays.asList(p), Arrays.asList(offspring)).get(0);
	            } else {
	            	return p;
	            }
			}
        });
    }

    public ParticleCrossoverStrategy getCrossover() {
        return crossover;
    }

    public ControlParameter getCrossoverProbability() {
        return crossoverProbability;
    }

    public ParentReplacementStrategy getParentReplacementStrategy() {
        return parentReplacementStrategy;
    }

    public ProbabilityDistributionFunction getRandom() {
        return random;
    }

    public Selector<Particle> getSelector() {
        return selector;
    }

    public void setCrossover(ParticleCrossoverStrategy crossover) {
        this.crossover = crossover;
    }

    public void setCrossoverProbability(ControlParameter crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public void setParentReplacementStrategy(ParentReplacementStrategy parentReplacementStrategy) {
        this.parentReplacementStrategy = parentReplacementStrategy;
    }

    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }

    public void setSelector(Selector<Particle> selector) {
        this.selector = selector;
    }
}
