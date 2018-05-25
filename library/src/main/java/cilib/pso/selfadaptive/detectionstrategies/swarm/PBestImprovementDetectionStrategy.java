package cilib.pso.selfadaptive.detectionstrategies.swarm;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.measurement.single.PBestImprovementRatio;
import cilib.pso.PSO;


/**
 * A change is required when the percentage of particle's which updated their personal best position
 * falls below a given threshold.
 */
public class PBestImprovementDetectionStrategy implements SwarmUpdateDetectionStrategy {

    protected PBestImprovementRatio pBestImprovement;

    protected ControlParameter threshold;

    public PBestImprovementDetectionStrategy()
    {
        pBestImprovement = new PBestImprovementRatio();
        threshold = ConstantControlParameter.of(0.25);
    }

    @Override
    public boolean detect(PSO algorithm) {
        double improvement = pBestImprovement.getValue(algorithm).doubleValue();

        if(improvement < threshold.getParameter()) return true;

        return false;
    }

    @Override
    public SwarmUpdateDetectionStrategy getClone() {
        return this; //TODO: proper clone
    }

    public ControlParameter getThreshold() {
        return threshold;
    }

    public void setThreshold(ControlParameter threshold) {
        this.threshold = threshold;
    }
}
