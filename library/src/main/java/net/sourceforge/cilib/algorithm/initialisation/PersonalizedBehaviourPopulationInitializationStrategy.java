/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.initialisation;

import com.google.common.base.Preconditions;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.behaviour.generator.BehaviourGenerator;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.pso.behaviour.generators.StandardVelocityProviderBehaviourGenerator;

/**
 * Generate a population using a {@link PopulationInitialisationStrategy}. Each entities behaviour
 * is uniquely generated using a {@link BehaviourGenerator} rather than provided
 * as part of the definition.
 */
public class PersonalizedBehaviourPopulationInitializationStrategy implements PopulationInitialisationStrategy {

	private BehaviourGenerator behaviourGenerator;
	private PopulationInitialisationStrategy delegate;
    /**
     * Create an instance of the {@code BehaviourGeneratorPopulationInitialisationStrategy}.
     */
	public PersonalizedBehaviourPopulationInitializationStrategy(){
		behaviourGenerator = new StandardVelocityProviderBehaviourGenerator(); 
		delegate = new ClonedPopulationInitialisationStrategy();
	}
	
    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
	public PersonalizedBehaviourPopulationInitializationStrategy(PersonalizedBehaviourPopulationInitializationStrategy copy){
		this.behaviourGenerator = copy.behaviourGenerator.getClone();
		this.delegate = copy.delegate.getClone();
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
	public PersonalizedBehaviourPopulationInitializationStrategy getClone() {
		return new PersonalizedBehaviourPopulationInitializationStrategy(this);
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
    
    public void setBehaviourGenerator(BehaviourGenerator generator){
    	this.behaviourGenerator = generator;
    }
    
    public BehaviourGenerator getBehaviourGenerator(){
    	return this.behaviourGenerator;
    }

    /**
     * Perform the required initialisation, using the provided <tt>Problem</tt>.
     * @param problem The <tt>Problem</tt> to use in the initialisation of the topology.
     * @return An {@code Iterable<E>} of cloned instances.
     * @throws InitialisationException if the initialisation cannot take place.
     */
    @Override
    public <E extends Entity> Iterable<E> initialise(Problem problem) {
        Preconditions.checkNotNull(problem, "No problem has been specified");
        
        Iterable<Entity> clones = delegate.initialise(problem);
        
        for (Entity p : clones) {
            p.setBehaviour(behaviourGenerator.generate());
        }

        return (Iterable<E>) clones;
    }

}
