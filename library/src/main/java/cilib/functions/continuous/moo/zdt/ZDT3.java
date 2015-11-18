/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.moo.zdt;

import cilib.functions.ContinuousFunction;
import cilib.problem.FunctionOptimisationProblem;
import cilib.problem.MOOptimisationProblem;
import cilib.type.types.container.Vector;

/**
 * <p>Zitzler-Thiele-Deb Test Function 3</p>
 *
 * Characteristics:
 * <ul>
 * <li>Convex Pareto-optimal front.</li>
 * <li>Discontiguous Pareto-optimal front.</li>
 * </ul>
 *
 * <p>
 * This function represents the discreteness feature; its Pareto-optimal front
 * consists of several noncontiguous convex parts. The introduction of the sine
 * function in h causes discontinuity in the Pareto-optimal front. However,
 * there is no discontinuity in the parameter space.
 * </p>
 *
 * <p>
 * The Pareto-optimal front is formed with g(x) = 1
 * </p>
 *
 * <p>
 * References:
 * </p>
 * <p>
 * <ul>
 * <li>
 * E. Zitzler, K. Deb and L. Thiele, "Comparison of multiobjective
 * evolutionary algorithms: Empirical results", in Evolutionary Computation,
 * vol 8, no 2, pp. 173-195, 2000.
 * </li>
 * </ul>
 * </p>
 *
 */
public final class ZDT3 extends MOOptimisationProblem {

    private static final long serialVersionUID = 5783167168187614882L;
    private static final String DOMAIN = "R(0:1)^30";

    private static class ZDT3_h extends ContinuousFunction {

        private static final long serialVersionUID = -3438306908263146396L;
        private final ZDT_f1 f1;
        private final ZDT_g g;

        public ZDT3_h() {
            this.f1 = new ZDT_f1();
            this.g = new ZDT_g();
        }

        @Override
        public Double f(Vector input) {
            double f1_val = this.f1.f(input);
            double g_val = this.g.f(input);
            return 1.0 - Math.sqrt(f1_val / g_val) - (f1_val / g_val) * Math.sin(10.0 * Math.PI * f1_val);
        }
    }

    private static class ZDT3_f2 extends ContinuousFunction {

        private static final long serialVersionUID = 1052615620850285975L;
        private final ZDT_g g;
        private final ZDT3_h h;

        public ZDT3_f2() {
            this.g = new ZDT_g();
            this.h = new ZDT3_h();
        }

        @Override
        public Double f(Vector input) {
            return this.g.f(input) * this.h.f(input);
        }
    }

    public ZDT3() {
        FunctionOptimisationProblem zdt3_f1 = new FunctionOptimisationProblem();
        zdt3_f1.setFunction(new ZDT_f1());
        zdt3_f1.setDomain(DOMAIN);
        add(zdt3_f1);

        FunctionOptimisationProblem zdt3_f2 = new FunctionOptimisationProblem();
        zdt3_f2.setFunction(new ZDT3_f2());
        zdt3_f2.setDomain(DOMAIN);
        add(zdt3_f2);
    }

    public ZDT3(ZDT3 copy) {
        super(copy);
    }

    @Override
    public ZDT3 getClone() {
        return new ZDT3(this);
    }
}
