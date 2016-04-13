/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.LinearlyVaryingControlParameter;
import cilib.entity.Property;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.type.types.Numeric;
import cilib.type.types.container.Vector;
import fj.F;

public class GreyPSOIterationStrategy extends AbstractIterationStrategy<PSO> {

    protected IterationStrategy<PSO> delegate;
    protected double eta;
    protected double inertiaMin;
    protected double inertiaMax;
    protected LinearlyVaryingControlParameter cMax;
    protected LinearlyVaryingControlParameter cMin;

    public GreyPSOIterationStrategy(){
        delegate = new SynchronousIterationStrategy();
        eta = 1.0;
        inertiaMin = 0.4;
        inertiaMax = 0.9;
        cMax = new LinearlyVaryingControlParameter(2.5,2.5);
        cMin = new LinearlyVaryingControlParameter(1.5,2.5);
    }

    @Override
    public GreyPSOIterationStrategy getClone() {
        return null;
    }

    @Override
    public void performIteration(PSO algorithm) {

        double[] gValues = new double[algorithm.getTopology().length()];
        double gMin = Double.MAX_VALUE;
        double gMax = -Double.MIN_VALUE;

        Vector gBest = (Vector)algorithm.getBestSolution().getPosition();

        //iterate through particles to calculate the relational grades
        for(int i = 0; i < algorithm.getTopology().length(); i++){
            Particle p = algorithm.getTopology().index(i);

            gValues[i] = relationalGrade(gBest, (Vector)p.getPosition());
            gMax = Math.max(gMax, gValues[i]);
            gMin = Math.min(gMin, gValues[i]);
        }

        //adapt the particles
        for(int i = 0; i < algorithm.getTopology().length(); i++) {
            SelfAdaptiveParticle p = (SelfAdaptiveParticle) algorithm.getTopology().index(i);

            p.put(Property.PREVIOUS_PARAMETERS, p.getParameterSet().asVector());

            double inertia = calculateInertia(gValues[i], inertiaMin, inertiaMax, gMin, gMax);
            double social = calculateSocial(gValues[i], cMin.getParameter(), cMax.getParameter(), gMin, gMax);
            double cognitive = 4.0 - social;
            p.setInertiaWeight(ConstantControlParameter.of(inertia));
            p.setCognitiveAcceleration(ConstantControlParameter.of(cognitive));
            p.setSocialAcceleration(ConstantControlParameter.of(social));
        }

        delegate.performIteration(algorithm);
    }

    private double relationalGrade(Vector gBest, Vector pos){
        double max = -Double.MIN_VALUE;
        double min = Double.MAX_VALUE;

        Vector.Builder builder = Vector.newBuilder();

        //calculate delta values, keeping track of min and max values accordingly
        for(int i = 0; i < gBest.size(); i++){
            double val = Math.abs(gBest.get(i).doubleValue() - pos.get(i).doubleValue());

            max = Math.max(max, val);
            min = Math.min(min, val);

            builder.add(val);
        }

        if(max == 0) return 1; //if the maximal difference is 0, pos must equal gBest

        Vector relationalCoefficients = builder.build();

        double sum = 0;
        for (Numeric delta : relationalCoefficients) {
            sum += (min + eta * max) / (delta.doubleValue() + eta * max);
        }

        return sum / relationalCoefficients.size();
    }

    private double calculateInertia(double g, double wMin, double wMax, double gMin, double gMax){
        double left = g * (wMin - wMax) / (gMax - gMin);
        double right = (wMax * gMax - wMin * gMin) / (gMax - gMin);

        return left + right;
    }

    private double calculateSocial(double g, double cMin, double cMax, double gMin, double gMax){
        double left = g * (cMax - cMin) / (gMax - gMin);
        double right = (cMin * gMax - cMax * gMin) / (gMax - gMin);

        return left + right;
    }

}
