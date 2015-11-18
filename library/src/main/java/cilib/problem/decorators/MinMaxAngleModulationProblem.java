/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.problem.decorators;

import cilib.functions.continuous.am.samplingstrategies.MinMaxAMSamplingStrategy;
import cilib.problem.AngleModulationProblem;
import cilib.problem.solution.Fitness;
import cilib.type.DomainRegistry;
import cilib.type.types.Type;
import cilib.type.types.container.Vector;

/**
 * Decorates an AngleModulationProblem with additional dimensions to optimize
 * the sampling range of the generating function.
 */
public class MinMaxAngleModulationProblem extends AngleModulationProblem {
    private AngleModulationProblem delegate;

    public MinMaxAngleModulationProblem() {    
    }
    
    public MinMaxAngleModulationProblem(MinMaxAngleModulationProblem copy) {
        this.delegate = copy.delegate.getClone();
    }

    @Override
    public AngleModulationProblem getClone() {
        return new MinMaxAngleModulationProblem(this);
    }
    
    public void setDelegate(AngleModulationProblem delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public DomainRegistry getDomain() {
        DomainRegistry domain = delegate.getDomain().getClone();
        String dimension = String.valueOf(domain.getDimension() + 2);
        domain.setDomainString(domain.getDomainString().replaceAll("\\^.*", "^" + dimension));
        
        return domain;
    }

    @Override
    protected Fitness calculateFitness(Type solution) {
        Vector solutionVector = (Vector) solution;
        
        Double[] partialSolution = new Double[solutionVector.size() - 2];
        for (int i = 0; i < partialSolution.length; i++) {
            partialSolution[i] = solutionVector.doubleValueOf(i);
        }
        
        double min = solutionVector.doubleValueOf(solutionVector.size() - 2);
        double max = solutionVector.doubleValueOf(solutionVector.size() - 1);
        
        delegate.getGeneratingFunction().setSampler(new MinMaxAMSamplingStrategy(min, max));
        
        String bitString = delegate.getGeneratingFunction().f(Vector.of(partialSolution));
        Vector expandedVector = delegate.decodeBitString(bitString, delegate.getGeneratingFunction().getBitsPerDimension());
        return delegate.getGeneratingFunction().getDelegate().getFitness(expandedVector);
    }
}
