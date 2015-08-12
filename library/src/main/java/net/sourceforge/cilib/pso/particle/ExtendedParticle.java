/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * An extended particle has the control parameters embedded within the candidate
 * solution, i.e., [w,c1,c2,x1,x2,...,xn].
 *
 */
public class ExtendedParticle extends StandardParticle {

	public ExtendedParticle(){
		super();
	}
	
	public ExtendedParticle(ExtendedParticle copy){
		super(copy);
	}
	
	public ExtendedParticle getClone(){
		return new ExtendedParticle(this);
	}
	
    public StructuredType getSolution(){
    	Vector extendedPosition = (Vector) get(Property.CANDIDATE_SOLUTION);
        return extendedPosition.copyOfRange(3, extendedPosition.size());
    }
    
    public double getInertiaWeight(){
    	Vector extendedPosition = (Vector) get(Property.CANDIDATE_SOLUTION);
    	return extendedPosition.get(0).doubleValue();
    }
    
    public double getCognitiveWeight(){
    	Vector extendedPosition = (Vector) get(Property.CANDIDATE_SOLUTION);
    	return extendedPosition.get(1).doubleValue();
    }
    
    public double getSocialWeight(){
    	Vector extendedPosition = (Vector) get(Property.CANDIDATE_SOLUTION);
    	return extendedPosition.get(2).doubleValue();
    }
}
