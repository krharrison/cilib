/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.tuning.parameterchange.triggers;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.tuning.TuningAlgorithm;

public class SolutionCountParameterChangeTrigger extends ParameterChangeTrigger {
    private ControlParameter count;

    public SolutionCountParameterChangeTrigger() {
        this.count = ConstantControlParameter.of(5.0);
    }

    @Override
    public Boolean f(TuningAlgorithm a) {
        return a.getParameterList().length() <= count.getParameter();
    }

    public void setCount(ControlParameter count) {
        this.count = count;
    }

    public ControlParameter getCount() {
        return count;
    }

}
