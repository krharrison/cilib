package cilib.pso.velocityprovider;

import cilib.pso.particle.Particle;
import cilib.type.types.container.StructuredType;

/**
 * A velocity provider which directly uses the velocity stored in the particle's properties
 */
public class DirectVelocityProvider implements VelocityProvider{
    @Override
    public VelocityProvider getClone() {
        return this;
    }

    @Override
    public StructuredType get(Particle particle) {
        return particle.getVelocity();
    }
}
