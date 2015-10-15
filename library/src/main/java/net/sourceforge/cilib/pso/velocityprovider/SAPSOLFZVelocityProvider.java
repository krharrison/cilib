/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import fj.P1;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.guideprovider.GuideProvider;
import net.sourceforge.cilib.pso.guideprovider.NBestGuideProvider;
import net.sourceforge.cilib.pso.guideprovider.PBestGuideProvider;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * Implementation of the standard / default velocity update equation.
 */
public final class SAPSOLFZVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = 8204479765311251730L;

    protected ControlParameter inertiaWeight;
    protected ControlParameter socialAcceleration;
    protected ControlParameter cognitiveAcceleration;
    protected double aInitial;
    protected double aFinal;
    protected double modulationIndex;

    private GuideProvider globalGuideProvider;
    private GuideProvider localGuideProvider;

    /** Creates a new instance of StandardVelocityUpdate. */
    public SAPSOLFZVelocityProvider() {
        this(ConstantControlParameter.of(0.729844),
            ConstantControlParameter.of(1.496180),
            ConstantControlParameter.of(1.496180));
        
        	this.globalGuideProvider = new NBestGuideProvider();
        	this.localGuideProvider = new PBestGuideProvider();
        
        	aInitial = -0.05;
        	aFinal = 0.15;
        	modulationIndex = 0.6;
    }

    public SAPSOLFZVelocityProvider(ControlParameter inertia, ControlParameter social, ControlParameter cog) {
        this.inertiaWeight = inertia;
        this.socialAcceleration = social;
        this.cognitiveAcceleration = cog;

        this.globalGuideProvider = new NBestGuideProvider();
        this.localGuideProvider = new PBestGuideProvider();

        this.aInitial = -0.05;
        this.aFinal = 0.15;
        this.modulationIndex = 0.6;

    }

    /**
     * Copy constructor.
     * @param copy The object to copy.
     */
    public SAPSOLFZVelocityProvider(SAPSOLFZVelocityProvider copy) {
        this.inertiaWeight = copy.inertiaWeight.getClone();
        this.cognitiveAcceleration = copy.cognitiveAcceleration.getClone();
        this.socialAcceleration = copy.socialAcceleration.getClone();
        this.globalGuideProvider = copy.globalGuideProvider.getClone();
        this.localGuideProvider = copy.localGuideProvider.getClone();
        this.aInitial = copy.aInitial;
        this.aFinal = copy.aFinal;
        this.modulationIndex = copy.modulationIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SAPSOLFZVelocityProvider getClone() {
        return new SAPSOLFZVelocityProvider(this);
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

    /**
     * Perform the velocity update for the given {@linkplain Particle}.
     * @param particle The {@linkplain Particle} velocity that should be updated.
     */
    @Override
    public Vector get(Particle particle) {
    	
    	double activity = calculateActivity(particle);
    	double threshold = calculateThreshold();
    	
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector localGuide = (Vector) localGuideProvider.get(particle);
        Vector globalGuide = (Vector) globalGuideProvider.get(particle);
        
        Vector dampenedVelocity = Vector.copyOf(velocity).multiply(inertiaWeight.getParameter());
        Vector cognitiveComponent;
        Vector socialComponent;
        
    	if(activity < threshold) //repel from personal best and personal worst
    	{
    		cognitiveComponent = Vector.copyOf(localGuide).subtract(position).multiply(cp(cognitiveAcceleration)).multiply(random()).multiply(-1);
    	    socialComponent = Vector.copyOf((Vector) particle.get(Property.WORST_POSITION)).subtract(position).multiply(cp(socialAcceleration)).multiply(random()).multiply(-1);
    	}
    	else 					//standard update
    	{
    		cognitiveComponent = Vector.copyOf(localGuide).subtract(position).multiply(cp(cognitiveAcceleration)).multiply(random());
    	    socialComponent = Vector.copyOf(globalGuide).subtract(position).multiply(cp(socialAcceleration)).multiply(random());
    	}
    	
    	return Vectors.sumOf(dampenedVelocity, cognitiveComponent, socialComponent).valueE("Cannot determine velocity");
    }

    
    private double calculateActivity(Particle particle){
    	int dimension = AbstractAlgorithm.get().getOptimisationProblem().getDomain().getDimension();
    	Vector domain = (Vector) AbstractAlgorithm.get().getOptimisationProblem().getDomain().getBuiltRepresentation();

    	Vector pBest = (Vector) particle.getBestPosition();
    	Vector gBest = (Vector) AbstractAlgorithm.get().getBestSolution().getPosition();
    	
    	double sum = 0;
    	
    	for(int i = 0; i < dimension; i++){
    		double dimLength = domain.get(i).getBounds().getRange();
    		sum += Math.abs((pBest.get(i).doubleValue() - gBest.get(i).doubleValue()) / (dimension * dimLength));
    	}
    	
    	return sum;
    }
    
    private double calculateThreshold(){
    	double percentComplete = AbstractAlgorithm.get().getPercentageComplete();
    	return Math.pow(1 - percentComplete, modulationIndex) * (aInitial - aFinal) + aFinal;
    	//return Math.pow(1 - percentComplete, modulationIndex) * (aFinal - aInitial) + aInitial;
    }
    /**
     * Get the {@linkplain ControlParameter} representing the inertia weight of
     * the {@linkplain VelocityProvider}.
     * @return the inertia component {@linkplain ControlParameter}.
     */
    public ControlParameter getInertiaWeight() {
        return inertiaWeight;
    }

    /**
     * Set the {@linkplain ControlParameter} for the inertia weight of the
     * velocity update equation.
     * @param inertiaWeight The inertiaWeight to set.
     */
    public void setInertiaWeight(ControlParameter inertiaWeight) {
        this.inertiaWeight = inertiaWeight;
    }

    /**
     * Gets the {@linkplain ControlParameter} representing the cognitive
     * component within this {@link VelocityProvider}.
     * @return the cognitiveComponent.
     */
    public ControlParameter getCognitiveAcceleration() {
        return cognitiveAcceleration;
    }

    /**
     * Set the cognitive component {@linkplain ControlParameter}.
     * @param cognitiveComponent The cognitiveComponent to set.
     */
    public void setCognitiveAcceleration(ControlParameter cognitiveComponent) {
        this.cognitiveAcceleration = cognitiveComponent;
    }

    /**
     * Get the {@linkplain ControlParameter} representing the social component
     * of the velocity update equation.
     * @return the socialComponent.
     */
    public ControlParameter getSocialAcceleration() {
        return socialAcceleration;
    }

    /**
     * Set the {@linkplain ControlParameter} for the social component.
     * @param socialComponent The socialComponent to set.
     */
    public void setSocialAcceleration(ControlParameter socialComponent) {
        this.socialAcceleration = socialComponent;
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
}
