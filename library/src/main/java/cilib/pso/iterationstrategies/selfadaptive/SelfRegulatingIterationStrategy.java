/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.controlparameter.ConstantControlParameter;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.UniformDistribution;
import cilib.problem.solution.Fitness;
import cilib.problem.solution.InferiorFitness;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import fj.data.List;

public class SelfRegulatingIterationStrategy extends AbstractIterationStrategy<PSO> {
    protected IterationStrategy<PSO> delegate;
    //protected double initialInertia;
    //protected double finalInertia;
    protected double deltaInertia;
    protected double eta;

    private double social;
    private double cognitive;

    public SelfRegulatingIterationStrategy(){
        delegate = new SynchronousIterationStrategy();
        //initialInertia = 1.05;
        //finalInertia = 0.5;
        deltaInertia = 0.00011; //(1.05 - 0.5) / 5000, where 5000 is meant to be the number of iterations
        eta = 1;
    }

    @Override
    public AbstractIterationStrategy<PSO> getClone() {
        return null;
    }

    @Override
    public void performIteration(PSO algorithm) {

        //get the values of the social and cognitive constants so we can reset the values for non-best particles
        if(algorithm.getIterations() == 0){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle)algorithm.getTopology().index(0);
            social = sp.getSocialAcceleration().getParameter();
            cognitive = sp.getSocialAcceleration().getParameter();
        }

        int bestIndex = bestIndex(algorithm);

        List<Particle> particles = algorithm.getTopology();
        for (int i = 0; i < algorithm.getTopology().length(); i++){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) particles.index(i);

            if(i == bestIndex){
                double inertia = sp.getInertiaWeight().getParameter() + eta * deltaInertia;
                sp.setInertiaWeight(ConstantControlParameter.of(inertia));
                sp.setCognitiveAcceleration(ConstantControlParameter.of(0));
                sp.setSocialAcceleration(ConstantControlParameter.of(0));
            }
            else
            {
                double inertia = sp.getInertiaWeight().getParameter() - deltaInertia;
                sp.setInertiaWeight(ConstantControlParameter.of(inertia));
                sp.setCognitiveAcceleration(ConstantControlParameter.of(cognitive));
                sp.setSocialAcceleration(ConstantControlParameter.of(social));
            }
        }

        delegate.performIteration(algorithm);

    }

    private int bestIndex(PSO algorithm) {
        Fitness bestFitness = InferiorFitness.instance();
        int bestIndex = 0;
        List<Particle> particles = algorithm.getTopology();
        for (int i = 0; i < algorithm.getTopology().length(); i++){
            if (particles.index(i).getFitness().compareTo(bestFitness) > 0) {
                bestFitness = particles.index(i).getFitness();
                bestIndex = i;
            }
        }
        return bestIndex;
    }
}
