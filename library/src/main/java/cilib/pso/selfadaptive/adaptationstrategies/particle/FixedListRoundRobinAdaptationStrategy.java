package cilib.pso.selfadaptive.adaptationstrategies.particle;

import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FixedListRoundRobinAdaptationStrategy implements ParticleAdaptationStrategy {

    protected List<ParameterSet> parameters;
    protected HashMap<SelfAdaptiveParticle, Integer> indices;


    public FixedListRoundRobinAdaptationStrategy(){
        parameters = new ArrayList<>();
        indices = new HashMap<>();
    }

    public FixedListRoundRobinAdaptationStrategy(FixedListRoundRobinAdaptationStrategy copy){
        this.parameters = new ArrayList<>(copy.parameters);
        this.indices = new HashMap<>(copy.indices);
    }

    @Override
    public void adapt(Particle particle, PSO algorithm) {
        SelfAdaptiveParticle sp = (SelfAdaptiveParticle) particle;
        int index = 0;
        if(indices.containsKey(sp)){
            index = indices.get(sp);
        }

        ParameterSet params = parameters.get(index);
        sp.setParameterSet(params);
        index = ++index % parameters.size();
        indices.put(sp, index);
    }

    @Override
    public FixedListRoundRobinAdaptationStrategy getClone() {
        return new FixedListRoundRobinAdaptationStrategy(this);
    }

    public void addParameterSet(ParameterSet params){
        parameters.add(params);
    }
}
