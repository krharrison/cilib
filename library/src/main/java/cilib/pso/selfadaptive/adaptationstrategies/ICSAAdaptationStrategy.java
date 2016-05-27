/**
 *           __  __
 *   _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *  / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies;

import cilib.algorithm.population.IterationStrategy;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.LinearlyVaryingControlParameter;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.type.types.Numeric;
import cilib.type.types.container.Vector;

/**
 * S. Jun and L. Jian, “An Improved Self-Adaptive Particle Swarm Optimization Algorithm with Simulated Annealing,”
 * in Intelligent Information Technology Application, 2009. IITA 2009. Third International Symposium on, 2009, vol. 3, pp. 396–399.
 */
public class ICSAAdaptationStrategy implements AlgorithmAdaptationStrategy{

    protected double inertiaA;
    protected double inertiaB;
    protected double socialA;
    protected double socialB;

    public ICSAAdaptationStrategy(){
        super();
        inertiaA = 0.9;
        inertiaB = 0.45;
        socialA = 0.5;
        socialB = 2.5;
    }

    public ICSAAdaptationStrategy(ICSAAdaptationStrategy copy){
        this.inertiaA = copy.inertiaA;
        this.inertiaB = copy.inertiaB;
        this.socialA = copy.socialA;
        this.socialB = copy.socialB;
    }

    @Override
    public void adapt(PSO algorithm) {
        //TODO: implement offset as this algorithm requires non-negative fitness values
        double globalBest = algorithm.getBestSolution().getFitness().getValue();
        for(Particle p : algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;

            double adaptiveCoefficient = globalBest / sp.getFitness().getValue();
            double f = calculateF(adaptiveCoefficient);
            double g = calculateG(adaptiveCoefficient);

            double inertia = inertiaA * f + inertiaB;
            double social = socialA * g + socialB;

            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
            sp.setSocialAcceleration(ConstantControlParameter.of(social));
        }
    }

    private double calculateF(double adaptiveCoefficient){
        if(adaptiveCoefficient < 0.0001) return 2;
        else if(adaptiveCoefficient < 0.01) return 1;
        else if(adaptiveCoefficient < 0.1) return 0.3;
        else if(adaptiveCoefficient < 0.9) return -0.8;
        else return -5.5;
    }

    private double calculateG(double adaptiveCoefficient){
        if(adaptiveCoefficient < 0.0001) return 2.5;
        else if(adaptiveCoefficient < 0.01) return 1.2;
        else if(adaptiveCoefficient < 0.1) return 0.5;
        else if(adaptiveCoefficient < 0.9) return 0.2;
        else return 0.1;
    }

    @Override
    public ICSAAdaptationStrategy getClone() {
        return new ICSAAdaptationStrategy(this);
    }

    public double getSocialB() {
        return socialB;
    }

    public void setSocialB(double socialB) {
        this.socialB = socialB;
    }

    public double getInertiaA() {
        return inertiaA;
    }

    public void setInertiaA(double inertiaA) {
        this.inertiaA = inertiaA;
    }

    public double getInertiaB() {
        return inertiaB;
    }

    public void setInertiaB(double inertiaB) {
        this.inertiaB = inertiaB;
    }

    public double getSocialA() {
        return socialA;
    }

    public void setSocialA(double socialA) {
        this.socialA = socialA;
    }

}
