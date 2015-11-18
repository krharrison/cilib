/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.measurement.multiple;

import cilib.algorithm.AbstractAlgorithm;
import cilib.measurement.Measurement;
import cilib.type.types.Real;
import cilib.algorithm.Algorithm;
import cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Entity;
import cilib.functions.Gradient;
import cilib.problem.FunctionOptimisationProblem;
import cilib.problem.Problem;
import cilib.problem.solution.OptimisationSolution;
import cilib.type.types.container.Vector;

/**
 *
 * @author florent
 */
public class GradientVectorLengthAverage implements Measurement<Real> {

    @Override
    public GradientVectorLengthAverage getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm)
    {
        
        double res = 0.0;
        int i = 0;
        
        MultiPopulationBasedAlgorithm multi;
        if (algorithm instanceof SinglePopulationBasedAlgorithm)
        {
            multi = neighbourhood2populations((SinglePopulationBasedAlgorithm<Entity>) algorithm);
        }
        else
        {
            multi = (MultiPopulationBasedAlgorithm) algorithm;
        }
        for (SinglePopulationBasedAlgorithm single : multi.getPopulations())
        {
            ++i;
            OptimisationSolution best = single.getBestSolution();
            Problem d = single.getOptimisationProblem();
            FunctionOptimisationProblem fop = (FunctionOptimisationProblem) d;
            Gradient df = (Gradient) fop.getFunction();

            res += df.getGradientVectorLength((Vector) best.getPosition());
        }
        return Real.valueOf(res / ((double)i));
    }

private <E extends Entity> MultiPopulationBasedAlgorithm neighbourhood2populations(SinglePopulationBasedAlgorithm<E> s) {
        MultiPopulationBasedAlgorithm m = new MultiPopulationBasedAlgorithm() {
            @Override
        protected void algorithmIteration() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
        public AbstractAlgorithm getClone() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
        public OptimisationSolution getBestSolution() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
        public Iterable<OptimisationSolution> getSolutions() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        for (E e : s.getTopology()) {
            SinglePopulationBasedAlgorithm dummyPopulation = s.getClone();
            dummyPopulation.setTopology(s.getNeighbourhood().f(s.getTopology(), e));
            m.addPopulationBasedAlgorithm(dummyPopulation);
        }
        return m;
    }
}
