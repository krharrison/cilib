/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.positionprovider;

import fj.P1;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.entity.Property;
import cilib.math.random.UniformDistribution;
import cilib.problem.solution.Fitness;
import cilib.pso.particle.Particle;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;

public class VectorBasedPositionProvider implements PositionProvider {

    private ControlParameter granularity;
    private PositionProvider delegate;

    public VectorBasedPositionProvider() {
        this.granularity = ConstantControlParameter.of(0.5);
        this.delegate = new StandardPositionProvider();
    }

    public VectorBasedPositionProvider(VectorBasedPositionProvider copy) {
        this.granularity = copy.granularity.getClone();
        this.delegate = copy.delegate.getClone();
    }

    @Override
    public PositionProvider getClone() {
        return new VectorBasedPositionProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector newPos = (Vector) delegate.get(particle);

        Particle tmp = particle.getClone();
        tmp.setPosition(newPos);
        Fitness newFitness = particle.getBehaviour().getFitnessCalculator().getFitness(tmp);

        final UniformDistribution uniform = new UniformDistribution();
        Vector newPBest = newPos.plus(Vector.newBuilder().repeat(newPos.size(), Real.valueOf(1.0)).build().multiply(new P1<Number>() {
                    @Override
                    public Number _1() {
                        return uniform.getRandomNumber(-granularity.getParameter(), granularity.getParameter());
                    }
                }));
        tmp.setPosition(newPos);
        Fitness newPBestFitness = particle.getBehaviour().getFitnessCalculator().getFitness(tmp);

        if (newPBestFitness.compareTo(newFitness) < 0) {
            Vector tmpVector = Vector.copyOf(newPos);
            newPos = newPBest;
            newPBest = tmpVector;

            newPBestFitness = newFitness;
        }

        double dot = ((Vector) particle.getNeighbourhoodBest().getBestPosition())
                .subtract(newPos).dot(newPBest.subtract(newPos));

        if (dot < 0) {
            return (Vector) particle.getPosition();
        }

        particle.put(Property.BEST_POSITION, newPBest);
        particle.put(Property.BEST_FITNESS, newPBestFitness);

        return newPos;
    }

    public void setDelegate(PositionProvider delegate) {
        this.delegate = delegate;
    }

    public PositionProvider getDelegate() {
        return delegate;
    }

    public void setGranularity(ControlParameter granularity) {
        this.granularity = granularity;
    }

    public ControlParameter getGranularity() {
        return granularity;
    }
}
