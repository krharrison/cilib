/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.selfadaptive;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.behaviour.StandardParticleBehaviour;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.velocityprovider.ClampingVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.recipes.ElitistSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

public class GreyPSOIterationStrategy extends AbstractIterationStrategy<PSO>{
	protected IterationStrategy<PSO> delegate;
	protected ClampingVelocityProvider velocityProvider;
	protected double eta;
	protected Selector<Particle> elitistSelector;
	
	public GreyPSOIterationStrategy(){
		super();
		elitistSelector = new ElitistSelector<Particle>();
		
	}
	
	@Override
	public AbstractIterationStrategy<PSO> getClone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void performIteration(PSO algorithm) {
		
		Vector gBest = (Vector)algorithm.getBestSolution().getPosition();
		Vector.Builder builder = Vector.newBuilder();
		double minInertia = Double.MAX_VALUE;
		double maxInertia = Double.MIN_VALUE;
		double minG = Double.MAX_VALUE;
		double maxG = Double.MIN_VALUE;
		
		//calculate the grey coefficient and find the min/max inertia values
		for(Particle p : algorithm.getTopology())
		{
			RelationalInformation info = calculateDelta(gBest, (Vector) p.getPosition());
			double sum = 0;
			double etaMax = eta * info.max;
			//double weight = info.getDelta().length();
			for(Numeric delta : info.delta){
				sum += (info.min + etaMax) / (delta.doubleValue() + etaMax);
			}
			
			sum /= info.delta.length(); //this assumes equal weighting for each element
			
			minG = Math.min(minG, sum);
			maxG = Math.max(maxG, sum);
			
			builder.add(sum);
			
			StandardParticleBehaviour behaviour = (StandardParticleBehaviour)p.getBehaviour();
			StandardVelocityProvider provider = (StandardVelocityProvider)((ClampingVelocityProvider) behaviour.getVelocityProvider()).getDelegate();
			double inertia = provider.getInertiaWeight().getParameter();
			
			minInertia = Math.min(minInertia, inertia);
			maxInertia = Math.max(maxInertia, inertia);
		}
		
		Vector g = builder.build();

		int index = 0;
		for(Particle p : algorithm.getTopology()){
			double inertia = calculateInertia(minInertia, maxInertia, g.doubleValueOf(index), minG, maxG);
			
			index++;
		}
	}

	
	private RelationalInformation calculateDelta(Vector gBest, Vector pos){
		
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		
		Vector.Builder builder = Vector.newBuilder();
		
		for(int i = 0; i < gBest.size(); i++){
			double val = Math.abs(gBest.get(i).doubleValue() - pos.get(i).doubleValue());
			
			max = Math.max(max, val);
			min = Math.min(min, val);
			
			builder.add(val);
		}
		
		return new RelationalInformation(builder.build(), min, max);
		
	}
	
	private double calculateInertia(double minInertia, double maxInertia, double g, double minG, double maxG){
		//double term1 = (minInertia - maxInertia) / (maxG - minG); //this is the original
		double term1 = (maxInertia - minInertia) / (maxG - minG); //this is what I assume to be correct
		double term2 = (maxInertia * maxG - minInertia * minG) / (maxG - minG);
		
		return term1 * g + term2;
	}
	
	public void setVelocityProvider(ClampingVelocityProvider velocityProvider){
		this.velocityProvider = velocityProvider;
	}
	
	public void setDelegate(IterationStrategy<PSO> delegate){
		this.delegate = delegate;
	}
	
	
	class RelationalInformation{
		
		public RelationalInformation(Vector delta, double min, double max){
			this.delta = delta;
			this.min = min;
			this.max = max;
		}
		
		Vector delta;
		double min;
		double max;
		
		//public Vector getDelta(){
		//	return this.delta;
		//}
		
		//public double getMin(){
		//	return this.min;
		//}
		
		//public double getMax(){
		//	return this.max;
		//}
	}
}
