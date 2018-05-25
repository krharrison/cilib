package cilib.pso.selfadaptive.detectionstrategies.swarm;

import cilib.algorithm.AbstractAlgorithm;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.pso.PSO;

public class PeriodicDetectionStrategy implements SwarmUpdateDetectionStrategy {

    private ControlParameter period;

    public PeriodicDetectionStrategy(){
        period = ConstantControlParameter.of(5.0);
    }


    @Override
    public boolean detect(PSO pso) {
        return AbstractAlgorithm.get().getIterations() % period.getParameter() == 0;
    }

    @Override
    public PeriodicDetectionStrategy getClone() {
        return null;
    }

    public void setPeriod(ControlParameter period) {
        this.period = period;
    }
}
