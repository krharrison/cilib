package cilib.pso.selfadaptive.parameterfitnessadjustmentstrategies;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterSet;

/**
 * Adjust the fitness of a {@link ParameterSet} by a constant amount if it was successful.
 */
public class SuccessfulAdjustmentStrategy implements ParameterFitnessAdjustmentStrategy {

    protected ControlParameter adjustment;

    public SuccessfulAdjustmentStrategy(){
        adjustment = ConstantControlParameter.of(1.0);
    }

    @Override
    public void adjustFitness(PSO algorithm) {

        for(Particle p : algorithm.getTopology()){
            if(p instanceof SelfAdaptiveParticle){
                SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
                if(sp.wasSuccessful()) sp.getParameterSet().incrementFitness(adjustment.getParameter());
            }
        }
    }
}
