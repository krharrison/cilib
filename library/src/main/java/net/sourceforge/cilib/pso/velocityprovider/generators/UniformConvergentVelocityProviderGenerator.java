/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider.generators;

import static com.google.common.base.Preconditions.checkState;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.pso.guideprovider.GuideProvider;
import net.sourceforge.cilib.pso.guideprovider.NBestGuideProvider;
import net.sourceforge.cilib.pso.guideprovider.PBestGuideProvider;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Generates {@link StandardParticleVelocity} velocity providers with parameters
 * that are guaranteed to be convergent. Parameters are uniformly distributed within
 * the (triangular) convergent region.
 * 
 * Shape Distributions (2002 ACM Transaction on Graphics)
 */
public class UniformConvergentVelocityProviderGenerator implements VelocityProviderGenerator {

	private GuideProvider localGuideProvider;
	private GuideProvider globalGuideProvider;

	
	public UniformConvergentVelocityProviderGenerator(){

		this.globalGuideProvider = new NBestGuideProvider();
        this.localGuideProvider = new PBestGuideProvider(); 
        
	}
	/**
	 * 
	 */
	public UniformConvergentVelocityProviderGenerator( UniformConvergentVelocityProviderGenerator copy) {

		
		this.localGuideProvider = copy.localGuideProvider.getClone();
		this.globalGuideProvider = copy.globalGuideProvider.getClone();
	}
	
	/**
	 *
	 */
	@Override
	public VelocityProviderGenerator getClone() {
		return new UniformConvergentVelocityProviderGenerator(this);
	}

	/**
	 * Generate parameters which satisfy convergence criteria
	 */
	@Override
	public VelocityProvider generate() {
		StandardVelocityProvider provider = new StandardVelocityProvider();
		provider.setLocalGuideProvider(localGuideProvider);
		provider.setGlobalGuideProvider(globalGuideProvider);

		//define the vertices of the convergent parameter triangle
		Vector A = Vector.of(0, 2);
		Vector B = Vector.of(1, 2);
		Vector C = Vector.of(1, 4);

		double r1 = Rand.nextDouble();
		double r2 = Rand.nextDouble();
		double sqr1 = Math.sqrt(r1);
				
		Vector v = A.multiply(1 - sqr1).plus(B.multiply(sqr1 * (1 - r2)).plus(C.multiply(sqr1 * r2)));

		double inertia = v.get(0).doubleValue();
		double theta = v.get(1).doubleValue(); 		//this is c1 + c2, so we need to split this
		
		/*
		 * Need to divide up the remainder of theta, but ensure that cognitive and social both stay
		 * within the range of [1,2].
		 */
		double remainder = theta - 2; //c1 and c2 each get 1 automatically, then split the rest randomly while ensuring they fall in the appropriate range
		double min = remainder > 1 ? remainder - 1 : 0; //minimum excess that cognitive gets
		double max = Math.max(remainder, 1);
		double excess = (Rand.nextDouble() * (max - min) + min);
		//excess = Math.min(excess, 1); //ensure we don't take more than we can.
		double cognitive =  1 + excess;
		double social = theta - cognitive;				//social component is the remainder
		
		checkState(inertia > 0.5 * (social + cognitive) - 1, "Divergent Parameter generated");
		
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
}
