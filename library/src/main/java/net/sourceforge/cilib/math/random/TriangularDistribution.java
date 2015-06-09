package net.sourceforge.cilib.math.random;

import static com.google.common.base.Preconditions.checkArgument;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.random.generator.Rand;

public class TriangularDistribution implements ProbabilityDistributionFunction {

	 private ControlParameter minimum;
	 private ControlParameter maximum;
	 private ControlParameter mode;
	
	 public TriangularDistribution(){
		 this.minimum = ConstantControlParameter.of(0.0);
		 this.maximum = ConstantControlParameter.of(1.0);
		 this.mode = ConstantControlParameter.of(0.5);
	 }
	 
	@Override
	public double getRandomNumber() {
		return getRandomNumber(minimum.getParameter(), maximum.getParameter(), mode.getParameter());
	}

	@Override
	public double getRandomNumber(double... parameters) {
		checkArgument(parameters.length == 3, "The Triangular distribution requires three parameters - min, max, mode.");
		checkArgument(parameters[2] <= parameters[1] && parameters[2] >= parameters[0], "The mode must be between min and max.");
		
		double minimum = parameters[0];
		double maximum = parameters[1];
		double mode = parameters[2];
		
		double u = Rand.nextDouble();

        if (u <= (mode - minimum) / (maximum - minimum))
            return minimum + Math.sqrt(u * (maximum - minimum) * (mode - minimum));
        else
            return maximum - Math.sqrt((1 - u) * (maximum - minimum) * (maximum - mode));
	}
	
    public ControlParameter getMinimum() {
        return minimum;
    }

    public void setMinimum(ControlParameter minimum) {
        this.minimum = minimum;
    }
    
    public ControlParameter getMaximum() {
        return maximum;
    }

    public void setMaximum(ControlParameter maximum) {
        this.maximum = maximum;
    }

    public ControlParameter getMode() {
        return mode;
    }

    public void setMode(ControlParameter mode) {
        this.mode = mode;
    }
    
}
