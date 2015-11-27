/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.math.random.GaussianDistribution;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.UniformDistribution;
import cilib.pso.PSO;
import cilib.pso.behaviour.StandardParticleBehaviour;
import cilib.pso.dynamic.ChargedParticle;
import cilib.pso.particle.Particle;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.pso.velocityprovider.SelfAdaptiveVelocityProvider;
import cilib.type.types.container.Vector;

/**
 * Update parameters using quantum position update if particle is charged.
 * Otherwise, delegate parameter update to another adaptation strategy.
 *
 * The nucleus is taken as the parameter set of a particle's neighbourhood best
 *
 * TODO: what if parameters exit the convergent region?
 */
public class QuantumAdaptationStrategy implements AdaptationStrategy {

    private static final double EPSILON = 0.000000001;

    private GaussianDistribution normalDistribution;
    private ProbabilityDistributionFunction distribution;
    private ControlParameter radius;
    private Boolean uniform; //true if using uniform distributions

    protected AdaptationStrategy delegate;

    public QuantumAdaptationStrategy(){
        this.normalDistribution = new GaussianDistribution();
        this.radius = ConstantControlParameter.of(0.1);
        this.distribution = new UniformDistribution();
        this.delegate = new RandomRegenerationAdaptationStrategy();
        this.uniform = true;
    }

    public QuantumAdaptationStrategy(QuantumAdaptationStrategy copy){
        this.normalDistribution = copy.normalDistribution;
        this.radius = copy.radius;
        this.distribution = copy.distribution;
        this.delegate = copy.delegate.getClone();
        this.uniform = copy.uniform;
    }

    @Override
    public void adapt(Particle p, PSO algorithm) {

        ChargedParticle checkChargeParticle = (ChargedParticle) p;
        if (checkChargeParticle.getCharge() < EPSILON) { // the particle is neutral
            this.delegate.adapt(p, algorithm);
        } else { // the particle is charged

            //get the nucleus as the parameters of the neighborhood best
            Particle best = p.getNeighbourhoodBest();
            StandardParticleBehaviour bestBehaviour = (StandardParticleBehaviour) best.getBehaviour();
            SelfAdaptiveVelocityProvider bestProvider = (SelfAdaptiveVelocityProvider) bestBehaviour.getVelocityProvider();
            Vector nucleus = bestProvider.getParameterSet().asVector();

            //get the behaviour of the particle
            StandardParticleBehaviour behaviour = (StandardParticleBehaviour) p.getBehaviour();
            SelfAdaptiveVelocityProvider provider = (SelfAdaptiveVelocityProvider) behaviour.getVelocityProvider();

            //only update if new position is convergent, otherwise regenerate
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

                //step 3 - calculate a random number using the provided probability distribution
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

                provider.getParameterSet().fromVector(nucleus.plus(position.multiply(root / distance).multiply(radius.getParameter()).multiply(sign)));

            } while(!provider.getParameterSet().isConvergent());

        }
    }

    @Override
    public QuantumAdaptationStrategy getClone() {
        return new QuantumAdaptationStrategy(this);
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
