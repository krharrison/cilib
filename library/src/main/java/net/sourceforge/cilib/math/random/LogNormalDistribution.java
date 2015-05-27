/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random;

import static com.google.common.base.Preconditions.checkArgument;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.random.GaussianDistribution;

public class LogNormalDistribution implements ProbabilityDistributionFunction {

	//mean and standard deviation of the distribution on a log scale
	private ControlParameter mean;
	private ControlParameter deviation;
	
	public LogNormalDistribution(){
		mean = ConstantControlParameter.of(0.0);
        deviation = ConstantControlParameter.of(1.0);
	}
	
	@Override
    public double getRandomNumber() {
        return getRandomNumber(mean.getParameter(), deviation.getParameter());
    }

    /**
     * Get a LogNormal-distributed random number using the mean and standard deviation specified.
     * 
     * @param parameters the mean and standard deviation on the log scale.
     * 
     * @return a LogNormally distributed random number.
     */
    @Override
    public double getRandomNumber(double... parameters) {
    	checkArgument(parameters.length == 2, "The LogNormal distribution requires two parameters.");
    	
    	GaussianDistribution gaussian = new GaussianDistribution();
    	return Math.exp(gaussian.getRandomNumber(parameters[0], parameters[1]));
    }
    
    public void setDeviation(ControlParameter deviation) {
        this.deviation = deviation;
    }

    public ControlParameter getDeviation() {
        return deviation;
    }

    public void setMean(ControlParameter mean) {
        this.mean = mean;
    }

    public ControlParameter getMean() {
        return mean;
    }
	
}
