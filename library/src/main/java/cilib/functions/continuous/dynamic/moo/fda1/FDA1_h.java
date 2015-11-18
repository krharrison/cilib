/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.dynamic.moo.fda1;

import cilib.functions.ContinuousFunction;
import cilib.problem.FunctionOptimisationProblem;
import cilib.type.types.container.Vector;

/**
 * This function is the h function of the FDA1 problem defined on page 428 in
 * the following paper: M.Farina, K.Deb, P.Amato. Dynamic multiobjective
 * optimization problems: test cases, approximations and applications, IEEE
 * Transactions on Evolutionary Computation, 8(5): 425-442, 2003
 *
 */
public class FDA1_h extends ContinuousFunction {

    private static final long serialVersionUID = -539665464941830813L;
    //members
    private ContinuousFunction fda1_g;
    private ContinuousFunction fda1_f1;
    private FunctionOptimisationProblem fda1_f1_problem;
    private FunctionOptimisationProblem fda1_g_problem;

    //Domain("R(-1, 1)^20")

    /**
     * Sets the g function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the g function.
     */
    public void setFDA1_g(FunctionOptimisationProblem problem) {
        this.fda1_g_problem = problem;
        this.fda1_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return fda1_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getFDA1_g_problem() {
        return this.fda1_g_problem;
    }

    /**
     * Sets the g function that is used in the FDA1 problem without specifying
     * the problem.
     * @param fda1_g ContinuousFunction used for the g function.
     */
    public void setFDA1_g(ContinuousFunction fda1_g) {
        this.fda1_g = fda1_g;
    }

    /**
     * Returns the g function that is used in the FDA1 problem.
     * @return fda1_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getFDA1_g() {
        return this.fda1_g;
    }

    /**
     * Sets the f1 function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the f1 function.
     */
    public void setFDA1_f(FunctionOptimisationProblem problem) {
        this.fda1_f1_problem = problem;
        this.fda1_f1 = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the f1 function.
     * @return fda1_f1_problem FunctionOptimisationProblem used for the f1
     * function.
     */
    public FunctionOptimisationProblem getFDA1_f_problem() {
        return this.fda1_f1_problem;
    }

    /**
     * Sets the f1 function that is used in the FDA1 problem without specifying
     * the problem.
     * @param fda1_f1 ContinuousFunction used for the f1 function.
     */
    public void setFDA1_f(ContinuousFunction fda1_f1) {
        this.fda1_f1 = fda1_f1;
    }

    /**
     * Returns the f1 function that is used in the FDA1 problem.
     * @return fda1_f1 ContinuousFunction used for the f1 function.
     */
    public ContinuousFunction getFDA1_f() {
        return this.fda1_f1;
    }

    /**
     * Evaluates the function.
     */
    @Override
    public Double f(Vector input) {
        Vector y = input.copyOfRange(0, 1);
        Vector z = input.copyOfRange(1, input.size());
        double g = this.fda1_g.f(z);
        double f1 = this.fda1_f1.f(y);

        double value = 1.0;
        value -= Math.sqrt((double) f1 / (double) g);

        return value;
    }
}
