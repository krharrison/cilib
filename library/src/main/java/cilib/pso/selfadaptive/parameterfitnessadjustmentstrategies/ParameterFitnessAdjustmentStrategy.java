package cilib.pso.selfadaptive.parameterfitnessadjustmentstrategies;

import cilib.pso.PSO;
import cilib.pso.selfadaptive.ParameterSet;

/**
 * Define how to adjust the fitness of a {@link ParameterSet}.
 */
public interface ParameterFitnessAdjustmentStrategy {

    void adjustFitness(PSO algorithm);

}
