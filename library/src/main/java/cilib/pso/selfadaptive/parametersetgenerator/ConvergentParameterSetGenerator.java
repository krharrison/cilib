/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.parametersetgenerator;

import cilib.controlparameter.ConstantControlParameter;
import cilib.math.random.generator.Rand;
import cilib.pso.selfadaptive.ParameterBounds;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.type.types.Bounds;

public class ConvergentParameterSetGenerator implements ParameterSetGenerator{

    protected ParameterBounds inertiaBounds;
    protected ParameterBounds socialBounds;
    protected ParameterBounds cognitiveBounds;


    public ConvergentParameterSetGenerator(){
        inertiaBounds = new ParameterBounds(0, 1);
        socialBounds = new ParameterBounds(0, 4);
        cognitiveBounds = new ParameterBounds(0, 4);
    }

    public ConvergentParameterSetGenerator(ConvergentParameterSetGenerator copy){
        this.inertiaBounds = copy.getInertiaBounds();
        this.socialBounds = copy.getSocialBounds();
        this.cognitiveBounds = copy.getCognitiveBounds();
    }

    public ParameterSet generate(){

        ParameterSet params = new ParameterSet();

        do{
            params.setInertiaWeight(ConstantControlParameter.of(Rand.nextDouble() * inertiaBounds.getRange() + inertiaBounds.getLowerBound().getParameter()));
            params.setCognitiveAcceleration(ConstantControlParameter.of(Rand.nextDouble() * cognitiveBounds.getRange() + cognitiveBounds.getLowerBound().getParameter()));
            params.setSocialAcceleration(ConstantControlParameter.of(Rand.nextDouble() * socialBounds.getRange() + socialBounds.getLowerBound().getParameter()));
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
    public void setInertiaBounds(ParameterBounds inertiaBounds){
        this.inertiaBounds = inertiaBounds;
    }

    public ParameterBounds getInertiaBounds(){
        return this.inertiaBounds;
    }

    /**
     * Sets the range for the social coefficient.
     * @param socialBounds The bounds for the social coefficient.
     */
    public void setSocialBounds(ParameterBounds socialBounds){
        this.socialBounds = socialBounds;
    }

    public ParameterBounds getSocialBounds(){
        return this.socialBounds;
    }

    /**
     * Sets the range for the cognitive coefficient.
     * @param cognitiveBounds The bounds for the cognitive coefficient.
     */
    public void setCognitiveBounds(ParameterBounds cognitiveBounds){
        this.cognitiveBounds = cognitiveBounds;
    }

    public ParameterBounds getCognitiveBounds(){
        return this.cognitiveBounds;
    }
}
