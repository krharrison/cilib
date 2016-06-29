/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies;

import cilib.controlparameter.ConstantControlParameter;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;

/**
 * Z. Wu and J. Zhou, “A Self-Adaptive Particle Swarm Optimization Algorithm with Individual Coefficients Adjustment,”
 * in Proceedings of the 2007 International Conference on Computational Intelligence and Security, 2007, pp. 133–136.
 */
public class SAICAdaptationStrategy implements SwarmAdaptationStrategy {

    protected double inertiaA;
    protected double inertiaB;
    protected double socialA;
    protected double socialB;

    public SAICAdaptationStrategy(){
        super();
        inertiaA = 0.9;
        inertiaB = 0.45;
        socialA = 0.5;
        socialB = 2.5;
    }

    public SAICAdaptationStrategy(SAICAdaptationStrategy copy){
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

            double fitness = sp.getFitness().getValue();

            double adaptiveCoefficient;

            if(Double.isInfinite(fitness)){ 	//set to 1 if invalid fitness
                adaptiveCoefficient = 1;
            }
            else if(fitness == 0.0){		//set to 0 if fitness is 0 to prevent division by 0
                adaptiveCoefficient = 0;
            }
            else {
                adaptiveCoefficient = (fitness - globalBest) / fitness;
            }

            double f = calculateF(adaptiveCoefficient);
            double g = calculateG(adaptiveCoefficient);

            double inertia = inertiaA * f + inertiaB;
            double social = socialA * g + socialB;

            sp.setInertiaWeight(ConstantControlParameter.of(inertia));
            sp.setSocialAcceleration(ConstantControlParameter.of(social));
        }
    }

    private double calculateF(double adaptiveCoefficient){
        return 2 * (1 - Math.cos((Math.PI * adaptiveCoefficient) / 2));
    }

    private double calculateG(double adaptiveCoefficient){
        return 2.5 * (1 - Math.cos((Math.PI * adaptiveCoefficient) / 2));
    }

    @Override
    public SAICAdaptationStrategy getClone() {
        return new SAICAdaptationStrategy(this);
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

    public double getSocialB() {
        return socialB;
    }

    public void setSocialB(double socialB) {
        this.socialB = socialB;
    }


}
