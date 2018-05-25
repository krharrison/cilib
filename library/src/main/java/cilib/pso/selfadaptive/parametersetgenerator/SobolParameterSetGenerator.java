package cilib.pso.selfadaptive.parametersetgenerator;

import cilib.math.random.generator.Rand;
import cilib.math.random.generator.quasi.Sobol;
import cilib.pso.selfadaptive.ParameterBounds;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.type.types.Bounds;

public class SobolParameterSetGenerator implements ParameterSetGenerator {

    protected Sobol sobol;

    protected ParameterBounds inertiaBounds;
    protected ParameterBounds socialBounds;
    protected ParameterBounds cognitiveBounds;

    public SobolParameterSetGenerator(){
        sobol = new Sobol(Rand.nextLong());
        sobol.setDimensions(3); //for three parameters

        inertiaBounds = new ParameterBounds(-1, 1);
        socialBounds = new ParameterBounds(0, 4.4);
        cognitiveBounds = new ParameterBounds(0, 4.4);
    }

    public SobolParameterSetGenerator(SobolParameterSetGenerator copy){
        this.sobol = copy.sobol;
        this.inertiaBounds = copy.inertiaBounds;
        this.socialBounds = copy.socialBounds;
        this.cognitiveBounds = copy.cognitiveBounds;
    }

    @Override
    public ParameterSet generate() {
        double[] point = sobol.nextPoint();
        point[0] = point[0] * inertiaBounds.getRange() + inertiaBounds.getLowerBound().getParameter();
        point[1] = point[1] * cognitiveBounds.getRange() + cognitiveBounds.getLowerBound().getParameter();
        point[2] = point[2] * socialBounds.getRange() + socialBounds.getLowerBound().getParameter();

        ParameterSet params = ParameterSet.fromValues(point[0], point[1], point[2]);

        return params;
    }

    @Override
    public ParameterSetGenerator getClone() {
        return null;
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
