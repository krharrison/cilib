/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies.inertia;

import cilib.controlparameter.ConstantControlParameter;
import cilib.entity.Property;
import cilib.math.random.GaussianDistribution;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.adaptationstrategies.SwarmAdaptationStrategy;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Gaussian Inertia Strategy
 *
 * Retain a list of all inertia values which lead to a particle success (and maintain convergence).
 * Use the average and standard deviation of the archive as the mean and deviation of the Gaussian distribution.
 */
public class GaussianArchiveInertiaStrategy implements SwarmAdaptationStrategy {

    protected int windowSize;           //the number of previous iterations to consider
    protected List<Double>[] archive;    //archive of successful inertia values
    protected GaussianDistribution gaussianDist;

    public GaussianArchiveInertiaStrategy(){
        windowSize = 10;
        gaussianDist = new GaussianDistribution();
        gaussianDist.setMean(ConstantControlParameter.of(0.5));
        gaussianDist.setDeviation(ConstantControlParameter.of(0.2));
    }

    public GaussianArchiveInertiaStrategy(GaussianArchiveInertiaStrategy copy){
        this.windowSize = copy.windowSize;
        this.archive = copy.archive.clone();
        this.gaussianDist = copy.gaussianDist;
    }

    @Override
    public void adapt(PSO algorithm) {
        if(algorithm.getIterations() == 0){
            archive = new List[windowSize];
            for(Particle p : algorithm.getTopology()){
                SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;

                sp.setInertiaWeight(ConstantControlParameter.of(gaussianDist.getRandomNumber()));
            }
        }
        else{
            int index = algorithm.getIterations() % windowSize;
            archive[index] = Lists.newArrayListWithCapacity(algorithm.getTopology().length());
            for (Particle p : algorithm.getTopology()){
                SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;

                //if the fitness improved and the parameters are convergent, add to the archive
                if(sp.getFitness().compareTo(sp.get(Property.PREVIOUS_FITNESS)) > 0){
                       // && sp.getParameterSet().isConvergent()){
                    archive[index].add(sp.getParameterSet().getInertiaWeight().getParameter());
                }
            }

            double mean = computeMean();
            double dev = computeStandardDeviation(mean);

            gaussianDist.setMean(ConstantControlParameter.of(mean));
            gaussianDist.setDeviation(ConstantControlParameter.of(dev));

            for (Particle p : algorithm.getTopology()) {
                SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;

                sp.setInertiaWeight(ConstantControlParameter.of(gaussianDist.getRandomNumber()));
            }
        }

    }

    private double computeMean(){
        double sum = 0;
        int count = 0;

        for(List<Double> list : archive){
            if(list != null){
                for(Double d : list){
                    sum += d;
                }
                count += list.size();
            }
        }

        if(count == 0) return 0.5;

        return sum / count;
    }

    private double computeStandardDeviation(double mean){
        double sum = 0; //reset sum for the standard deviation
        int count = 0;

        for(List<Double> list : archive){
            if(list != null){
                for(Double d : list){
                    sum += (mean - d) * (mean - d);
                }
                count += list.size();
            }
        }

        if(count == 0 || count == 1) return 0.2;
        return Math.sqrt(sum / (count - 1));
    }

    @Override
    public GaussianArchiveInertiaStrategy getClone() {
        return new GaussianArchiveInertiaStrategy(this);
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public GaussianDistribution getGaussianDist() {
        return gaussianDist;
    }

    public void setGaussianDist(GaussianDistribution gaussianDist) {
        this.gaussianDist = gaussianDist;
    }
}
