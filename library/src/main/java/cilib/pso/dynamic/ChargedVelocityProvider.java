/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.dynamic;

import cilib.algorithm.AbstractAlgorithm;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;
import cilib.pso.velocityprovider.StandardVelocityProvider;
import cilib.pso.velocityprovider.VelocityProvider;
import cilib.type.types.container.Vector;

/**
 * VelocityProvider that the so called Charged PSO makes use of.
 * This is an implementation of the original Charged PSO algorithm
 * developed by Blackwell and Bentley and then further improved by
 * Blackwell and Branke.
 */
public class ChargedVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = 365924556746583124L;

    private VelocityProvider delegate;
    private ControlParameter pCore; // lower limit
    private ControlParameter p; // upper limit

    public ChargedVelocityProvider() {
        this.delegate = new StandardVelocityProvider();
        this.pCore = ConstantControlParameter.of(1);
        this.p = ConstantControlParameter.of(30);
    }

    public ChargedVelocityProvider(ChargedVelocityProvider copy) {
        this.delegate = copy.delegate.getClone();
        this.pCore = copy.pCore.getClone();
        this.p = copy.p.getClone();
    }

    @Override
    public ChargedVelocityProvider getClone() {
        return new ChargedVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector position = (Vector) particle.getPosition();

        PSO pso = (PSO) AbstractAlgorithm.get();

        // Calculate acceleration of the current particle
        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); ++i) {
            double accSum = 0;
            for (Particle other : pso.getNeighbourhood().f(pso.getTopology(), particle)) {
                if (particle == other) {
                    continue;
                }

                double qi = ((ChargedParticle) particle).getCharge();
                double qj = ((ChargedParticle) other).getCharge();
                Vector rij = position.subtract((Vector) other.getPosition());
                double magnitude = rij.norm();

                if (this.pCore.getParameter() <= magnitude && magnitude <= this.p.getParameter()) {
                    accSum += (qi * qj / Math.pow(magnitude, 3)) * rij.doubleValueOf(i);
                }
            }
            builder.add(accSum);
        }

        Vector acceleration = builder.build();

        Vector velocity = (Vector) this.delegate.get(particle);

        return velocity.plus(acceleration);
    }

    public void setDelegate(VelocityProvider delegate) {
        this.delegate = delegate;
    }

    public VelocityProvider getDelegate() {
        return this.delegate;
    }

    /**
     * @return the pCore
     */
    public ControlParameter getPCore() {
        return this.pCore;
    }

    /**
     * @param core the pCore to set
     */
    public void setPCore(ControlParameter core) {
        this.pCore = core;
    }

    /**
     * @return the p
     */
    public ControlParameter getP() {
        return this.p;
    }

    /**
     * @param p the p to set
     */
    public void setP(ControlParameter p) {
        this.p = p;
    }
}
