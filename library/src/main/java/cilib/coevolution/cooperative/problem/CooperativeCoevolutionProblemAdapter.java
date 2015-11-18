/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.coevolution.cooperative.problem;

import cilib.algorithm.Algorithm;
import cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import cilib.entity.Entity;
import cilib.problem.AbstractProblem;
import cilib.problem.Problem;
import cilib.problem.solution.Fitness;
import cilib.type.StringBasedDomainRegistry;
import cilib.type.types.Type;
import cilib.type.types.Types;
import cilib.type.types.container.Vector;

/**
 * This {@linkplain CooperativeCoevolutionProblemAdapter optimisation problem}
 * is used by the participants of a {@linkplain CooperativeCoevolutionAlgorithm}.
 * It stores the {@linkplain DimensionAllocation} of the participant, and
 * provides the means to calculate the fitness of {@linkplain Entity}s
 * of the participating {@linkplain Algorithm}s.
 */
public class CooperativeCoevolutionProblemAdapter extends AbstractProblem {

    private static final long serialVersionUID = 3764040830993620887L;
    private final Problem problem;
    private Vector context;
    private final DimensionAllocation problemAllocation;

    /**
     * Creates an CooperativeCoevolutionProblemAdapter, which is assigned to
     * each participant in a {@linkplain CooperativeCoevolutionAlgorithm}.
     *
     * @param problem The original problem that is being optimised.
     * @param problemAllocation The {@linkplain DimensionAllocation} which dictates how the solutions
     *     of the {@linkplain Entity}'s that are optimizing this problem fits into the original problem.
     * @param context The current context solution of the {@linkplain CooperativeCoevolutionAlgorithm}
     */
    public CooperativeCoevolutionProblemAdapter(Problem problem, DimensionAllocation problemAllocation, Vector context) {
        this.problem = problem;
        this.problemAllocation = problemAllocation;
        this.context = Vector.copyOf(context);
        this.domainRegistry = new StringBasedDomainRegistry();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < problemAllocation.getSize(); i++) {
            String tmp = Types.getRepresentation(context.get(problemAllocation.getProblemIndex(i)));
            builder.append(tmp);
            if (i < problemAllocation.getSize() - 1) {
                builder.append(",");
            }
        }

        this.domainRegistry.setDomainString(builder.toString());
    }

    /**
     * @param copy
     */
    public CooperativeCoevolutionProblemAdapter(CooperativeCoevolutionProblemAdapter copy) {
        super(copy);
        this.context = Vector.copyOf(copy.context);
        this.problem = copy.problem.getClone();
        this.problemAllocation = copy.problemAllocation.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Fitness calculateFitness(Type solution) {
        for (int i = 0; i < problemAllocation.getSize(); ++i) {
            context.set(problemAllocation.getProblemIndex(i), ((Vector) solution).get(i));
        }
        return problem.getFitness(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractProblem getClone() {
        return new CooperativeCoevolutionProblemAdapter(this);
    }

    /**
     * @return Get the current {@linkplain DimensionAllocation}.
     */
    public DimensionAllocation getProblemAllocation() {
        return problemAllocation;
    }

    /**
     * Update the context vector with the given parameter.
     * @param context The new context.
     */
    public void updateContext(Vector context) {
        this.context = Vector.copyOf(context);
    }
}
