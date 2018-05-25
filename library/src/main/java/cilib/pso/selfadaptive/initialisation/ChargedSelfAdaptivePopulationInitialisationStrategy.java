/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.initialisation;

import cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.entity.Entity;
import cilib.math.random.generator.Rand;
import cilib.problem.Problem;
import cilib.pso.dynamic.ChargedParticle;
import cilib.pso.particle.SelfAdaptiveParticle;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Create a collection of {@linkplain Entity entities} by cloning the given
 * prototype {@link Entity}.
 * <p>
 * The entity has to be a {@link SelfAdaptiveParticle}. Their charges are set during
 * the initialisation process.
 */
public class ChargedSelfAdaptivePopulationInitialisationStrategy implements PopulationInitialisationStrategy {

    private SelfAdaptiveParticle prototypeEntity;
    private ControlParameter entityNumber;
    private ControlParameter chargedRatio; // determines the percentage of the swarm that is to be charged
    private ControlParameter chargeMagnitude; // charge magnitude

    /**
     * Create an instance of the {@code ChargedPopulationInitialisationStrategy}.
     */
    public ChargedSelfAdaptivePopulationInitialisationStrategy() {
        entityNumber = ConstantControlParameter.of(20);
        prototypeEntity = null; // This has to be manually set as Individuals are used in PSO etc...
        chargedRatio = ConstantControlParameter.of(0.5);    // one half of the swarm is charged => Atomic swarm
        chargeMagnitude = ConstantControlParameter.of(16); // the obscure value 16 comes from the article where the charged PSO was analysed for the 1st time by its creators
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * <p/>
     * @param copy The instance to copy.
     */
    public ChargedSelfAdaptivePopulationInitialisationStrategy(ChargedSelfAdaptivePopulationInitialisationStrategy copy) {
        this.entityNumber = copy.entityNumber;
        this.chargedRatio = copy.chargedRatio;
        this.chargeMagnitude = copy.chargeMagnitude;

        if (prototypeEntity != null) {
            this.prototypeEntity = copy.prototypeEntity.getClone();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChargedSelfAdaptivePopulationInitialisationStrategy getClone() {
        return new ChargedSelfAdaptivePopulationInitialisationStrategy(this);
    }

    /**
     * Perform the required initialisation, using the provided <tt>Topology</tt> and
     * <tt>Problem</tt>.
     * @param problem The <tt>Problem</tt> to use in the initialisation of the topology.
     * @return An {@code Iterable<E>} of cloned instances.
     */
    @Override
    public <E extends Entity> Iterable<E> initialise(Problem problem) {
        Preconditions.checkNotNull(problem, "No problem has been specified");
        Preconditions.checkNotNull(prototypeEntity, "No prototype Entity object has been defined for the clone operation in the entity construction process.");

        List<E> clones = new ArrayList<>();
        int chargedCounter = 0;
        int neutralCounter = 0;

        for (int i = 0; i < entityNumber.getParameter(); ++i) {
            E entity = (E) prototypeEntity.getClone();
            double rand = Rand.nextDouble();

            // makes sure the particles are randomly positioned across the topology
            if (chargedCounter < Math.floor(entityNumber.getParameter() * chargedRatio.getParameter()) && rand < chargedRatio.getParameter()) {
                ((SelfAdaptiveParticle) entity).setCharge(chargeMagnitude.getParameter());
                ++chargedCounter;
            } else if (neutralCounter >= Math.floor(entityNumber.getParameter() * (1.0 - chargedRatio.getParameter()))) {
                ((SelfAdaptiveParticle) entity).setCharge(chargeMagnitude.getParameter());
                ++chargedCounter;
            } else {
                ((SelfAdaptiveParticle) entity).setCharge(0);
                ++neutralCounter;
            }
            entity.initialise(problem);
            clones.add(entity);
        }

        return clones;
    }

    /**
     * Set the prototype {@link  Entity} for the copy process.
     *
     * @param entityType    The {@linkplain Entity} to use for the cloning
     *                      process. This must be a {@linkplain SelfAdaptiveParticle}.
     */
    @Override
    public void setEntityType(Entity entityType) {
        try {
            this.prototypeEntity = (SelfAdaptiveParticle) entityType;
        } catch (ClassCastException e) {
            throw new UnsupportedOperationException("The entityType of ChargedSelfAdaptivePopulationInitialisationStrategy must be a SelfAdaptiveParticle.", e);
        }
    }

    /**
     * Get the {@link Entity} that has been defined as the prototype to copy.
     *
     * @return The prototype {@linkplain Entity}.
     */
    @Override
    public Entity getEntityType() {
        return this.prototypeEntity;
    }

    /**
     * Get the defined number of {@code Entity} instances to create.
     * @return The number of {@code Entity} instances.
     */
    @Override
    public int getEntityNumber() {
        return (int) this.entityNumber.getParameter();
    }

    /**
     * Set the number of {@code Entity} instances to clone.
     * @param entityNumber The number to clone.
     */
    @Override
    public void setEntityNumber(int entityNumber) {
        this.entityNumber = ConstantControlParameter.of(entityNumber);
    }

    /**
     * Set the number of {@code Entity} instances to clone.
     * @param entityNumber The number to clone.
     */
    public void setEntityNumberControlParameter(ControlParameter entityNumber) {
        this.entityNumber = entityNumber;
    }

    /**
     * @return the chargedRatio
     */
    public ControlParameter getChargedRatio() {
        return chargedRatio;
    }

    /**
     * @param chargedRatio the chargedRatio to set
     */
    public void setChargedRatio(ControlParameter chargedRatio) {
        this.chargedRatio = chargedRatio;
    }

    /**
     * @return the chargeMagnitude
     */
    public ControlParameter getChargeMagnitude() {
        return chargeMagnitude;
    }

    /**
     * @param chargeMagnitude the chargeMagnitude to set
     */
    public void setChargeMagnitude(ControlParameter chargeMagnitude) {
        this.chargeMagnitude = chargeMagnitude;
    }
}
