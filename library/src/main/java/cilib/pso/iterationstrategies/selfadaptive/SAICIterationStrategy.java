/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.controlparameter.ConstantControlParameter;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;

public class SAICIterationStrategy extends AbstractIterationStrategy<PSO> {

    protected double inertiaA;
    protected double inertiaB;
    protected double socialA;
    protected double socialB;
    protected IterationStrategy<PSO> delegate;

    protected double offset; //offset required as we need a positive fitness :(

    public SAICIterationStrategy() {
        super();
        inertiaA = 0.9;
        inertiaB = 0.45;
        socialA = 0.5;
        socialB = 2.5;
        delegate = new SynchronousIterationStrategy();
        offset = 0;
    }

    public SAICIterationStrategy(SAICIterationStrategy copy){
        super(copy);
        this.inertiaA = copy.inertiaA;
        this.inertiaB = copy.inertiaB;
        this.socialA = copy.socialA;
        this.socialB = copy.socialB;
        this.delegate = copy.delegate.getClone();
        this.offset = copy.offset;
    }

    @Override
    public SAICIterationStrategy getClone() {
        return new SAICIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        double globalBest = algorithm.getBestSolution().getFitness().getValue() + offset;
        for(Particle p : algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;

            double fitness = sp.getFitness().getValue() + offset;

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

        delegate.performIteration(algorithm);
    }

    private double calculateF(double adaptiveCoefficient){
        return 2 * (1 - Math.cos((Math.PI * adaptiveCoefficient) / 2));
    }

    private double calculateG(double adaptiveCoefficient){
        return 2.5 * (1 - Math.cos((Math.PI * adaptiveCoefficient) / 2));
    }


    public void setDelegate(IterationStrategy<PSO> delegate){
        this.delegate = delegate;
    }

    public void setOffset(double offset){
        this.offset = offset;
    }

    public void setInertiaA(double inertiaA){
        this.inertiaA = inertiaA;
    }

    public void setInertiaB(double inertiaB){
        this.inertiaB = inertiaB;
    }

    public void setSocialA(double socialA){
        this.socialA = socialA;
    }

    public void setSocialB(double socialB){
        this.socialB = socialB;
    }
}
