/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.discontinuous;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.type.types.container.Vector;

public class ConvergenceAnalysis extends ContinuousFunction{

	private Map<Integer, Double> values;
	private double min;
	private double max;
	
	public ConvergenceAnalysis(){
		values = new HashMap<Integer, Double>();
		min = -1000;
		max = 1000;
	}
	
	@Override
	public Double f(Vector input) {
		int hash = input.hashCode();
		
		if(values.containsKey(hash)){
			return values.get(hash);
		}
		else{
			double value = Rand.nextDouble() * (max - min) + min;
			values.put(hash, value);
			return value;
		}
	}

	
	public void setMin(double value){
		this.min = value;
	}
	
	public void setMax(double value){
		this.max = value;
	}
}
