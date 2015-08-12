/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.initialisation;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;

public class ExtendedParticleInitialisationStrategy implements PopulationInitialisationStrategy{

	protected PopulationInitialisationStrategy delegate;
	protected DomainRegistry parameterDomainRegistry;

	public ExtendedParticleInitialisationStrategy(){
		parameterDomainRegistry = new StringBasedDomainRegistry();
		delegate = new ClonedPopulationInitialisationStrategy();
	}
	
	public ExtendedParticleInitialisationStrategy(ExtendedParticleInitialisationStrategy copy){
		this.delegate = copy.delegate.getClone();
		this.parameterDomainRegistry = copy.parameterDomainRegistry.getClone();
	}
	
	@Override
	public ExtendedParticleInitialisationStrategy getClone() {
		return new ExtendedParticleInitialisationStrategy(this);
	}

	@Override
	public void setEntityType(Entity entity) {
		delegate.setEntityType(entity);
		
	}

	@Override
	public Entity getEntityType() {
		return delegate.getEntityType();
	}

	@Override
	public <E extends Entity> Iterable<E> initialise(Problem problem) {
		problem.getDomain().setDomainString(parameterDomainRegistry.getDomainString() + "," + problem.getDomain().getDomainString());
		
		return delegate.initialise(problem);
	}

	@Override
	public int getEntityNumber() {
		return delegate.getEntityNumber();
	}

	@Override
	public void setEntityNumber(int entityNumber) {
		delegate.setEntityNumber(entityNumber);
	}

    public void setParameterDomain(String domain) {
        this.parameterDomainRegistry.setDomainString(domain);
    } 
	
}
