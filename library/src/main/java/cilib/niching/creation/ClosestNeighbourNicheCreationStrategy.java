/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.niching.creation;

import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.LinearlyVaryingControlParameter;
import cilib.controlparameter.UpdateOnIterationControlParameter;
import cilib.entity.Entity;
import cilib.entity.visitor.ClosestEntityVisitor;
import cilib.measurement.generic.Iterations;
import cilib.niching.NichingSwarms;
import cilib.problem.boundaryconstraint.ClampingBoundaryConstraint;
import cilib.pso.PSO;
import cilib.pso.behaviour.StandardParticleBehaviour;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.velocityprovider.ClampingVelocityProvider;
import cilib.pso.velocityprovider.GCVelocityProvider;
import cilib.pso.velocityprovider.StandardVelocityProvider;
import cilib.stoppingcondition.Maximum;
import cilib.stoppingcondition.MeasuredStoppingCondition;
import fj.Equal;
import fj.F;

/**
 * <p>
 * Create a set of niching locations, based on a provided set of identified
 * niching entities.
 * </p>
 * <p>
 * For each newly discovered niching location, a new sub-swarmType is creates that will
 * maintain the niche. For the case of the PSO, the niching particle and the closest
 * particle to the identified particle are grouped into a niche. Sub-swarms will always
 * then have at least two particles.
 * </p>
 * <p>
 * The rational for two particles is that a particle is a social entity and as a result
 * needs to share information. Ensuring that there are at least two particles within
 * a sub-swarmType will enable the velocity update equation associated with the particle
 * to still operate.
 * </p>
 */
public class ClosestNeighbourNicheCreationStrategy extends NicheCreationStrategy {

    /**
     * Default constructor.
     */
    public ClosestNeighbourNicheCreationStrategy() {
        this.swarmType = new PSO();
        ((SynchronousIterationStrategy) ((PSO) this.swarmType).getIterationStrategy()).setBoundaryConstraint(new ClampingBoundaryConstraint());
        this.swarmType.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 500));

        ClampingVelocityProvider delegate = new ClampingVelocityProvider(ConstantControlParameter.of(1.0),
                new StandardVelocityProvider(new UpdateOnIterationControlParameter(new LinearlyVaryingControlParameter(0.7, 0.2)),
                    ConstantControlParameter.of(1.2), ConstantControlParameter.of(1.2)));

        GCVelocityProvider gcVelocityProvider = new GCVelocityProvider();
        gcVelocityProvider.setDelegate(delegate);
        gcVelocityProvider.setRho(ConstantControlParameter.of(0.01));

        this.swarmBehavior = new StandardParticleBehaviour();
        ((StandardParticleBehaviour) this.swarmBehavior).setVelocityProvider(gcVelocityProvider);
    }

    @Override
    public NichingSwarms f(NichingSwarms a, final Entity b) {
        //There should be at least two particles
        fj.data.List<Entity> t = a.getMainSwarm().getTopology();
        if (a.getMainSwarm().getTopology().length() <= 1 || !t.exists(Equal.<Entity>anyEqual().eq(b))) {
            return a;
        }

        // Get closest particle
        ClosestEntityVisitor<Particle> closestEntityVisitor = new ClosestEntityVisitor<>();
        closestEntityVisitor.setTargetEntity((Particle) b);

        // Clone particles
        Particle nicheMainParticle = (Particle) b.getClone();
        final Particle nicheClosestParticle = closestEntityVisitor.f(a.getMainSwarm().getTopology());

        // Set behavior and nBest
        nicheMainParticle.setNeighbourhoodBest(nicheMainParticle);
        nicheClosestParticle.setNeighbourhoodBest(nicheMainParticle);

        nicheMainParticle.setBehaviour(swarmBehavior.getClone());
        nicheClosestParticle.setBehaviour(swarmBehavior.getClone());

        // Create new subswarm
        SinglePopulationBasedAlgorithm newSubSwarm = swarmType.getClone();
        newSubSwarm.setOptimisationProblem(a.getMainSwarm().getOptimisationProblem());
        newSubSwarm.setTopology(fj.data.List.list(nicheMainParticle, nicheClosestParticle));

        // Create new mainswarm
        SinglePopulationBasedAlgorithm newMainSwarm = a.getMainSwarm().getClone();
        fj.data.List<Entity> local = a.getMainSwarm().getTopology();
        newMainSwarm.setTopology(local.filter(new F<Entity, Boolean>() {
           @Override
            public Boolean f(Entity e) {
               return !e.equals(b) && !e.equals(nicheClosestParticle);
           }
        }));

        return NichingSwarms.of(newMainSwarm, a.getSubswarms().cons(newSubSwarm));
    }
}
