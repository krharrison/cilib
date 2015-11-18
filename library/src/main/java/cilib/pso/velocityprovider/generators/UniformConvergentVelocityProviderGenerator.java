/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.velocityprovider.generators;

import cilib.controlparameter.ConstantControlParameter;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.UniformDistribution;
import cilib.math.random.generator.Rand;
import cilib.pso.guideprovider.GuideProvider;
import cilib.pso.guideprovider.NBestGuideProvider;
import cilib.pso.guideprovider.PBestGuideProvider;
import cilib.pso.velocityprovider.StandardVelocityProvider;
import cilib.pso.velocityprovider.VelocityProvider;
import cilib.type.types.container.Vector;

public class UniformConvergentVelocityProviderGenerator implements VelocityProviderGenerator {

	private GuideProvider globalGuideProvider;
	private GuideProvider localGuideProvider;
	private UniformDistribution cognitiveDistribution;
	private UniformDistribution socialDistribution;
	private UniformDistribution inertiaDistribution;
	//private boolean initialized;
	private double x1,x2,x3,pT1; //the x points and the probability associated with triangle 1
	private Vector T1A, T1B, T1C, T2A, T2B, T2C; //the vertices of T1 and T2, respectively

	public UniformConvergentVelocityProviderGenerator(){
		this.globalGuideProvider = new NBestGuideProvider();
		this.localGuideProvider = new PBestGuideProvider();
		//this.initialized = false;

		//set up the range for the cognitive component
		UniformDistribution cog = new UniformDistribution();
		cog.setLowerBound(ConstantControlParameter.of(0));
		cog.setUpperBound(ConstantControlParameter.of(4));
		this.cognitiveDistribution = cog;

		//set up the range for the social component
		UniformDistribution soc = new UniformDistribution();
		soc.setLowerBound(ConstantControlParameter.of(0));
		soc.setUpperBound(ConstantControlParameter.of(4));
		this.socialDistribution = soc;

		//set up the range for the inertia component
		UniformDistribution inert = new UniformDistribution();
		inert.setLowerBound(ConstantControlParameter.of(0));
		inert.setUpperBound(ConstantControlParameter.of(1));
		this.inertiaDistribution = inert;

		//TODO: this needs to be done somewhere else once distributions are accounted for
		initializeTriangles();
	}

	public UniformConvergentVelocityProviderGenerator(UniformConvergentVelocityProviderGenerator clone){
		this.cognitiveDistribution = clone.cognitiveDistribution;
		this.socialDistribution = clone.socialDistribution;
		this.inertiaDistribution = clone.inertiaDistribution;
		this.globalGuideProvider = clone.globalGuideProvider.getClone();
		this.localGuideProvider = clone.localGuideProvider.getClone();
		//this.initialized = false; //force reinitialization
//		this.x1 = clone.x1;
//		this.x2 = clone.x2;
//		this.x3 = clone.x3;
//		this.pT1 = clone.pT1;
//		this.T1A = Vector.copyOf(clone.T1A);
//		this.T1B = Vector.copyOf(clone.T1B);
//		this.T1C = Vector.copyOf(clone.T1C);
//		this.T2A = Vector.copyOf(clone.T2A);
//		this.T2B = Vector.copyOf(clone.T2B);
//		this.T2C = Vector.copyOf(clone.T2C);
	}

	@Override
	public UniformConvergentVelocityProviderGenerator getClone() {
		return new UniformConvergentVelocityProviderGenerator(this);
	}

	@Override
	public VelocityProvider generate() {
		StandardVelocityProvider provider = new StandardVelocityProvider();
		provider.setLocalGuideProvider(localGuideProvider);
		provider.setGlobalGuideProvider(globalGuideProvider);

		double cognitive;
		double social;
		double inertia;

		Vector point;

		double r1 = Rand.nextDouble();
		double r2 = Rand.nextDouble();
		double sqr1 = Math.sqrt(r1);

		//generate in triangle 1
		if(Rand.nextDouble() < pT1){
			point = T1A.multiply(1 - sqr1).plus(T1B.multiply(sqr1 * (1 - r2)).plus(T1C.multiply(sqr1 * r2)));
		}
		else{ //triangle 2
			point = T2A.multiply(1 - sqr1).plus(T2B.multiply(sqr1 * (1 - r2)).plus(T2C.multiply(sqr1 * r2)));
		}

		double theta = point.get(0).doubleValue(); 		//this is c1 + c2, so we need to split this
		inertia = point.get(1).doubleValue();

		/*
		 * Need to divide up the remainder of theta, but ensure that cognitive and social both stay
		 * within the range of [0,4].
		 */
		//TODO: make this more general and account for user supplied values.
		double min = theta > 4 ? theta - 4 : 0; 			//minimum amount which must be allocated to cognitive
		double remainder = theta - min;						//the remainder after cognitive has taken its minimum
		double max = Math.min(remainder, 4);				//the maximum that can be allocated to cognitive
		cognitive =  Rand.nextDouble() * (max - min) + min; //generate a random amount to allocate to cognitive
		social = theta - cognitive;							//social is simply (theta - cognitive)

		provider.setCognitiveAcceleration(ConstantControlParameter.of(cognitive));
		provider.setSocialAcceleration(ConstantControlParameter.of(social));
		provider.setInertiaWeight(ConstantControlParameter.of(inertia));

		return provider;
	}


	private void initializeTriangles(){
		double c1min = cognitiveDistribution.getLowerBound().getParameter();
		double c2min = socialDistribution.getLowerBound().getParameter();
		double wmin = inertiaDistribution.getLowerBound().getParameter();
		double wmax = inertiaDistribution.getUpperBound().getParameter();

		//ensure that the inertia distributions lower bound is sufficiently large
		//wmin = Math.max(wmin, (c1min + c2min / 2) -1);
		//inertiaDistribution.setLowerBound(wmin);

		x1 = c1min + c2min;
		x2 = 2 * (wmin + 1);
		x3 = 2 * (wmax + 1);

		T1A = Vector.of(x1, wmin);
		T1B = Vector.of(x1, wmax);
		T1C = Vector.of(x3, wmax);

		T2A = Vector.of(x1, wmin);
		T2B = Vector.of(x3, wmax);
		T2C = Vector.of(x2, wmin);

		double aT1 = ((x3 - x1) * (wmax - wmin)) / 2.0;
		double aT2 = ((x2 - x1) * (wmax - wmin)) / 2.0;

		pT1 = aT1 / (aT1 + aT2);

		//initialized = true;
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

//    public void setCognitiveDistribution(UniformDistribution cognitiveDistribution){
//		this.cognitiveDistribution = cognitiveDistribution;
//		this.initialized = false;
//	}

	public ProbabilityDistributionFunction getCognitiveDistribution(){
		return this.cognitiveDistribution;
	}

	//	public void setSocialDistribution(UniformDistribution socialDistribution){
//		this.socialDistribution = socialDistribution;
//		this.initialized = false;
//	}
	public ProbabilityDistributionFunction getSocialDistribution(){
		return this.socialDistribution;
	}

//	public void setInertiaDistribution(UniformDistribution inertiaDistribution){
//		this.inertiaDistribution = inertiaDistribution;
//		this.initialized = false;
//	}

	public ProbabilityDistributionFunction getInertiaDistribution(){
		return this.inertiaDistribution;
	}

}