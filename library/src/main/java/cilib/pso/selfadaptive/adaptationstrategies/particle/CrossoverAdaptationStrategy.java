package cilib.pso.selfadaptive.adaptationstrategies.particle;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.math.random.generator.Rand;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.pso.selfadaptive.crossover.ParentCentricCrossover;
import cilib.pso.selfadaptive.crossover.VectorCrossover;
import cilib.type.types.container.Vector;
import cilib.util.selection.recipes.RandomSelector;
import cilib.util.selection.recipes.Selector;
import cilib.util.selection.recipes.TournamentSelector;
import fj.F;

import java.util.ArrayList;
import java.util.List;

public class CrossoverAdaptationStrategy implements ParticleAdaptationStrategy {

    protected VectorCrossover crossover;
    protected ControlParameter randomProbability;
    protected Selector<ParameterSet> particleSelector;
    protected RandomSelector<ParameterSet> randomSelector;

    static F<Particle, ParameterSet> particleMapper =
            new F<Particle, ParameterSet>(){

                @Override
                public ParameterSet f(Particle particle) {
                    return ((SelfAdaptiveParticle) particle).getParameterSet();
                }
            };

    public CrossoverAdaptationStrategy(){
        crossover = new ParentCentricCrossover();
        randomProbability = ConstantControlParameter.of(0.01);
        particleSelector = new TournamentSelector(); //default is 10% of popSize
        randomSelector = new RandomSelector();
    }

    @Override
    public void adapt(Particle particle, PSO algorithm) {

        SelfAdaptiveParticle sp = (SelfAdaptiveParticle) particle;
        int n = crossover.getNumberOfParents();
        List<Vector> parents = new ArrayList<>();
        parents.add(sp.getParameterSet().asVector());

        if(Rand.nextDouble() < randomProbability.getParameter()){
            ParameterSet randomParent = randomSelector.on(algorithm.getTopology().map(particleMapper)).select();
            //SelfAdaptiveParticle randomParent = (SelfAdaptiveParticle) randomSelector.on(algorithm.getTopology()).select();
            parents.add(randomParent.asVector());
        }

        int remaining = n - parents.size();

        for(int i = 0; i < remaining; i++){
            //SelfAdaptiveParticle parent = (SelfAdaptiveParticle) particleSelector.on(algorithm.getTopology()).select();
            ParameterSet parent = particleSelector.on(algorithm.getTopology().map(particleMapper)).select();
            parents.add(parent.asVector());
        }

        //create new parameter set from the first offspring and ensuring it is convergent
        ParameterSet params;
        do {
            List<Vector> offspring = crossover.crossover(parents);
            params = ParameterSet.createFromVector(offspring.get(0));
        } while(!params.isConvergent());

        sp.setParameterSet(params);
    }


    @Override
    public ParticleAdaptationStrategy getClone() {
        return null;
    }
}
