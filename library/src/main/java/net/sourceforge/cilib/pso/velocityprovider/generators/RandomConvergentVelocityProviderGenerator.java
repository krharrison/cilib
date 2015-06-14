/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider.generators;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.pso.guideprovider.GuideProvider;
import net.sourceforge.cilib.pso.guideprovider.NBestGuideProvider;
import net.sourceforge.cilib.pso.guideprovider.PBestGuideProvider;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.container.Vector;

public class RandomConvergentVelocityProviderGenerator implements VelocityProviderGenerator {

	private GuideProvider globalGuideProvider;
	private GuideProvider localGuideProvider;
	private ProbabilityDistributionFunction cognitiveDistribution;
	private ProbabilityDistributionFunction socialDistribution;
	private ProbabilityDistributionFunction inertiaDistribution;
	
	public RandomConvergentVelocityProviderGenerator(){
		this.globalGuideProvider = new NBestGuideProvider();
		this.localGuideProvider = new PBestGuideProvider();
		
		//set up the range for the cognitive component
		UniformDistribution cog = new UniformDistribution();
		cog.setLowerBound(ConstantControlParameter.of(1));
		cog.setUpperBound(ConstantControlParameter.of(2));
		this.cognitiveDistribution = cog;
		
		//set up the range for the social component
		UniformDistribution soc = new UniformDistribution();
		soc.setLowerBound(ConstantControlParameter.of(1));
		soc.setUpperBound(ConstantControlParameter.of(2));
		this.socialDistribution = soc;
		
		//set up the range for the inertia component
		UniformDistribution inert = new UniformDistribution();
		inert.setLowerBound(ConstantControlParameter.of(0.1));
		inert.setUpperBound(ConstantControlParameter.of(0.9));
		this.inertiaDistribution = inert;
	}
	
	public RandomConvergentVelocityProviderGenerator(RandomConvergentVelocityProviderGenerator clone){
		this.cognitiveDistribution = clone.cognitiveDistribution;
		this.socialDistribution = clone.socialDistribution;
		this.inertiaDistribution = clone.inertiaDistribution;
		this.globalGuideProvider = clone.globalGuideProvider.getClone();
		this.localGuideProvider = clone.localGuideProvider.getClone();
	}
	
	@Override
	public RandomConvergentVelocityProviderGenerator getClone() {
		return new RandomConvergentVelocityProviderGenerator(this);
	}

	@Override
	public VelocityProvider generate() {
		StandardVelocityProvider provider = new StandardVelocityProvider();
		provider.setLocalGuideProvider(localGuideProvider);
		provider.setGlobalGuideProvider(globalGuideProvider);
		
		double cognitive;
		double social;
		double inertia;
		
		double check; //used to check convergence
		
		do{
			inertia = inertiaDistribution.getRandomNumber();
			cognitive = cognitiveDistribution.getRandomNumber();
			social = socialDistribution.getRandomNumber();
			
			check = (cognitive + social) / 2 - 1;
			//TODO: should ensure that the inertia is between 0 and 1
		} while(check <= 0 && check >= inertia); //repeat if check <= 0 or check >= inertia
		
		
		provider.setCognitiveAcceleration(ConstantControlParameter.of(cognitive));
		provider.setSocialAcceleration(ConstantControlParameter.of(social));
		provider.setInertiaWeight(ConstantControlParameter.of(inertia));
		
		return provider;
	}
	
    /**
     * Sets the GuideProvider responsible for retrieving a particle's global guide.
     * @param globalGuideProvider The guide provider to set.
     */
    public void setGlobalGuideProvider(GuideProvider globalGuideProvider) {
        this.globalGuideProvider = globalGuideProvider;
    }

    /**
     * Sets the GuideProvider responsible for retrieving a particle's local guide.
     * @param localGuideProvider The guide provider to set.
     */
    public void setLocalGuideProvider(GuideProvider localGuideProvider) {
        this.localGuideProvider = localGuideProvider;
    }
    
    public void setCognitiveDistribution(ProbabilityDistributionFunction cognitiveDistribution){
		this.cognitiveDistribution = cognitiveDistribution;
	}
    
    public ProbabilityDistributionFunction getCognitiveDistribution(){
    	return this.cognitiveDistribution;
    }
	
	public void setSocialDistribution(ProbabilityDistributionFunction socialDistribution){
		this.socialDistribution = socialDistribution;
	}
	
    public ProbabilityDistributionFunction getSocialDistribution(){
    	return this.socialDistribution;
    }

	public void setInertiaDistribution(ProbabilityDistributionFunction inertiaDistribution){
		this.inertiaDistribution = inertiaDistribution;
	}
	
    public ProbabilityDistributionFunction getInertiaDistribution(){
    	return this.inertiaDistribution;
    }

}
