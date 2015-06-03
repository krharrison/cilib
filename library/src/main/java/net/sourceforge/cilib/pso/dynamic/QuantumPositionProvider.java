/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.pso.guideprovider.GuideProvider;
import net.sourceforge.cilib.pso.guideprovider.NBestGuideProvider;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.positionprovider.PositionProvider;
import net.sourceforge.cilib.pso.positionprovider.StandardPositionProvider;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * Position provider for QSO (Quantum PSO). Implemented according to paper by
 * Blackwell, Branke, and Li: "Particle Swarms for Dynamic Optimization Problems."
 *
 */
public class QuantumPositionProvider implements PositionProvider {

    private static final long serialVersionUID = -7844226788317206737L;

    private static final double EPSILON = 0.000000001;

    private GaussianDistribution normalDistribution;
    private EuclideanDistanceMeasure distanceMeasure;
    private ControlParameter radius;
    private ProbabilityDistributionFunction distribution;
    private Vector nucleus;

    private PositionProvider delegate;
    private GuideProvider globalGuide;
    private Boolean uniform;

    public QuantumPositionProvider() {
    	this.normalDistribution = new GaussianDistribution();
    	this.distanceMeasure = new EuclideanDistanceMeasure();
        this.radius = ConstantControlParameter.of(5);
        this.distribution = new UniformDistribution();
        this.delegate = new StandardPositionProvider();
        this.globalGuide = new NBestGuideProvider();
        this.uniform = true;
    }

    public QuantumPositionProvider(QuantumPositionProvider copy) {
    	this.normalDistribution = copy.normalDistribution;
    	this.distanceMeasure = copy.distanceMeasure;
        this.radius = copy.radius;
        this.distribution = copy.distribution;
        this.delegate = copy.delegate.getClone();
        this.globalGuide = copy.globalGuide.getClone();
        this.uniform = copy.uniform;
    }

    @Override
    public QuantumPositionProvider getClone() {
        return new QuantumPositionProvider(this);
    }

    /**
     * Update particle position; do it in a standard way if the particle is neutral, and
     * in a quantum way if the particle is charged. The "quantum" way entails sampling the
     * position in a spherical cloud around gbest with a radius r using some probability distribution.
     * @param particle the particle to update position of
     */
    @Override
    public Vector get(Particle particle) {
        ChargedParticle checkChargeParticle = (ChargedParticle) particle;
        if (checkChargeParticle.getCharge() < EPSILON) { // the particle is neutral
            return (Vector) this.delegate.get(particle);
        } else { // the particle is charged

            this.nucleus = (Vector) globalGuide.get(particle);

            //step 1 - create a position which is normally distributed around the nucleus
            Vector.Builder positionBuilder = Vector.newBuilder();
            int dimensions = particle.getDimension();
            for (int i = 0; i < dimensions; i++) {
            	positionBuilder.add(normalDistribution.getRandomNumber());
            }
            
            Vector position = positionBuilder.build();
            
            //step 2 - calculate the distance from the nucleus
            double distance = distanceMeasure.distance(position, this.nucleus);
            
            //step 3 - calculate a random number using the provided probability distribution
            double u = distribution.getRandomNumber();
            
           //special care to avoid negative roots
            double sign = Math.signum(u);
            u = Math.abs(u);
            
            //step 4 - calculate the quantum position
            //using the dth root of u gives a uniform distribution
            //using u gives a non-uniform distribution
            double root = u;
            if(uniform)
            {
            	root = Math.pow(u, 1.0 / dimensions);
            }

            return position.multiply(root / distance).multiply(radius.getParameter()).multiply(sign);
        }
    }

    public void setDelegate(PositionProvider delegate) {
        this.delegate = delegate;
    }

    public PositionProvider getDelegate() {
        return this.delegate;
    }

    public Vector getNucleus() {
        return this.nucleus;
    }

    public void setNucleus(Vector nucleus) {
        this.nucleus = nucleus;
    }

    /**
     * @return the radius
     */
    public ControlParameter getRadius() {
        return this.radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(ControlParameter radius) {
        //Preconditions.checkArgument(radius.getParameter() >= 0, "Radius of the electron cloud can not be negative");
        this.radius = radius;
    }
    
    public void setDistribution(ProbabilityDistributionFunction distribution){
    	this.distribution = distribution;
    }
    
    public ProbabilityDistributionFunction getDistribution(){
    	return this.distribution;
    }
    
    public void setUniform(Boolean uniform){
    	this.uniform = uniform;
    }
    
    public Boolean getUniform(){
    	return this.uniform;
    }
}
