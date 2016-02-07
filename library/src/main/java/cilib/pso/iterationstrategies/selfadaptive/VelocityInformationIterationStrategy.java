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
import cilib.entity.Property;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.type.types.Numeric;
import cilib.type.types.container.Vector;

/**
 * Adaptive Parameter Tuning of PSO Based on Velocity Information
 * by G. Hu.
 */
public class VelocityInformationIterationStrategy extends AbstractIterationStrategy<PSO> {

    protected double initialVelocity; 	//initial ideal velocity
    protected double inertiaChange;		//change in inertia
    protected double minInertia;		//minimum allowable inertia
    protected double maxInertia;		//maximum allowable inertia
    protected IterationStrategy<PSO> delegate;

    public VelocityInformationIterationStrategy(){
        super();
        initialVelocity = 100;
        inertiaChange = 0.1;
        minInertia = 0.3;
        maxInertia = 0.9;
        delegate = new SynchronousIterationStrategy();
    }

    public VelocityInformationIterationStrategy(VelocityInformationIterationStrategy copy){
        super(copy);
        this.initialVelocity = copy.initialVelocity;
        this.inertiaChange = copy.inertiaChange;
        this.minInertia = copy.minInertia;
        this.maxInertia = copy.maxInertia;
        this.delegate = copy.delegate.getClone();
    }

    @Override
    public AbstractIterationStrategy<PSO> getClone() {
        return new VelocityInformationIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        double idealVelocity = calculateIdealVelocity(algorithm);

        double sum = 0;
        //sum the absolute value of each particle's velocity
        for(Particle p : algorithm.getTopology()){
            Vector velocity = (Vector)p.getVelocity();
            for(Numeric n : velocity){
                sum += Math.abs(n.doubleValue());
            }
        }

        //get the average velocity component value - divide the sum by (number of particles * dimension of the problem)
        int problemDimension = algorithm.getOptimisationProblem().getDomain().getDimension();
        int numEntities = algorithm.getTopology().length();
        double averageAbsoluteVelocity = sum / (numEntities * problemDimension);


        for(Particle particle : algorithm.getTopology()){

            SelfAdaptiveParticle p = (SelfAdaptiveParticle) particle;
            double newInertia;
            if(averageAbsoluteVelocity >= idealVelocity){
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

    protected double calculateIdealVelocity(PSO algorithm){
        double factor = algorithm.getPercentageComplete() / 0.95;

        return initialVelocity * ((1 + Math.cos(factor * Math.PI)) / 2);
    }

    public void setInitialVelocity(double initialVelocity){
        this.initialVelocity = initialVelocity;
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