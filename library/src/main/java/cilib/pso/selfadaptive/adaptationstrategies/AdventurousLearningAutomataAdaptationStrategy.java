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
public class AdventurousLearningAutomataAdaptationStrategy implements AlgorithmAdaptationStrategy {

    protected LearningAutomata<Double> inertiaLA;
    protected LearningAutomata<Double> socialLA;
    protected LearningAutomata<Double> cognitiveLA;
    protected Bounds inertiaBounds;
    protected Bounds socialBounds;
    protected Bounds cognitiveBounds;
    protected int numInteria; //number of actions in the inertia automata
    protected int numAcceleration; //number of actions in the social/cognitive automata
    protected double successThreshold;

    Action<Double> inertia;
    Action<Double> social;
    Action<Double> cognitive;

    public AdventurousLearningAutomataAdaptationStrategy(){
        inertiaBounds = new Bounds(0, 1);
        socialBounds = new Bounds(0, 2);
        cognitiveBounds = new Bounds(0, 2);
        numInteria = 20;
        numAcceleration = 10;
        successThreshold = 0.5;
    }

    public AdventurousLearningAutomataAdaptationStrategy(AdventurousLearningAutomataAdaptationStrategy copy){
        this.inertiaBounds = copy.inertiaBounds;
        this.socialBounds = copy.socialBounds;
        this.cognitiveBounds = copy.cognitiveBounds;
        this.numInteria = copy.numInteria;
        this.numAcceleration = copy.numAcceleration;
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
            sp.setInertiaWeight(ConstantControlParameter.of(inertia.getAction()));
            sp.setSocialAcceleration(ConstantControlParameter.of(social.getAction()));
            sp.setCognitiveAcceleration(ConstantControlParameter.of(cognitive.getAction()));
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
        double min = inertiaBounds.getLowerBound();
        double range = inertiaBounds.getRange();
        for(int i = 0; i < numInteria; i++){
            double inertia = min + range * i / (double)numInteria;
            inertiaLA.addAction(new Action<>(inertia, 1.0 / numInteria));
        }
    }

    private void initializeSocialLA(){
        socialLA = new LearningAutomata<>();
        double min = socialBounds.getLowerBound();
        double range = socialBounds.getRange();
        for(int i = 0; i < numAcceleration; i++){
            double social = min + range * i / (double)numAcceleration;
            socialLA.addAction(new Action<>(social, 1.0 / numAcceleration));
        }
    }

    private void initializeCognitiveLA(){
        cognitiveLA = new LearningAutomata<>();
        double min = cognitiveBounds.getLowerBound();
        double range = cognitiveBounds.getRange();
        for(int i = 0; i < numAcceleration; i++){
            double cognitive = min + range * i / (double)numAcceleration;
            cognitiveLA.addAction(new Action<>(cognitive, 1.0 / numAcceleration));
        }
    }

    @Override
    public AdventurousLearningAutomataAdaptationStrategy getClone() {
        return new AdventurousLearningAutomataAdaptationStrategy(this);
    }

}