/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies.particle;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.math.random.GaussianDistribution;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.UniformDistribution;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.type.types.container.Vector;

/**
 * Update parameters using quantum position update if particle is charged.
 * Otherwise, delegate parameter update to another adaptation strategy.
 *
 * The nucleus is taken as the parameter set of a particle's neighbourhood best
 *
 */
public class QuantumParticleAdaptationStrategy implements ParticleAdaptationStrategy {

    private static final double EPSILON = 0.000000001;

    private GaussianDistribution normalDistribution;
    private ProbabilityDistributionFunction distribution;
    private ControlParameter radius;
    private Boolean uniform; //true if using uniform distributions

    protected ParticleAdaptationStrategy delegate;

    public QuantumParticleAdaptationStrategy(){
        this.normalDistribution = new GaussianDistribution();
        this.radius = ConstantControlParameter.of(0.1);
        this.distribution = new UniformDistribution();
        this.delegate = new RandomRegenerationParticleAdaptationStrategy();
        this.uniform = true;
    }

    public QuantumParticleAdaptationStrategy(QuantumParticleAdaptationStrategy copy){
        this.normalDistribution = copy.normalDistribution;
        this.radius = copy.radius;
        this.distribution = copy.distribution;
        this.delegate = copy.delegate.getClone();
        this.uniform = copy.uniform;
    }

    @Override
    public void adapt(Particle p, PSO algorithm) {

        SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
        if (sp.getCharge() < EPSILON) { // the particle is neutral
            this.delegate.adapt(p, algorithm);
        } else { // the particle is charged
            //get the nucleus as the parameters of the neighborhood best
            SelfAdaptiveParticle best = (SelfAdaptiveParticle) sp.getNeighbourhoodBest();
            Vector nucleus = best.getParameterSet().asVector();

            //only update if new position adheres to the PSO-iRC criteria, otherwise regenerate
            do {
                //step 1 - create a point using a normal distribution
                Vector.Builder positionBuilder = Vector.newBuilder();
                int dimensions = 3;
                for (int i = 0; i < dimensions; i++) {
                    positionBuilder.add(normalDistribution.getRandomNumber());
                }

                Vector position = positionBuilder.build();

                //step 2 - calculate the distance of the point from from the origin
                double distance = position.length();

                //step 3 - calculate a random number using the provided weight distribution
                double u = distribution.getRandomNumber();

                //special care to avoid negative roots
                double sign = Math.signum(u);
                u = Math.abs(u);

                //step 4 - calculate the quantum position
                //using the dth root of u gives a uniform distribution
                //using u gives a non-uniform distribution
                double root = u;
                if (uniform) {
                    root = Math.pow(u, 1.0 / dimensions);
                }

                //update parameters by adding the offset to the nucleus
                sp.getParameterSet().fromVector(nucleus.plus(position.multiply(root / distance).multiply(radius.getParameter()).multiply(sign)));

            } while(!check(sp.getParameterSet()));

        }
    }

    /**
     * Check whether the given parameter set adheres to the PSO-iRC criteria.
     * @param params
     * @return
     */
    private boolean check(ParameterSet params){
        double w = params.getInertiaWeight().getParameter();
        double c1 = params.getCognitiveAcceleration().getParameter();
        double c2 = params.getSocialAcceleration().getParameter();

        double upper = (24 * (1 - w * w)) / (7 - 5 * w);
        double lower = (22 - 30 * w * w) / (7 - 5 * w);
        double c = c1 + c2;

        return c > lower && c < upper;
    }

    @Override
    public QuantumParticleAdaptationStrategy getClone() {
        return new QuantumParticleAdaptationStrategy(this);
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

    public void setDelegate(ParticleAdaptationStrategy strategy){
        this.delegate = strategy;
    }
}
