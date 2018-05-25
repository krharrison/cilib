package cilib.pso.selfadaptive.pheromoneupdate;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.entity.Property;
import cilib.pso.hpso.pheromoneupdate.PheromoneUpdateStrategy;
import cilib.pso.particle.Particle;

public class PBestImprovementPheromoneUpdateStrategy implements PheromoneUpdateStrategy {

    protected ControlParameter improvement;
    protected ControlParameter noImprovement;

    public PBestImprovementPheromoneUpdateStrategy(){
        improvement = ConstantControlParameter.of(1.0);
        noImprovement = ConstantControlParameter.of(0.0);
    }

    public PBestImprovementPheromoneUpdateStrategy(PBestImprovementPheromoneUpdateStrategy copy){
        this.improvement = copy.improvement;
        this.noImprovement = copy.noImprovement;
    }

    /**
     * Update pheromone if the PBest stagnation counter is 0, indicating it was updated this iteration.
     * @param particle the {@linkplain Particle}
     * @return the value of the pheromone to add/remove
     */
    @Override
    public double updatePheromone(Particle particle) {
        return (particle.get(Property.PBEST_STAGNATION_COUNTER).intValue() == 0) ? improvement.getParameter() : noImprovement.getParameter();
    }

    @Override
    public PBestImprovementPheromoneUpdateStrategy getClone() {
        return new PBestImprovementPheromoneUpdateStrategy(this);
    }

    public void setImprovement(ControlParameter improvement) {
        this.improvement = improvement;
    }

    public void setNoImprovement(ControlParameter noImprovement) {
        this.noImprovement = noImprovement;
    }
}
