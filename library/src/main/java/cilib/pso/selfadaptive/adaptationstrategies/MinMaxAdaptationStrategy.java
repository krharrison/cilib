package cilib.pso.selfadaptive.adaptationstrategies;

import cilib.controlparameter.ConstantControlParameter;
import cilib.math.random.UniformDistribution;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterBounds;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.pso.PSO;

public class MinMaxAdaptationStrategy implements SwarmAdaptationStrategy {

    protected ParameterBounds inertiaBounds;
    protected ParameterBounds cognitiveBounds;
    protected ParameterBounds socialBounds;
    protected UniformDistribution uniformDistribution;


    public MinMaxAdaptationStrategy(){
        inertiaBounds = new ParameterBounds(-1,1);
        cognitiveBounds = new ParameterBounds(0, 4.4);
        socialBounds = new ParameterBounds(0, 4.4);
        uniformDistribution = new UniformDistribution();
    }

    @Override
    public void adapt(PSO algorithm) {

        updateBounds(algorithm);

        //update unsuccessful particles
        for(Particle p : algorithm.getTopology()){
            if (p instanceof SelfAdaptiveParticle) {
                SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
                if (!sp.wasSuccessful()) {
                    sp.setInertiaWeight(ConstantControlParameter.of(uniformRandomBounds(inertiaBounds)));
                    sp.setCognitiveAcceleration(ConstantControlParameter.of(uniformRandomBounds(cognitiveBounds)));
                    sp.setSocialAcceleration(ConstantControlParameter.of(uniformRandomBounds(socialBounds)));
                    sp.getParameterSet().resetFitness(); //reset fitness of parameter set after updating!
                }
            }
        }

    }

    /**
     * Update the bounds if more than 1 parameter set was successful.
     * @param algorithm
     */
    private void updateBounds(PSO algorithm){
        ParameterBounds inertiaRange = new ParameterBounds(Double.MAX_VALUE, Double.MIN_VALUE);
        ParameterBounds cognitiveRange = new ParameterBounds(Double.MAX_VALUE, Double.MIN_VALUE);
        ParameterBounds socialRange = new ParameterBounds(Double.MAX_VALUE, Double.MIN_VALUE);

        int successes = 0;

        for(Particle p : algorithm.getTopology()){
            if(p instanceof SelfAdaptiveParticle){
                SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
                if(sp.wasSuccessful()){
                    successes++;

                    ParameterSet parameterSet = sp.getParameterSet();
                    double inertia = parameterSet.getInertiaWeight().getParameter();
                    double cognitive = parameterSet.getCognitiveAcceleration().getParameter();
                    double social = parameterSet.getSocialAcceleration().getParameter();

                    //update the bounds based on the min/max values
                    inertiaRange.setLowerBound(Math.min(inertiaRange.getLowerBound().getParameter(), inertia));
                    inertiaRange.setUpperBound(Math.max(inertiaRange.getUpperBound().getParameter(), inertia));
                    cognitiveRange.setLowerBound(Math.min(cognitiveRange.getLowerBound().getParameter(), cognitive));
                    cognitiveRange.setUpperBound(Math.max(cognitiveRange.getUpperBound().getParameter(), cognitive));
                    socialRange.setLowerBound(Math.min(socialRange.getLowerBound().getParameter(), social));
                    socialRange.setUpperBound(Math.max(socialRange.getUpperBound().getParameter(), social));
                }
            }
        }

        //only update bounds if more than 1 successful parameter. otherwise, no changes made.
        if(successes > 1){
            inertiaBounds = inertiaRange;
            cognitiveBounds = cognitiveRange;
            socialBounds = socialRange;
        }
    }

    private double uniformRandomBounds(ParameterBounds bounds){
        return uniformDistribution.getRandomNumber(bounds.getLowerBound().getParameter(), bounds.getUpperBound().getParameter());
    }

    @Override
    public SwarmAdaptationStrategy getClone() {
        return this;
    }
}
