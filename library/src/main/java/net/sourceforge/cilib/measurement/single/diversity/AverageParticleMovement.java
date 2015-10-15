/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.diversity;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.entity.Property;

public class AverageParticleMovement implements Measurement<Real>{

	private double max;
	
	public AverageParticleMovement(){
		max = 2000;
	}
	
	@Override
	public Measurement<Real> getClone() {
		return this;
	}

	@Override
	public Real getValue(Algorithm algorithm) {
		PSO pso = (PSO) algorithm;
        int numberOfEntities = pso.getTopology().length();
        
        double sum = 0;
        
        for(Particle p : pso.getTopology()){
        	Vector pos = (Vector) p.getPosition();
        	Vector previousPos = (Vector) p.get(Property.PREVIOUS_SOLUTION);
        	
        	sum += pos.subtract(previousPos).norm();
        }
        
        double avg = sum / numberOfEntities;
        
        if(Double.isFinite(avg)){
        	return Real.valueOf(Math.min(avg, max));
        }
        else{
        	return Real.valueOf(max);
        }
	}

}
