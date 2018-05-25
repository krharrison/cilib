/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.entity;

import cilib.problem.solution.Fitness;
import cilib.pso.hpso.AdaptiveLearningIterationStrategy;
import cilib.type.types.Int;
import cilib.type.types.Real;
import cilib.type.types.Type;
import cilib.type.types.container.StructuredType;
import cilib.type.types.container.TypeList;

/**
* The defined types for all properties within {@linkplain Entity} objects.
*/
public class Property<T extends Type> {
       
    /**
     * Common properties
     */
    public final static Property<StructuredType> CANDIDATE_SOLUTION = new Property();
    public final static Property<StructuredType> PREVIOUS_SOLUTION = new Property();
    public final static Property<Fitness> FITNESS = new Property();
    public final static Property<Fitness> PREVIOUS_FITNESS = new Property();
    
    /**
     * Individual properties
     */
    public final static Property<StructuredType> STRATEGY_PARAMETERS = new Property();
    
    /**
     * Coevolution properties
     */
    public final static Property<Int> POPULATION_ID = new Property();
    
    /**
     * Memory-based properties
     */
    public final static Property<StructuredType> BEST_POSITION = new Property();
    public final static Property<Fitness> BEST_FITNESS = new Property();
    public final static Property<StructuredType> WORST_POSITION = new Property();
    public final static Property<Fitness> WORST_FITNESS = new Property();
    
    /**
     * Particle properties
     */
    public final static Property<StructuredType> VELOCITY = new Property();
    public final static Property<Int> PBEST_STAGNATION_COUNTER = new Property();
    public final static Property<Int> POSITION_UPDATE_COUNTER = new Property();
    
    /**
     * Quantum properties
     */
    public final static Property<Real> CHARGE = new Property();
    
    /**
     * Special case properties
     */
    public final static Property<AdaptiveLearningIterationStrategy.ParticleProperties> ADAPTIVE_LEARNING_PROPERTIES = new Property();
    public final static Property<StructuredType> V0 = new Property();
    public final static Property<StructuredType> V1 = new Property();
    public final static Property<StructuredType> PREVIOUS_PARAMETERS = new Property();
    public final static Property<Real> DISTANCE_TO_BEST = new Property();
    
    /**
     * Niching properties
     */
    public final static Property<TypeList> NICHE_DETECTION_FITNESSES = new Property();

}
