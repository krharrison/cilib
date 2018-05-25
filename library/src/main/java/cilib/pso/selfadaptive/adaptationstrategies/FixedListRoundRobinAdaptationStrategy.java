package cilib.pso.selfadaptive.adaptationstrategies;

import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterSet;

import java.util.ArrayList;
import java.util.List;

public class FixedListRoundRobinAdaptationStrategy implements SwarmAdaptationStrategy {

    protected List<ParameterSet> parameters;
    protected int index = 0;

    public FixedListRoundRobinAdaptationStrategy(){
        parameters = new ArrayList<>();
    }

    public FixedListRoundRobinAdaptationStrategy(FixedListRoundRobinAdaptationStrategy copy){
        this.parameters = new ArrayList<>(copy.parameters);
        this.index = copy.index;
    }

    @Override
    public void adapt(PSO algorithm) {
        ParameterSet params = parameters.get(index);
        params.resetFitness();

        for(Particle p : algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            sp.setParameterSet(params);
        }

        index = ++index % parameters.size();

    }

    @Override
    public FixedListRoundRobinAdaptationStrategy getClone() {
        return new FixedListRoundRobinAdaptationStrategy(this);
    }

    public void addParameterSet(ParameterSet params){
        parameters.add(params);
    }
}
