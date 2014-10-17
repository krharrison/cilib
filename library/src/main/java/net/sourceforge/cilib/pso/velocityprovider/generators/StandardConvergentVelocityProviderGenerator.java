/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider.generators;

import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.pso.guideprovider.GuideProvider;
import net.sourceforge.cilib.pso.guideprovider.NBestGuideProvider;
import net.sourceforge.cilib.pso.guideprovider.PBestGuideProvider;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;

/**
 * Generates {@link StandardParticleVelocity} velocity providers with parameters
 * that are guaranteed to be convergent. Parameters are generated randomly according to
 * respective probability distributions.
 */
public class StandardConvergentVelocityProviderGenerator implements VelocityProviderGenerator {

	private ProbabilityDistributionFunction cognitiveDistribution;
	private ProbabilityDistributionFunction socialDistribution;
	private ProbabilityDistributionFunction inertiaDistribution;
	
	private GuideProvider localGuideProvider;
	private GuideProvider globalGuideProvider;

	
	public StandardConvergentVelocityProviderGenerator(){
		
		cognitiveDistribution = new GaussianDistribution();
		GaussianDistribution temp = (GaussianDistribution) cognitiveDistribution;
		temp.setMean(ConstantControlParameter.of(1.496180));
		temp.setDeviation(ConstantControlParameter.of(0.25));
		
		socialDistribution = new GaussianDistribution();
		temp = (GaussianDistribution) socialDistribution;
		temp.setMean(ConstantControlParameter.of(1.496180));
		temp.setDeviation(ConstantControlParameter.of(0.25));
		
		inertiaDistribution = new GaussianDistribution();
		temp = (GaussianDistribution) inertiaDistribution;
		temp.setMean(ConstantControlParameter.of(0.729844));
		temp.setDeviation(ConstantControlParameter.of(0.2));
		
		
		this.globalGuideProvider = new NBestGuideProvider();
        this.localGuideProvider = new PBestGuideProvider(); 
        
	}
	/**
	 * 
	 */
	public StandardConvergentVelocityProviderGenerator( StandardConvergentVelocityProviderGenerator copy) {
		this.cognitiveDistribution = copy.cognitiveDistribution;
		this.socialDistribution = copy.socialDistribution;
		this.inertiaDistribution = copy.inertiaDistribution;
		
		this.localGuideProvider = copy.localGuideProvider.getClone();
		this.globalGuideProvider = copy.globalGuideProvider.getClone();
	}
	
	/**
	 *
	 */
	@Override
	public VelocityProviderGenerator getClone() {
		return new StandardConvergentVelocityProviderGenerator(this);
	}

	/**
	 * Generate parameters which satisfy the inequality: w > 0.5 (c1+c2) - 1
	 */
	@Override
	public VelocityProvider generate() {
		StandardVelocityProvider provider = new StandardVelocityProvider();
		provider.setLocalGuideProvider(localGuideProvider);
		provider.setGlobalGuideProvider(globalGuideProvider);

		double cognitive;
		double social;
		double inertia;
		
		do{
			cognitive = cognitiveDistribution.getRandomNumber();
			social = socialDistribution.getRandomNumber();
			inertia = inertiaDistribution.getRandomNumber();

			//clamp the range for inertia
			//if(inertia < 0) inertia = 0;
			//if(inertia > 1) inertia = 1;
		} while(inertia <= 0.5 * (social + cognitive) - 1);
		
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
	
	public void setSocialDistribution(ProbabilityDistributionFunction socialDistribution){
		this.socialDistribution = socialDistribution;
	}

	public void setInertiaDistribution(ProbabilityDistributionFunction inertiaDistribution){
		this.inertiaDistribution = inertiaDistribution;
	}
}
