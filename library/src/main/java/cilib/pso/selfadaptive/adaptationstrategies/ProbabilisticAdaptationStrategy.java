package cilib.pso.selfadaptive.adaptationstrategies;

import cilib.math.random.generator.Rand;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.pso.selfadaptive.crossover.ArithmeticCrossover;
import cilib.pso.selfadaptive.crossover.BlendCrossover;
import cilib.pso.selfadaptive.crossover.VectorCrossover;
import cilib.pso.selfadaptive.parametersetgenerator.ConvergentParameterSetGenerator;
import cilib.pso.selfadaptive.parametersetgenerator.ParameterSetGenerator;
import cilib.type.types.container.Vector;
import cilib.util.selection.arrangement.Arrangement;
import cilib.util.selection.arrangement.ReverseArrangement;
import cilib.util.selection.arrangement.SortedArrangement;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ProbabilisticAdaptationStrategy implements SwarmAdaptationStrategy {

    protected Arrangement<Particle> arrangement;
    protected Arrangement<Particle> reverser;
    protected double randomRate;
    protected ParameterSetGenerator generator;

    protected VectorCrossover crossover;

    public ProbabilisticAdaptationStrategy(){
        arrangement = new SortedArrangement<>();
        reverser = new ReverseArrangement<>();
        randomRate = 0.5;
        generator = new ConvergentParameterSetGenerator();
        crossover = new ArithmeticCrossover();
    }

    public ProbabilisticAdaptationStrategy(ProbabilisticAdaptationStrategy copy){
        arrangement = copy.arrangement;
        reverser = copy.reverser;
        randomRate = copy.randomRate;
        generator = copy.generator.getClone();
        crossover = copy.crossover;
    }

    @Override
    public void adapt(PSO algorithm) {
        Iterable<Particle> ordering = orderParticles(algorithm);
        Iterator<Particle> iterator = ordering.iterator(); //ordered from worst to best

        int particles = algorithm.getTopology().length();
        int rank = 0; //start with rank = 0

        if(!iterator.hasNext()) return;

        SelfAdaptiveParticle p;
        p = (SelfAdaptiveParticle) iterator.next();

        ParameterSet bestParameters = p.getParameterSet();

        do {
            p = (SelfAdaptiveParticle) iterator.next();
            double prob = (double)rank / (particles - 1); //probability of changing parameters

            if(Rand.nextDouble() < prob){ //find new parameters
                if(Rand.nextDouble() < randomRate){
                    p.setParameterSet(generator.generate());
                }
                else { //create new parameters from crossover
                    List<Vector> offspring = crossover.crossover(Arrays.asList(p.getParameterSet().asVector(), bestParameters.asVector()));
                    ParameterSet o1 = ParameterSet.createFromVector(offspring.get(0));
                    ParameterSet o2 = ParameterSet.createFromVector(offspring.get(1));

                    if(o1.isConvergent()) p.setParameterSet(o1);
                    else if(o2.isConvergent()) p.setParameterSet(o2);
                    else { } //no change???
                }
            }

            rank--;
        } while(iterator.hasNext());
    }

    @Override
    public ProbabilisticAdaptationStrategy getClone() {
        return new ProbabilisticAdaptationStrategy(this);
    }

    private Iterable<Particle> orderParticles(PSO algorithm){
        return reverser.arrange(arrangement.arrange(algorithm.getTopology()));
    }
}
