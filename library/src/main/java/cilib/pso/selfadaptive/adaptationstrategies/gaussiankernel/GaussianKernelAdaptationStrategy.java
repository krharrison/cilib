/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies.gaussiankernel;

import cilib.entity.Property;
import cilib.math.random.GaussianDistribution;
import cilib.problem.solution.InferiorFitness;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.pso.selfadaptive.adaptationstrategies.SwarmAdaptationStrategy;
import cilib.pso.selfadaptive.parametersetgenerator.ConvergentParameterSetGenerator;
import cilib.pso.selfadaptive.parametersetgenerator.ParameterSetGenerator;
import cilib.type.types.container.Vector;
import cilib.util.selection.WeightedObject;
import cilib.util.selection.recipes.RouletteWheelSelector;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Heavily inspired by:
 *
 * K. Socha and M. Dorigo, “Ant Colony Optimization for Continuous Domains,”
 * European Journal of Operational Research, vol. 185, no. 3, pp. 1155–1173, 2008.
 *
 * TODO: need to find a good way to associate fitness to parameters based on success.
 * Improving the best particle should be better than improving the worst
 */

public class GaussianKernelAdaptationStrategy implements SwarmAdaptationStrategy {

    protected double q;     //locality of the search process
    protected int k;        //archive size
    protected double eta;   //convergence speed;

    protected GaussianDistribution gaussianDistribution;
    protected ParameterSetGenerator generator;

    protected List<ArchiveSolution> archive;
    protected RouletteWheelSelector<WeightedObject> selector;

    public GaussianKernelAdaptationStrategy(){
        q = 0.0001;
        k = 50;
        eta = 0.85;
        archive = Lists.newArrayListWithCapacity(k);
        generator = new ConvergentParameterSetGenerator();
        selector = new RouletteWheelSelector<>();
        gaussianDistribution = new GaussianDistribution();
    }

    public GaussianKernelAdaptationStrategy(GaussianKernelAdaptationStrategy copy){
        this.q = copy.q;
        this.k = copy.k;
        this.eta = copy.eta;
        this.archive = copy.archive;
        this.generator = copy.generator.getClone();
        this.selector = copy.selector;
        this.gaussianDistribution = copy.gaussianDistribution;
    }

    public GaussianKernelAdaptationStrategy getClone(){
        return new GaussianKernelAdaptationStrategy(this);
    }


    @Override
    public void adapt(PSO algorithm) {

        if(algorithm.getIterations() == 0){
            for(int i = 0; i < k - algorithm.getTopology().length(); i++){ //fill remaining archive slots, if any, with random solutions
                ParameterSet params = generator.generate();
                params.setFitness(1.0 / k);
                archive.add(new ArchiveSolution(params));
            }
        }
        //else{
            for(Particle p : algorithm.getTopology()){
                SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
                ParameterSet params =  sp.getParameterSet();
                if(params.isConvergent()) { //only add if the parameters were convergent
                    params.setFitness(1.0 / k);
                    //TODO: assumes minimization
                    //params.setFitness(sp.get(Property.PREVIOUS_FITNESS).getValue() - sp.getFitness().getValue());  //TODO: Evaluate properly
                    archive.add(new ArchiveSolution(params)); //TODO: sorted insertion based on fitness?
                }
            }
       // }

        //TODO: Can I get away without the sort here? Need to ensure that the archive is sorted properly
        Collections.sort(archive, new Comparator<ArchiveSolution>() {
            @Override
            public int compare(ArchiveSolution s1, ArchiveSolution s2) {
                boolean s1NaN = s1.getParameterSet().getFitness().getValue().isNaN();
                boolean s2NaN = s2.getParameterSet().getFitness().getValue().isNaN();
                if(s1NaN && s2NaN) return 0;
                else if(s1NaN) return 1;
                else if(s2NaN) return -1;
                else return s1.compareTo(s2);
            }
        });

        archive = archive.subList(0, k); //keep k best solutions
        assignArchiveWeights();

        //TODO: can I do this based on just the archive list? Perhaps need to define appropriate weighting
        List<WeightedObject> parameters = Lists.newArrayListWithCapacity(k);
        for(ArchiveSolution solution : archive){
            parameters.add(new WeightedObject(solution, solution.weight));
        }

        for(Particle p : algorithm.getTopology()) {
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            ArchiveSolution solution = (ArchiveSolution) selector.on(parameters).select().getObject();
            Vector solutionVector = solution.getParameterSet().asVector();

            Vector.Builder builder = Vector.newBuilder();
            for(int i = 0; i < 3; i++){
                double mean = solutionVector.get(i).doubleValue(); //the mean of the Gaussian
                double dev = 0; //the standard deviation of the Gaussian
                for(ArchiveSolution sol : archive){
                    dev += Math.abs(sol.getParameterSet().asVector().get(i).doubleValue() - mean);
                }

                dev /= (k - 1);
                dev *= eta;

                builder.add(gaussianDistribution.getRandomNumber(mean, dev));
            }

            ParameterSet params = new ParameterSet();
            params.fromVector(builder.build());
            sp.setParameterSet(params);
        }

    }


    private void assignArchiveWeights(){
        for(int i = 0; i < archive.size(); i++){
            ArchiveSolution solution = archive.get(i);

            double frac = 1.0 / (q * k * Math.sqrt(2 * Math.PI));
            double expFrac = -Math.pow(i - 1, 2) / (2*q*q*k*k);
            solution.setWeight(frac * Math.exp(expFrac));
        }
    }

    public double getQ() {
        return q;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public double getEta() {
        return eta;
    }

    public void setEta(double eta) {
        this.eta = eta;
    }

    public ParameterSetGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(ParameterSetGenerator generator) {
        this.generator = generator;
    }


}


