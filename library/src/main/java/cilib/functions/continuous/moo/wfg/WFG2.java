/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.functions.continuous.moo.wfg;

import java.util.List;

import cilib.functions.ContinuousFunction;
import cilib.problem.FunctionOptimisationProblem;
import cilib.type.types.container.Vector;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 *
 */
public class WFG2 extends WFGProblem {

    private static final long serialVersionUID = -545831558090360395L;
    
    public WFG2() { }

    @Override
    protected void initialize() {
        clear();
        final int k = 2 * (this.m - 1);
        for (int i = 0; i < this.m; ++i) {
            final int index = i;
            ContinuousFunction function = new ContinuousFunction() {

                @Override
                public Double f(Vector input) {
                    Vector y = Problems.WFG2(input, k, m);
                    return y.doubleValueOf(index);
                }
            };
            FunctionOptimisationProblem wfg2_fm = new FunctionOptimisationProblem();
            wfg2_fm.setFunction(function);
            List<String> domain = Lists.newArrayList();
            for (int j = 0; j < k + this.l; ++j) {
                domain.add("R(0:" + 2 * (j + 1) + ")");
            }
            wfg2_fm.setDomain(Joiner.on(", ").join(domain));
            add(wfg2_fm);
        }
    }

    public WFG2(WFG2 copy) {
        super(copy);
    }

    @Override
    public WFG2 getClone() {
        return new WFG2(this);
    }
}
