/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.controlparameter.ExponentiallyVaryingControlParameter;
import cilib.entity.Property;
import cilib.measurement.single.diversity.AverageParticleMovement;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.type.types.Numeric;
import cilib.type.types.container.Vector;

/**
 * Adapt the inertia weight based on the step sizes of the particles
 *
 * Based on Adaptive Parameter Tuning of PSO Based on Velocity Information
 * by G. Hu.
 */
public class MovementInformationIterationStrategy extends AbstractIterationStrategy<PSO> {

    protected double initialVelocity; 	//initial ideal velocity
    protected double inertiaChange;		//change in inertia
    protected double minInertia;		//minimum allowable inertia
    protected double maxInertia;		//maximum allowable inertia
    protected IterationStrategy<PSO> delegate;

    protected ControlParameter idealMovement;
    private AverageParticleMovement movementMeasure;

    public MovementInformationIterationStrategy(){
        super();
        inertiaChange = 0.1;
        minInertia = 0.4;
        maxInertia = 0.9;
        movementMeasure = new AverageParticleMovement();
        //1095.445 is the delta-max for domain of [-100,100]^30
        ExponentiallyVaryingControlParameter movement = new ExponentiallyVaryingControlParameter(1095.445,0);
        movement.setCurve(-4);
        idealMovement = movement;
        delegate = new SynchronousIterationStrategy();
    }

    public MovementInformationIterationStrategy(MovementInformationIterationStrategy copy){
        super(copy);
        this.inertiaChange = copy.inertiaChange;
        this.minInertia = copy.minInertia;
        this.maxInertia = copy.maxInertia;
        this.movementMeasure = copy.movementMeasure.getClone();
        this.idealMovement = copy.idealMovement.getClone();
        this.delegate = copy.delegate.getClone();
    }

    @Override
    public MovementInformationIterationStrategy getClone() {
        return new MovementInformationIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        double idealMovementSize = idealMovement.getParameter();
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

            p.put(Property.PREVIOUS_PARAMETERS, p.getParameterSet().asVector());
            p.setInertiaWeight(ConstantControlParameter.of(newInertia));
        }

        delegate.performIteration(algorithm);
    }

    public void setIdealMovement(ControlParameter idealMovement){
        this.idealMovement = idealMovement;
    }

    public void setInertiaChange(double inertiaChange){
        this.inertiaChange = inertiaChange;
    }

    public void setMinInertia(double minInertia){
        this.minInertia = minInertia;
    }

    public void setMaxInertia(double maxInertia){
        this.maxInertia = maxInertia;
    }

    public void setDelegate(IterationStrategy<PSO> delegate){
        this.delegate = delegate;
    }
}