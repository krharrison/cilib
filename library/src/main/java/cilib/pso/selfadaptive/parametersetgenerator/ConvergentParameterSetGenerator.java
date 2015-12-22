/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.parametersetgenerator;

import cilib.controlparameter.ConstantControlParameter;
import cilib.math.random.generator.Rand;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.type.types.Bounds;

public class ConvergentParameterSetGenerator implements ParameterSetGenerator{

    protected Bounds inertiaBounds;
    protected Bounds socialBounds;
    protected Bounds cognitiveBounds;


    public ConvergentParameterSetGenerator(){
        inertiaBounds = new Bounds(0, 1);
        socialBounds = new Bounds(0, 4);
        cognitiveBounds = new Bounds(0, 4);
    }

    public ConvergentParameterSetGenerator(ConvergentParameterSetGenerator copy){
        this.inertiaBounds = copy.getInertiaBounds();
        this.socialBounds = copy.getSocialBounds();
        this.cognitiveBounds = copy.getCognitiveBounds();
    }

    public ParameterSet generate(){

        ParameterSet params = new ParameterSet();

        do{
            params.setInertiaWeight(ConstantControlParameter.of(Rand.nextDouble() * inertiaBounds.getRange() + inertiaBounds.getLowerBound()));
            params.setCognitiveAcceleration(ConstantControlParameter.of(Rand.nextDouble() * cognitiveBounds.getRange() + cognitiveBounds.getLowerBound()));
            params.setSocialAcceleration(ConstantControlParameter.of(Rand.nextDouble() * socialBounds.getRange() + socialBounds.getLowerBound()));
        } while(!params.isConvergent());

        return params;
    }

    public ConvergentParameterSetGenerator getClone(){
        return new ConvergentParameterSetGenerator(this);
    }

    /**
     * Sets the range for the inertia coefficient.
     * @param inertiaBounds The bounds for the inertia coefficient.
     */
    public void setInertiaBounds(Bounds inertiaBounds){
        this.inertiaBounds = inertiaBounds;
    }

    public Bounds getInertiaBounds(){
        return this.inertiaBounds;
    }

    /**
     * Sets the range for the social coefficient.
     * @param socialBounds The bounds for the social coefficient.
     */
    public void setSocialBounds(Bounds socialBounds){
        this.socialBounds = socialBounds;
    }

    public Bounds getSocialBounds(){
        return this.socialBounds;
    }

    /**
     * Sets the range for the cognitive coefficient.
     * @param cognitiveBounds The bounds for the cognitive coefficient.
     */
    public void setCognitiveBounds(Bounds cognitiveBounds){
        this.cognitiveBounds = cognitiveBounds;
    }

    public Bounds getCognitiveBounds(){
        return this.cognitiveBounds;
    }
}
