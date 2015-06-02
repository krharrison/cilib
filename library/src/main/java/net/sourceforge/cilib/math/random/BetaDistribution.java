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
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.math.random.GammaDistribution;

public class BetaDistribution implements ProbabilityDistributionFunction {

	private ControlParameter alpha;
	private ControlParameter beta;
	
	public BetaDistribution(){
		alpha = ConstantControlParameter.of(1.0);
		beta = ConstantControlParameter.of(1.0);
	}
	
    /**
     * Get a Beta-distributed random number using the parameters specified in the class.
     * @return a Beta-distributed random number.
     */
	@Override
	public double getRandomNumber() {
		return getRandomNumber(alpha.getParameter(), beta.getParameter());
	}

    /**
     * Get a Beta-distributed random number using the parameters specified.
     * This is based on the implementation found in NumPy.
     * 
     * @param parameters the alpha and beta of the distribution.
     * 
     * @return a Beta-distributed random number.
     */
	@Override
	public double getRandomNumber(double... parameters) {
		checkArgument(parameters.length > 1, "The Beta distribution requires at least two parameters.");
		checkArgument(parameters[0] > 0, "The alpha parameter must be greater than zero.");
        checkArgument(parameters[1] > 0, "The beta parameter must be greater than zero.");

        double a = parameters[0];
        double b = parameters[1];
        
        if ((a <= 1.0) && (b <= 1.0))
        {
            double u,v,x,y;
            /* Jonk's algorithm */

            while (true)
            {
                u = Rand.nextDouble();
                v = Rand.nextDouble();
                x = Math.pow(u, 1.0/a);
                y = Math.pow(v, 1.0/b);

                if ((x+y) <= 1.0)
                {
                    if (x+y > 0)
                    {
                        return x / (x + y);
                    }
                    else
                    {
                        double logX = Math.log(u) / a;
                        double logY = Math.log(v) / b;
                        double logM = logX > logY ? logX : logY;
                        logX -= logM;
                        logY -= logM;

                        return Math.exp(logX - Math.log(Math.exp(logX) + Math.exp(logY)));
                    }
                }
            }
        }
        else
        {
        	GammaDistribution gamma = new GammaDistribution();
        	double ga = gamma.getRandomNumber(1, a);
        	double gb = gamma.getRandomNumber(1, b);
        	
        	return(ga / (ga + gb));
        }
	}
	
	
    public void setAlpha(ControlParameter alpha) {
        this.alpha = alpha;
    }

    public ControlParameter getAlpha() {
        return alpha;
    }

    public void setBeta(ControlParameter beta) {
        this.beta = beta;
    }

    public ControlParameter getBeta() {
        return beta;
    }
}
