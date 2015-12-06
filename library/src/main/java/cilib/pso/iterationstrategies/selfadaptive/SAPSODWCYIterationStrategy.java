package cilib.pso.iterationstrategies.selfadaptive;

import java.util.Iterator;

import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.controlparameter.ConstantControlParameter;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.util.selection.arrangement.Arrangement;
import cilib.util.selection.arrangement.SortedArrangement;

public class SAPSODWCYIterationStrategy extends AbstractIterationStrategy<PSO>{
    protected IterationStrategy<PSO> delegate;
    protected Arrangement<Particle> arrangement;

    protected double alpha;
    protected double beta;
    protected double gamma;

    public SAPSODWCYIterationStrategy(){
        super();
        arrangement = new SortedArrangement<Particle>();
        delegate = new SynchronousIterationStrategy();
        alpha = 3;
        beta = 200;
        gamma = 8;
    }

    public SAPSODWCYIterationStrategy(SAPSODWCYIterationStrategy copy){
        super(copy);
        this.arrangement = copy.arrangement;
        this.delegate = copy.delegate.getClone();
        this.alpha = copy.alpha;
        this.beta = copy.beta;
        this.gamma = copy.gamma;
    }

    @Override
    public SAPSODWCYIterationStrategy getClone() {
        return new SAPSODWCYIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {

        Iterable<Particle> ordering = orderParticles(algorithm);
        Iterator<Particle> iterator = ordering.iterator();
        int rank = 1;
        int particles = algorithm.getTopology().length();
        int dimensions = algorithm.getOptimisationProblem().getDomain().getDimension();

        SelfAdaptiveParticle p;
        while(iterator.hasNext()){
            p = (SelfAdaptiveParticle) iterator.next();

            double expTerm = Math.exp(-particles / beta);
            double rankTerm = (dimensions * rank) / gamma;
            double inertia = 1 / (alpha - expTerm + (rankTerm * rankTerm));

            p.setInertiaWeight(ConstantControlParameter.of(inertia));

            rank++; //increment rank for next particle
        }

        delegate.performIteration(algorithm);
    }

    private Iterable<Particle> orderParticles(PSO algorithm){
        return arrangement.arrange(algorithm.getTopology());
    }

    public void setDelegate(IterationStrategy<PSO> delegate){
        this.delegate = delegate;
    }

    public void setAlpha(double alpha){
        this.alpha = alpha;
    }

    public void setBeta(double beta){
        this.beta = beta;
    }

    public void setGamma(double gamma){
        this.gamma = gamma;
    }
}
