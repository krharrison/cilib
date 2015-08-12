/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.boundaryconstraint;

import fj.F;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;
import net.sourceforge.cilib.util.Vectors;

/**
 * Prevent any {@link Entity} from over-shooting the problem search space. Any
 * Entity that passes outside the search space is placed on the boundaries of
 * the search space.
 */
public class ExtendedParticleParameterClampingBoundaryConstraint implements BoundaryConstraint {

    private static final long serialVersionUID = 3910725111116160491L;

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedParticleParameterClampingBoundaryConstraint getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enforce(Entity entity) {
        Vector candidateSolution = (Vector) entity.getPosition();
        
        //clamp the parameter portion of the parameters
        Vector result = Vectors.transform(candidateSolution.copyOfRange(0, 3), new F<Numeric, Double>() {
            @Override
            public Double f(Numeric from) {
                Bounds bounds = from.getBounds();
                if (Double.compare(from.doubleValue(), bounds.getLowerBound()) < 0) {
                    return bounds.getLowerBound();
                } else if (Double.compare(from.doubleValue(), bounds.getUpperBound()) > 0) { // number > upper bound
                    return bounds.getUpperBound() - Maths.EPSILON;
                }
                return from.doubleValue();
            }
        });
        
        //update the candidate position
        candidateSolution.setReal(0, result.doubleValueOf(0));
        candidateSolution.setReal(1, result.doubleValueOf(1));
        candidateSolution.setReal(2, result.doubleValueOf(2));
        
        //set the entity position to the clamped position
        entity.setPosition(candidateSolution);
    }
}
