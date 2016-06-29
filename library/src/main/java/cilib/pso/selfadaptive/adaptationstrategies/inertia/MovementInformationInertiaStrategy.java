/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies.inertia;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.controlparameter.ExponentiallyVaryingControlParameter;
import cilib.measurement.single.diversity.AverageParticleMovement;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.adaptationstrategies.SwarmAdaptationStrategy;
import cilib.type.types.container.Vector;

/**
 * Adapt the inertia weight based on the step sizes of the particles
 *
 * Inspired heavily by:
 * G. Xu, “An Adaptive Parameter Tuning of Particle Swarm Optimization Algorithm,”
 * Applied Mathematics and Computation, vol. 219, no. 9, pp. 4560–4569, 2013.
 */

public class MovementInformationInertiaStrategy implements SwarmAdaptationStrategy {

    protected double inertiaChange;		//change in inertia
    protected double minInertia;		//minimum allowable inertia
    protected double maxInertia;		//maximum allowable inertia
    protected double initialMovement;   //ideal initial movement
    //protected ControlParameter idealMovement;

    private AverageParticleMovement movementMeasure;

    public MovementInformationInertiaStrategy(){
        super();
        inertiaChange = 0.1;
        minInertia = 0.4;
        maxInertia = 0.9;
        movementMeasure = new AverageParticleMovement();
        //1095.445 is the delta-max for domain of [-100,100]^30
        //ExponentiallyVaryingControlParameter movement = new ExponentiallyVaryingControlParameter(1095.445,0);
        //movement.setCurve(-4);
        //idealMovement = movement;
    }

    public MovementInformationInertiaStrategy(MovementInformationInertiaStrategy copy){
        this.inertiaChange = copy.inertiaChange;
        this.minInertia = copy.minInertia;
        this.maxInertia = copy.maxInertia;
        this.movementMeasure = copy.movementMeasure.getClone();
        //this.idealMovement = copy.idealMovement.getClone();
    }

    @Override
    public void adapt(PSO algorithm) {

        //initial movement is delta_max
        if(algorithm.getIterations() == 0){
            Vector domain = (Vector) algorithm.getOptimisationProblem().getDomain().getBuiltRepresentation();
            initialMovement = 0.75 * Math.sqrt(domain.size() * Math.pow(domain.get(0).getBounds().getRange(), 2));
        }

        //double idealMovementSize = idealMovement.getParameter();
        double idealMovementSize = calculateIdealMovement(algorithm);
        double avgMovement = movementMeasure.getValue(algorithm).doubleValue();

        for(Particle particle : algorithm.getTopology()){

            SelfAdaptiveParticle p = (SelfAdaptiveParticle) particle;
            double newInertia;
            if(avgMovement >= idealMovementSize){
                newInertia = Math.max(p.getInertiaWeight().getParameter() - inertiaChange, minInertia);
            }
            else{
                newInertia = Math.min(p.getInertiaWeight().getParameter() + inertiaChange, maxInertia);
            }

            p.setInertiaWeight(ConstantControlParameter.of(newInertia));
        }

    }

    @Override
    public MovementInformationInertiaStrategy getClone() {
        return new MovementInformationInertiaStrategy(this);
    }

    private double calculateIdealMovement(PSO algorithm){
        double factor = algorithm.getPercentageComplete() / 0.95;
        return initialMovement * ((1 + Math.cos(factor * Math.PI)) / 2);
    }

    public double getInertiaChange() {
        return inertiaChange;
    }

    public void setInertiaChange(double inertiaChange) {
        this.inertiaChange = inertiaChange;
    }

    public double getMinInertia() {
        return minInertia;
    }

    public void setMinInertia(double minInertia) {
        this.minInertia = minInertia;
    }

    public double getMaxInertia() {
        return maxInertia;
    }

    public void setMaxInertia(double maxInertia) {
        this.maxInertia = maxInertia;
    }

    //public ControlParameter getIdealMovement() {
    //    return idealMovement;
    //}

   // public void setIdealMovement(ControlParameter idealMovement) {
   //     this.idealMovement = idealMovement;
   // }
}
