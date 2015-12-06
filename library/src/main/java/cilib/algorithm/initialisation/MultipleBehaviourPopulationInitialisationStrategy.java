/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 *//*

package cilib.algorithm.initialisation;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.math.random.generator.Rand;
import cilib.pso.velocityprovider.generators.RandomDivergentVelocityProviderGenerator;
import com.google.common.base.Preconditions;

import cilib.entity.Entity;
import cilib.entity.behaviour.generator.BehaviourGenerator;
import cilib.problem.Problem;
import cilib.pso.behaviour.generators.StandardVelocityProviderBehaviourGenerator;
import cilib.pso.particle.Particle;

*/
/**
 * Generate a heterogeneous population using a {@link PopulationInitialisationStrategy}. Each entities behaviour
 * is uniquely generated, from one of two {@link BehaviourGenerator}, rather than provided
 * as part of the definition.
 *//*

public class MultipleBehaviourPopulationInitialisationStrategy implements PopulationInitialisationStrategy {

    private BehaviourGenerator behaviour1Generator;
    private BehaviourGenerator behaviour2Generator;
    private ControlParameter behaviour1Ratio;
    private PopulationInitialisationStrategy delegate;
    */
/**
     * Create an instance of the {@code BehaviourGeneratorPopulationInitialisationStrategy}.
     *//*

    public MultipleBehaviourPopulationInitialisationStrategy(){
        behaviour1Generator = new StandardVelocityProviderBehaviourGenerator();
        StandardVelocityProviderBehaviourGenerator gen = new StandardVelocityProviderBehaviourGenerator();
        gen.setVelocityProviderGenerator(new RandomDivergentVelocityProviderGenerator());
        behaviour2Generator = gen;
        behaviour1Ratio = ConstantControlParameter.of(0.5);
        delegate = new ClonedPopulationInitialisationStrategy();
    }

    */
/**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     *//*

    public MultipleBehaviourPopulationInitialisationStrategy(MultipleBehaviourPopulationInitialisationStrategy copy){
        this.behaviour1Generator = copy.behaviour1Generator.getClone();
        this.behaviour2Generator = copy.behaviour2Generator.getClone();
        this.behaviour1Ratio = copy.behaviour1Ratio.getClone();
        this.delegate = copy.delegate.getClone();
    }

    */
/**
     * {@inheritDoc}
     *//*

    @Override
    public MultipleBehaviourPopulationInitialisationStrategy getClone() {
        return new MultipleBehaviourPopulationInitialisationStrategy(this);
    }

    */
/**
     * Set the prototype {@link Entity} for the copy process.
     * @param entityType The {@link Entity} to use for the cloning process. This must be a {@link Particle}.
     *//*

    @Override
    public void setEntityType(Entity entityType) {
        delegate.setEntityType(entityType);
    }

    */
/**
     * Get the {@link Entity} that has been defined as the prototype to copy.
     *
     * @return The prototype {@linkplain Entity}.
     *//*

    @Override
    public Entity getEntityType() {
        return delegate.getEntityType();
    }

    */
/**
     * Get the defined number of {@code Entity} instances to create.
     * @return The number of {@code Entity} instances.
     *//*

    @Override
    public int getEntityNumber() {
        return delegate.getEntityNumber();
    }

    */
/**
     * Set the number of {@code Entity} instances to clone.
     * @param entityNumber The number to clone.
     *//*

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

    public void setBehaviour1Generator(BehaviourGenerator generator){
        this.behaviour1Generator = generator;
    }

    public BehaviourGenerator getBehaviour1Generator(){
        return this.behaviour1Generator;
    }

    public void setBehaviour2Generator(BehaviourGenerator generator){
        this.behaviour2Generator = generator;
    }

    public BehaviourGenerator getBehaviour2Generator(){
        return this.behaviour2Generator;
    }

    */
/**
     * @return the chargedRatio
     *//*

    public ControlParameter getBehaviour1Ratio() {
        return behaviour1Ratio;
    }

    */
/**
     * @param behaviour1Ratio the chargedRatio to set
     *//*

    public void setBehaviour1Ratio(ControlParameter behaviour1Ratio) {
        this.behaviour1Ratio = behaviour1Ratio;
    }

    */
/**
     * Perform the required initialisation, using the provided <tt>Problem</tt>.
     * @param problem The <tt>Problem</tt> to use in the initialisation of the topology.
     * @return An {@code Iterable<E>} of cloned instances.
     *//*

    @Override
    public <E extends Entity> Iterable<E> initialise(Problem problem) {
        Preconditions.checkNotNull(problem, "No problem has been specified");

        Iterable<Entity> clones = delegate.initialise(problem);

        int behaviour1Count = 0;
        int behaviour2Count = 0;
        for (Entity p : clones) {
            double rand = Rand.nextDouble();
            // makes sure the different behaviours are randomly positioned across the topology
            if (behaviour1Count < Math.floor(delegate.getEntityNumber() * behaviour1Ratio.getParameter()) && rand < behaviour1Ratio.getParameter()) {
                p.setBehaviour(behaviour1Generator.generate());
                behaviour1Count++;
            }
            else if(behaviour2Count >= Math.floor(delegate.getEntityNumber() * (1.0 - behaviour1Ratio.getParameter()))){
                p.setBehaviour(behaviour1Generator.generate());
                behaviour1Count++;
            }
            else {
                p.setBehaviour(behaviour2Generator.generate());
                behaviour2Count++;
            }
        }

        return (Iterable<E>) clones;
    }

}*/
