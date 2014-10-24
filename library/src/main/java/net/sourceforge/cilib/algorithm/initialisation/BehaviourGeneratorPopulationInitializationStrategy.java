/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.initialisation;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.behaviour.Behaviour;
import net.sourceforge.cilib.entity.behaviour.generator.BehaviourGenerator;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.pso.behaviour.generators.StandardVelocityProviderBehaviourGenerator;

/**
 * Generate a population using a {@link HeterogeneousPopulationInitialisationStrategy}
 * with a behaviour pool that is generated by a {@link BehaviourGenerator} rather than provided
 * as part of the definition.
 */
public class BehaviourGeneratorPopulationInitializationStrategy implements PopulationInitialisationStrategy {

	private int poolSize;
	private BehaviourGenerator behaviourGenerator;
	private HeterogeneousPopulationInitialisationStrategy delegate;
	
    /**
     * Create an instance of the {@code BehaviourGeneratorPopulationInitialisationStrategy}.
     */
	public BehaviourGeneratorPopulationInitializationStrategy(){
		poolSize = 25;
		behaviourGenerator = new StandardVelocityProviderBehaviourGenerator(); 
		delegate = new HeterogeneousPopulationInitialisationStrategy();
	}
	
    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
	public BehaviourGeneratorPopulationInitializationStrategy(BehaviourGeneratorPopulationInitializationStrategy copy){
		this.poolSize = copy.poolSize;
		this.behaviourGenerator = copy.behaviourGenerator.getClone();
		this.delegate = copy.delegate.getClone();
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
	public BehaviourGeneratorPopulationInitializationStrategy getClone() {
		return new BehaviourGeneratorPopulationInitializationStrategy(this);
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

    public void setDelegate(HeterogeneousPopulationInitialisationStrategy delegate) {
        this.delegate = delegate;
    }
    
    public void setPoolSize(int poolSize){
    	this.poolSize = poolSize;
    }
    
    public HeterogeneousPopulationInitialisationStrategy getDelegate(){
    	return delegate;
    }
    
    public void setBehaviourGenerator(BehaviourGenerator generator){
    	this.behaviourGenerator = generator;
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
        Preconditions.checkState(poolSize > 0, "Must have a pool size of at least 1.");
        
        //generate a behavior pool to use for the heterogeneous initialization
        for(int i = 0 ; i < poolSize; i++){
        	delegate.addBehavior(behaviourGenerator.generate());
        }

        Iterable<Entity> clones = delegate.initialise(problem);

        return (Iterable<E>) clones;
    }

}
