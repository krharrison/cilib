/**
 *         __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies.inertia;

import cilib.controlparameter.ConstantControlParameter;
import cilib.entity.Property;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.adaptationstrategies.AlgorithmAdaptationStrategy;
import cilib.type.types.Numeric;
import cilib.type.types.container.Vector;
import fj.F;
import fj.F2;

/**
 * Inertia weight based on velocity informatiom
 *
 * G. Xu, “An Adaptive Parameter Tuning of Particle Swarm Optimization Algorithm,”
 * Applied Mathematics and Computation, vol. 219, no. 9, pp. 4560–4569, 2013.
 */

public class VelocityInformationInertiaStrategy implements AlgorithmAdaptationStrategy {
    protected double inertiaChange;		//change in inertia
    protected double minInertia;		//minimum allowable inertia
    protected double maxInertia;		//maximum allowable inertia


    private double initialVelocity; 	//initial ideal velocity
    
    public VelocityInformationInertiaStrategy(){
        inertiaChange = 0.1;
        minInertia = 0.3;
        maxInertia = 0.9;
    }

    @Override
    public void adapt(PSO algorithm) {

        //TODO: can this be a bit less hacky
        if(algorithm.getIterations() == 0){
            Vector v = (Vector) algorithm.getOptimisationProblem().getDomain().getBuiltRepresentation();
            initialVelocity = v.get(0).getBounds().getRange() / 2;
        }

        double avgVelocity = averageAbsoluteVelocity(algorithm);
        double idealVelocity = calculateIdealVelocity(algorithm);

        for (Particle p : algorithm.getTopology()){
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            double newInertia;
            if(avgVelocity >= idealVelocity){
                newInertia = Math.max(sp.getInertiaWeight().getParameter() - inertiaChange, minInertia);
            }
            else{
                newInertia = Math.min(sp.getInertiaWeight().getParameter() + inertiaChange, maxInertia);
            }

            sp.setInertiaWeight(ConstantControlParameter.of(newInertia));
        }
    }

    private double averageAbsoluteVelocity(PSO algorithm){
        double sum = 0;
        //find the average absolute velocity
        for (Particle p : algorithm.getTopology()){
            Vector velocity = (Vector)p.getVelocity();

            sum += velocity.foldLeft(0.0, new F<Numeric, Double>() {
                @Override
                public Double f(Numeric x) {
                    return Math.abs(x.doubleValue());
                }
            });
        }

        int problemDimension = algorithm.getOptimisationProblem().getDomain().getDimension();
        int numEntities = algorithm.getTopology().length();

        return sum / (numEntities * problemDimension);
    }

    private double calculateIdealVelocity(PSO algorithm){
        double factor = algorithm.getPercentageComplete() / 0.95;

        return initialVelocity * ((1 + Math.cos(factor * Math.PI)) / 2);
    }

    @Override
    public VelocityInformationInertiaStrategy getClone() {
        return this;
    }


    public double getMaxInertia() {
        return maxInertia;
    }

    public void setMaxInertia(double maxInertia) {
        this.maxInertia = maxInertia;
    }

    public double getMinInertia() {
        return minInertia;
    }

    public void setMinInertia(double minInertia) {
        this.minInertia = minInertia;
    }

    public double getInertiaChange() {
        return inertiaChange;
    }

    public void setInertiaChange(double inertiaChange) {
        this.inertiaChange = inertiaChange;
    }

}
