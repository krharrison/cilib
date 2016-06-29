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
public class AdventurousLearningAutomataAdaptationStrategy implements SwarmAdaptationStrategy {

    protected LearningAutomata<Double> inertiaLA;
    protected LearningAutomata<Double> socialLA;
    protected LearningAutomata<Double> cognitiveLA;
    protected Bounds inertiaBounds;
    protected Bounds socialBounds;
    protected Bounds cognitiveBounds;
    protected int numInteria; //number of actions in the inertia automata
    protected int numAcceleration; //number of actions in the social/cognitive automata
    protected double successThreshold;

    Action<Double> inertiaAction;
    Action<Double> socialAction;
    Action<Double> cognitiveAction;

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
            inertiaLA.updateSuccessful(inertiaAction);
            socialLA.updateSuccessful(socialAction);
            cognitiveLA.updateSuccessful(cognitiveAction);
        }
        else
        {
            inertiaLA.updateUnsuccessful(inertiaAction);
            socialLA.updateUnsuccessful(socialAction);
            cognitiveLA.updateUnsuccessful(cognitiveAction);
        }

        inertiaAction = inertiaLA.select();
        cognitiveAction = cognitiveLA.select();
        socialAction = socialLA.select();

        for (Particle p : algorithm.getTopology()) {
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            sp.setInertiaWeight(ConstantControlParameter.of(inertiaAction.getAction()));
            sp.setSocialAcceleration(ConstantControlParameter.of(socialAction.getAction()));
            sp.setCognitiveAcceleration(ConstantControlParameter.of(cognitiveAction.getAction()));
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

    public Bounds getInertiaBounds() {
        return inertiaBounds;
    }

    public void setInertiaBounds(Bounds inertiaBounds) {
        this.inertiaBounds = inertiaBounds;
    }

    public Bounds getSocialBounds() {
        return socialBounds;
    }

    public void setSocialBounds(Bounds socialBounds) {
        this.socialBounds = socialBounds;
    }

    public Bounds getCognitiveBounds() {
        return cognitiveBounds;
    }

    public void setCognitiveBounds(Bounds cognitiveBounds) {
        this.cognitiveBounds = cognitiveBounds;
    }

    public int getNumInteria() {
        return numInteria;
    }

    public void setNumInteria(int numInteria) {
        this.numInteria = numInteria;
    }

    public int getNumAcceleration() {
        return numAcceleration;
    }

    public void setNumAcceleration(int numAcceleration) {
        this.numAcceleration = numAcceleration;
    }

    public double getSuccessThreshold() {
        return successThreshold;
    }

    public void setSuccessThreshold(double successThreshold) {
        this.successThreshold = successThreshold;
    }

    public LearningAutomata<Double> getInertiaLA() {
        return inertiaLA;
    }

    public void setInertiaLA(LearningAutomata<Double> inertiaLA) {
        this.inertiaLA = inertiaLA;
    }

    public LearningAutomata<Double> getSocialLA() {
        return socialLA;
    }

    public void setSocialLA(LearningAutomata<Double> socialLA) {
        this.socialLA = socialLA;
    }

    public LearningAutomata<Double> getCognitiveLA() {
        return cognitiveLA;
    }

    public void setCognitiveLA(LearningAutomata<Double> cognitiveLA) {
        this.cognitiveLA = cognitiveLA;
    }


}