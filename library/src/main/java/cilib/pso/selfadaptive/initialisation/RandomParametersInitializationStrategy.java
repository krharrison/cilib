package cilib.pso.selfadaptive.initialisation;

import cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import cilib.algorithm.initialisation.PersonalizedParticleParametersInitializationStrategy;
import cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import cilib.entity.Entity;
import cilib.problem.Problem;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.pso.selfadaptive.parametersetgenerator.ConvergentParameterSetGenerator;
import cilib.pso.selfadaptive.parametersetgenerator.ParameterSetGenerator;
import com.google.common.base.Preconditions;


/**
 * Construct an initial population with random parameters
 */
public class RandomParametersInitializationStrategy implements PopulationInitialisationStrategy {
    private ParameterSetGenerator parameterGenerator;
    private PopulationInitialisationStrategy delegate;
    /**
     * Create an instance of the {@code RandomParametersInitializationStrategy}.
     */
    public RandomParametersInitializationStrategy(){
        parameterGenerator = new ConvergentParameterSetGenerator();
        delegate = new ClonedPopulationInitialisationStrategy();
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public RandomParametersInitializationStrategy(RandomParametersInitializationStrategy copy){
        this.parameterGenerator = copy.parameterGenerator.getClone();
        this.delegate = copy.delegate.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomParametersInitializationStrategy getClone() {
        return new RandomParametersInitializationStrategy(this);
    }

    /**
     * Set the prototype {@link Entity} for the copy process.
     * @param entityType The {@link Entity} to use for the cloning process. This must be a {@link Particle}.
     */
    @Override
    public void setEntityType(Entity entityType) {
        delegate.setEntityType(entityType);
    }

    /**
     * Get the {@link Entity} that has been defined as the prototype to copy.
     *
     * @return The prototype {@linkplain Entity}.
     */
    @Override
    public Entity getEntityType() {
        return delegate.getEntityType();
    }

    /**
     * Get the defined number of {@code Entity} instances to create.
     * @return The number of {@code Entity} instances.
     */
    @Override
    public int getEntityNumber() {
        return delegate.getEntityNumber();
    }

    /**
     * Set the number of {@code Entity} instances to clone.
     * @param entityNumber The number to clone.
     */
    @Override
    public void setEntityNumber(int entityNumber) {
        delegate.setEntityNumber(entityNumber);
    }

    public void setDelegate(PopulationInitialisationStrategy delegate) {
        this.delegate = delegate;
    }

    public PopulationInitialisationStrategy getDelegate(){
        return delegate;
    }

    public void setParameterGenerator(ParameterSetGenerator generator){
        this.parameterGenerator = generator;
    }

    public ParameterSetGenerator getParameterGenerator(){
        return this.parameterGenerator;
    }

    /**
     * Perform the required initialisation, using the provided <tt>Problem</tt>.
     * @param problem The <tt>Problem</tt> to use in the initialisation of the topology.
     * @return An {@code Iterable<E>} of cloned instances.
     */
    @Override
    public <E extends Entity> Iterable<E> initialise(Problem problem) {
        Preconditions.checkNotNull(problem, "No problem has been specified");

        Iterable<Entity> clones = delegate.initialise(problem);

        ParameterSet params = parameterGenerator.generate();

        for (Entity e : clones) {
            SelfAdaptiveParticle p = (SelfAdaptiveParticle) e;
            p.setParameterSet(params);
        }

        return (Iterable<E>) clones;
    }

}
