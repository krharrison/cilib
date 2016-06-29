/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies.inertia;

import cilib.controlparameter.ConstantControlParameter;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.adaptationstrategies.SwarmAdaptationStrategy;
import cilib.util.selection.arrangement.Arrangement;
import cilib.util.selection.arrangement.SortedArrangement;

import java.util.Iterator;

/**
 * C. Dong, G. Wang, Z. Chen, and Z. Yu, “A Method of Self-adaptive Inertia Weight for PSO,” in Proceedings of the 2008
 * International Conference on Computer Science and Software Engineering, 2008, vol. 1, pp. 1195–1198.
 */
public class DWCYInertiaStrategy implements SwarmAdaptationStrategy {

    protected Arrangement<Particle> arrangement;
    protected double alpha;
    protected double beta;
    protected double gamma;

    public DWCYInertiaStrategy(){
        arrangement = new SortedArrangement<Particle>();
        alpha = 3;
        beta = 200;
        gamma = 8;
    }

    public DWCYInertiaStrategy(DWCYInertiaStrategy copy){
        this.arrangement = copy.arrangement;
        this.alpha = copy.alpha;
        this.beta = copy.beta;
        this.gamma = copy.gamma;
    }


    @Override
    public void adapt(PSO algorithm) {
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
    }

    private Iterable<Particle> orderParticles(PSO algorithm){
        return arrangement.arrange(algorithm.getTopology());
    }

    @Override
    public DWCYInertiaStrategy getClone() {
        return new DWCYInertiaStrategy(this);
    }

    public Arrangement<Particle> getArrangement() {
        return arrangement;
    }

    public void setArrangement(Arrangement<Particle> arrangement) {
        this.arrangement = arrangement;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

}
