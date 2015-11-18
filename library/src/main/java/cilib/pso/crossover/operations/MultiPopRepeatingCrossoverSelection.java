/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.crossover.operations;

import com.google.common.collect.Maps;
import fj.P;
import fj.P3;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import cilib.algorithm.AbstractAlgorithm;
import cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.entity.Property;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.UniformDistribution;
import cilib.problem.solution.Fitness;
import cilib.pso.PSO;
import cilib.pso.crossover.pbestupdate.CurrentPositionOffspringPBestProvider;
import cilib.pso.crossover.velocityprovider.ZeroOffspringVelocityProvider;
import cilib.pso.particle.Particle;
import cilib.type.types.container.StructuredType;
import cilib.util.selection.Samples;

/**
 * Perform crossover on archive solutions by creating a dummy particle for each
 * archive solution selected.
 */
public class MultiPopRepeatingCrossoverSelection extends CrossoverSelection {

    private ProbabilityDistributionFunction random = new UniformDistribution();
    private ControlParameter retries;

    public MultiPopRepeatingCrossoverSelection() {
        super();
        crossoverStrategy.setPbestProvider(new CurrentPositionOffspringPBestProvider());
        crossoverStrategy.setVelocityProvider(new ZeroOffspringVelocityProvider());
        this.retries = ConstantControlParameter.of(10);
    }

    public MultiPopRepeatingCrossoverSelection(MultiPopRepeatingCrossoverSelection copy) {
        super(copy);
        this.retries = copy.retries;
    }

    @Override
    public P3<Boolean, Particle, Particle> select(PSO algorithm, Property solutionType, Property fitnessType) {
        boolean isBetter = false;

        List<Particle> parents = new ArrayList<>();

        MultiPopulationBasedAlgorithm algs = (MultiPopulationBasedAlgorithm) AbstractAlgorithm.getAlgorithmList().index(0);
        List<SinglePopulationBasedAlgorithm> pops = algs.getPopulations();

        if (pops.size() > 2) {
            pops = selector.on(pops).select(Samples.first(crossoverStrategy.getNumberOfParents()).unique());
        } else {
            pops = selector.on(pops).select(Samples.all());
            parents.add((Particle) selector.on(pops.get((int) random.getRandomNumber(0, pops.size())).getTopology()).select());
        }

        for (SinglePopulationBasedAlgorithm a : pops) {
            parents.add((Particle) selector.on(a.getTopology()).select());
        }

        Map<Particle, StructuredType> tmp = Maps.newHashMap();

        //put pbest as candidate solution for the crossover
        for (Particle e : parents) {
            tmp.put(e, e.getPosition());
            e.put(Property.CANDIDATE_SOLUTION, e.getNeighbourhoodBest().getBestPosition());
        }

        //perform crossover and select particle to compare with
        Particle offspring = (Particle) crossoverStrategy.crossover(parents).get(0);
        Particle selectedParticle = particleProvider.f(fj.data.List.iterableList(parents), offspring);

        //replace selectedEntity if offspring is better
        if (((Fitness) offspring.get(fitnessType))
                .compareTo((Fitness) selectedParticle.get(fitnessType)) > 0) {
            isBetter = true;
        }

        // revert solutions
        for (Particle e : parents) {
            e.setPosition(tmp.get(e));
        }

        if (isBetter && offspring.getNeighbourhoodBest() == null) {
            offspring.setNeighbourhoodBest(offspring.getClone());
        }
        return P.p(isBetter, selectedParticle, offspring);
    }

    @Override
    public P3<Boolean, Particle, Particle> doAction(PSO algorithm, Property solutionType, Property fitnessType) {
        int counter = 0;
        boolean isBetter;
        P3<Boolean, Particle, Particle> result;

        do {
            result = select(algorithm, solutionType, fitnessType);
            isBetter = result._1();
        } while (++counter < retries.getParameter() && !isBetter);

        return result;
    }

    public void setRetries(ControlParameter retries) {
        this.retries = retries;
    }

    public ControlParameter getRetries() {
        return retries;
    }

    @Override
    public MultiPopRepeatingCrossoverSelection getClone() {
        return new MultiPopRepeatingCrossoverSelection(this);
    }
}
