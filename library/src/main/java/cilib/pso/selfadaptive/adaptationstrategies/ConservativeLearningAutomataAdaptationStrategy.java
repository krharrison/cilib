/**
 *           __  __
 *   _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *  / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies;


import cilib.controlparameter.ConstantControlParameter;
import cilib.entity.Property;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.learningautomata.Action;
import cilib.pso.selfadaptive.learningautomata.LearningAutomata;
import cilib.type.types.Bounds;

/**
 * The 'adventurous' unified adaptive PSO approach from:
 *
 * A. B. Hashemi and M. R. Meybodi, “A Note on the Learning Automata Based Algorithms for Adaptive Parameter
 * Selection in PSO,” Applied Soft Computing, vol. 11, no. 1, pp. 689–705, 2011.
 */
public class ConservativeLearningAutomataAdaptationStrategy implements AlgorithmAdaptationStrategy {

    protected LearningAutomata<Double> inertiaLA;
    protected LearningAutomata<Double> socialLA;
    protected LearningAutomata<Double> cognitiveLA;

    protected double deltaInertia;
    protected double deltaAcceleration;
    protected double successThreshold;

    Action<Double> inertia;
    Action<Double> social;
    Action<Double> cognitive;

    public ConservativeLearningAutomataAdaptationStrategy(){
        deltaInertia = 0.1;
        deltaAcceleration = 0.1;
        successThreshold = 0.5;
    }

    public ConservativeLearningAutomataAdaptationStrategy(ConservativeLearningAutomataAdaptationStrategy copy){
        this.deltaInertia = copy.deltaInertia;
        this.deltaAcceleration = copy.deltaAcceleration;
        this.successThreshold = 0.5;
        this.inertiaLA = copy.inertiaLA;
        this.socialLA = copy.socialLA;
        this.cognitiveLA = copy.cognitiveLA;

        //TODO: should I copy the current actions?
    }


    @Override
    public void adapt(PSO algorithm) {

        if(algorithm.getIterations() == 0){
            initializeInertiaLA();
            initializeSocialLA();
            initializeCognitiveLA();
        }
        else if(successful(algorithm)){
            inertiaLA.updateSuccessful(inertia);
            socialLA.updateSuccessful(social);
            cognitiveLA.updateSuccessful(cognitive);
        }
        else
        {
            inertiaLA.updateUnsuccessful(inertia);
            socialLA.updateUnsuccessful(social);
            cognitiveLA.updateUnsuccessful(cognitive);
        }

        inertia = inertiaLA.select();
        cognitive = cognitiveLA.select();
        social = socialLA.select();

        for (Particle p : algorithm.getTopology()) {
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;

            double newInertia = sp.getInertiaWeight().getParameter() + inertia.getAction();
            double newSocial = sp.getSocialAcceleration().getParameter() + social.getAction();
            double newCognitive = sp.getCognitiveAcceleration().getParameter() + cognitive.getAction();

            sp.setInertiaWeight(ConstantControlParameter.of(newInertia));
            sp.setSocialAcceleration(ConstantControlParameter.of(newSocial));
            sp.setCognitiveAcceleration(ConstantControlParameter.of(newCognitive));
        }
    }

    private boolean successful(PSO algorithm){
        int successes = 0;
        for (Particle p : algorithm.getTopology()) {
            if(p.getFitness().compareTo(p.get(Property.PREVIOUS_FITNESS)) > 0) successes++;
        }

        return successes / (double)algorithm.getTopology().length() > successThreshold;
    }

    private void initializeInertiaLA(){
        inertiaLA = new LearningAutomata<>();
        inertiaLA.addAction(new Action<>(-deltaInertia, 1.0 / 3));    //decrease
        inertiaLA.addAction(new Action<>(0.0, 1.0 / 3));              //no change
        inertiaLA.addAction(new Action<>(deltaInertia, 1.0 / 3));     //increase
    }

    private void initializeSocialLA(){
        socialLA = new LearningAutomata<>();
        socialLA.addAction(new Action<>(-deltaAcceleration, 1.0 / 3));  //decrease
        socialLA.addAction(new Action<>(0.0, 1.0 / 3));                 //no change
        socialLA.addAction(new Action<>(deltaAcceleration, 1.0 / 3));   //increase
    }

    private void initializeCognitiveLA(){
        cognitiveLA = new LearningAutomata<>();
        cognitiveLA.addAction(new Action<>(-deltaAcceleration, 1.0 / 3));  //decrease
        cognitiveLA.addAction(new Action<>(0.0, 1.0 / 3));                 //no change
        cognitiveLA.addAction(new Action<>(deltaAcceleration, 1.0 / 3));   //increase
    }

    @Override
    public ConservativeLearningAutomataAdaptationStrategy getClone() {
        return new ConservativeLearningAutomataAdaptationStrategy(this);
    }

}