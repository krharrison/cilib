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
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.entity.Property;
import cilib.moo.archive.Archive;
import cilib.problem.solution.Fitness;
import cilib.problem.solution.InferiorFitness;
import cilib.problem.solution.OptimisationSolution;
import cilib.pso.PSO;
import cilib.pso.crossover.pbestupdate.CurrentPositionOffspringPBestProvider;
import cilib.pso.crossover.velocityprovider.ZeroOffspringVelocityProvider;
import cilib.pso.particle.Particle;
import cilib.pso.particle.StandardParticle;
import cilib.type.types.container.StructuredType;
import cilib.util.selection.Samples;
import cilib.util.selection.recipes.ElitistSelector;

/**
 * Perform crossover on archive solutions by creating a dummy particle for each
 * archive solution selected.
 */
public class ArchiveRepeatingCrossoverSelection extends CrossoverSelection {

    private ControlParameter retries;

    public ArchiveRepeatingCrossoverSelection() {
        super();
        crossoverStrategy.setPbestProvider(new CurrentPositionOffspringPBestProvider());
        crossoverStrategy.setVelocityProvider(new ZeroOffspringVelocityProvider());
        this.retries = ConstantControlParameter.of(10);
    }

    public ArchiveRepeatingCrossoverSelection(ArchiveRepeatingCrossoverSelection copy) {
        super(copy);
        this.retries = copy.retries;
    }

    @Override
    public P3<Boolean, Particle, Particle> select(PSO algorithm, Property solutionType, Property fitnessType) {
        boolean isBetter = false;

        List<Particle> parents = new ArrayList<>();
        List<OptimisationSolution> solutions = new ArrayList<>();
        Archive archive = Archive.Provider.get();

        //select 3 non-dominated solutions and create dummy particles to perform crossover
        //if less than 3, select the remaining as random best particles
        if (archive.size() < crossoverStrategy.getNumberOfParents()) {
            parents = new ElitistSelector().on(algorithm.getTopology()).select(Samples.first(crossoverStrategy.getNumberOfParents() - archive.size()).unique());
            if (!archive.isEmpty()) {
                solutions = selector.on(archive).select(Samples.all()); //should give all archive solutions
            }
        } else {
            solutions = selector.on(archive).select(Samples.first(crossoverStrategy.getNumberOfParents()).unique());
        }

        //create particle from each solution
        for (OptimisationSolution sol : solutions) {
            Particle p = new StandardParticle();
            p.put(Property.CANDIDATE_SOLUTION, (StructuredType) sol.getPosition());
            p.put(Property.BEST_FITNESS, InferiorFitness.instance());
            p.put(Property.BEST_POSITION, (StructuredType) sol.getPosition());
            p.updateFitness(p.getBehaviour().getFitnessCalculator().getFitness(p));
            p.put(Property.BEST_FITNESS, p.getFitness());
            p.put(Property.PREVIOUS_FITNESS, p.getFitness());
            parents.add(p);
        }

        Map<Particle, StructuredType> tmp = Maps.newHashMap();

        //put pbest as candidate solution for the crossover
        for (Particle e : parents) {
            tmp.put(e, e.getPosition());
            e.put(Property.CANDIDATE_SOLUTION, e.<StructuredType>get(solutionType));
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
    public ArchiveRepeatingCrossoverSelection getClone() {
        return new ArchiveRepeatingCrossoverSelection(this);
    }
}
