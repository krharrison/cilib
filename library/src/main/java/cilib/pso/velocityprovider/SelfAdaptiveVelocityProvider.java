/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.velocityprovider;

import fj.P1;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.math.random.generator.Rand;
import cilib.pso.guideprovider.GuideProvider;
import cilib.pso.guideprovider.NBestGuideProvider;
import cilib.pso.guideprovider.PBestGuideProvider;
import cilib.pso.particle.Particle;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.type.types.Numeric;
import cilib.type.types.container.StructuredType;
import cilib.type.types.container.Vector;
import cilib.util.Vectors;

public class SelfAdaptiveVelocityProvider implements VelocityProvider {

    private ControlParameter vMax;
    private ParameterSet parameters;

    private GuideProvider globalGuideProvider;
    private GuideProvider localGuideProvider;

    public SelfAdaptiveVelocityProvider(){

        this.vMax = ConstantControlParameter.of(Double.NaN);
        this.parameters = new ParameterSet();

        this.globalGuideProvider = new NBestGuideProvider();
        this.localGuideProvider = new PBestGuideProvider();

    }

    public SelfAdaptiveVelocityProvider(SelfAdaptiveVelocityProvider copy){
        this.vMax = copy.vMax.getClone();
        this.parameters = copy.parameters.getClone();

        this.globalGuideProvider = copy.globalGuideProvider.getClone();
        this.localGuideProvider = copy.localGuideProvider.getClone();
    }

    @Override
    public SelfAdaptiveVelocityProvider getClone() {
        return new SelfAdaptiveVelocityProvider(this);
    }

    private static P1<Number> random() {
        return new P1<Number>() {
            @Override
            public Number _1() {
                return Rand.nextDouble();
            }
        };
    }

    private static P1<Number> cp(final ControlParameter r) {
        return new P1<Number>() {
            @Override
            public Number _1() {
                return r.getParameter();
            }
        };
    }

    @Override
    public StructuredType get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector localGuide = (Vector) localGuideProvider.get(particle);
        Vector globalGuide = (Vector) globalGuideProvider.get(particle);

        Vector dampenedVelocity = Vector.copyOf(velocity).multiply(cp(parameters.getInertiaWeight()));
        Vector cognitiveComponent = Vector.copyOf(localGuide).subtract(position).multiply(cp(parameters.getCognitiveAcceleration())).multiply(random());
        Vector socialComponent = Vector.copyOf(globalGuide).subtract(position).multiply(cp(parameters.getSocialAcceleration())).multiply(random());
        velocity =  Vectors.sumOf(dampenedVelocity, cognitiveComponent, socialComponent).valueE("Cannot determine velocity");

        //return the raw velocity if clamping isn't to be used (i.e., vMax is NaN)
        if(Double.isNaN(vMax.getParameter())) return velocity;

        //perform clamping
        Vector.Builder builder = Vector.newBuilder();
        for (Numeric value : velocity) {
            builder.add(Math.min(Math.max(-vMax.getParameter(), value.doubleValue()), vMax.getParameter()));
        }
        return builder.build();
    }

    /**
     * Sets the GuideProvider responsible for retrieving a particle's global guide.
     * @param globalGuideProvider The guide provider to set.
     */
    public void setGlobalGuideProvider(GuideProvider globalGuideProvider) {
        this.globalGuideProvider = globalGuideProvider;
    }

    /**
     * Sets the GuideProvider responsible for retrieving a particle's local guide.
     * @param localGuideProvider The guide provider to set.
     */
    public void setLocalGuideProvider(GuideProvider localGuideProvider) {
        this.localGuideProvider = localGuideProvider;
    }

    public void setParameterSet(ParameterSet parameters){
        this.parameters = parameters;
    }

    public ParameterSet getParameterSet(){
        return this.parameters;
    }

    /**
     * Get the {@linkplain ControlParameter} representing the inertia weight of
     * the {@linkplain VelocityProvider}.
     * @return the inertia component {@linkplain ControlParameter}.
     */
    public ControlParameter getInertiaWeight() {
        return parameters.getInertiaWeight();
    }

    /**
     * Set the {@linkplain ControlParameter} for the inertia weight of the
     * velocity update equation.
     * @param inertiaWeight The inertiaWeight to set.
     */
    public void setInertiaWeight(ControlParameter inertiaWeight) {
        this.parameters.setInertiaWeight(inertiaWeight);
    }

    /**
     * Gets the {@linkplain ControlParameter} representing the cognitive
     * component within this {@link VelocityProvider}.
     * @return the cognitiveComponent.
     */
    public ControlParameter getCognitiveAcceleration() {
        return this.parameters.getCognitiveAcceleration();
    }

    /**
     * Set the cognitive component {@linkplain ControlParameter}.
     * @param cognitiveComponent The cognitiveComponent to set.
     */
    public void setCognitiveAcceleration(ControlParameter cognitiveComponent) {
        this.parameters.setCognitiveAcceleration(cognitiveComponent);
    }

    /**
     * Get the {@linkplain ControlParameter} representing the social component
     * of the velocity update equation.
     * @return the socialComponent.
     */
    public ControlParameter getSocialAcceleration() {
        return this.parameters.getSocialAcceleration();
    }

    /**
     * Set the {@linkplain ControlParameter} for the social component.
     * @param socialComponent The socialComponent to set.
     */
    public void setSocialAcceleration(ControlParameter socialComponent) {
        this.parameters.setSocialAcceleration(socialComponent);
    }

}