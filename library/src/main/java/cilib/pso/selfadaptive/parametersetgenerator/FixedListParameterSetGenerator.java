/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.parametersetgenerator;

import cilib.math.random.generator.Rand;
import cilib.pso.selfadaptive.ParameterSet;

/**
 * Generates parameter sets by randomly selecting them from a list of the 100 best.
 * Alternatively, the percentage can be set to limit parameters to the best X% of parameters.
 */
public class FixedListParameterSetGenerator implements ParameterSetGenerator {

    protected double percentage; //used to control the percentage of the best 100 which are used for selection

    public FixedListParameterSetGenerator()
    {
        percentage = 1.0;
    }

    @Override
    /**
     * Return a random entry from the list of 100 best parameters
     */
    public ParameterSet generate() {
        int highIndex = (int)(percentage * parameters.length);
        return parameters[Rand.nextInt(highIndex)];
    }

    @Override
    public ParameterSetGenerator getClone() {
        return this;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    //best 100 parameter sets
    public static ParameterSet[] parameters = {
            ParameterSet.fromValues(0.1, 0.95, 2.85),
            ParameterSet.fromValues(-0.1, 0.875, 2.625),
            ParameterSet.fromValues(0, 0.9, 2.7),
            ParameterSet.fromValues(0, 0.925, 2.775),
            ParameterSet.fromValues(0.6, 1.8, 1.8),
            ParameterSet.fromValues(0.5, 1.9, 1.9),
            ParameterSet.fromValues(0.7, 1.65, 1.65),
            ParameterSet.fromValues(-0.2, 0.8, 2.4),
            ParameterSet.fromValues(-0.3, 0.7, 2.1),
            ParameterSet.fromValues(0.6, 1.85, 1.85),
            ParameterSet.fromValues(0.5, 1.95, 1.95),
            ParameterSet.fromValues(0.4, 2, 2),
            ParameterSet.fromValues(0.2, 0.975, 2.925),
            ParameterSet.fromValues(-0.1, 0.85, 2.55),
            ParameterSet.fromValues(0.1, 0.975, 2.925),
            ParameterSet.fromValues(0.7, 1.7, 1.7),
            ParameterSet.fromValues(0.7, 1.6, 1.6),
            ParameterSet.fromValues(0.3, 1, 3),
            ParameterSet.fromValues(0.7, 2.55, 0.85),
            ParameterSet.fromValues(0.7, 2.475, 0.825),
            ParameterSet.fromValues(0.6, 1.75, 1.75),
            ParameterSet.fromValues(0.3, 0.975, 2.925),
            ParameterSet.fromValues(0.1, 0.925, 2.775),
            ParameterSet.fromValues(0.2, 1, 3),
            ParameterSet.fromValues(0.2, 0.95, 2.85),
            ParameterSet.fromValues(0.5, 1.85, 1.85),
            ParameterSet.fromValues(0.6, 1.9, 1.9),
            ParameterSet.fromValues(0.5, 2, 2),
            ParameterSet.fromValues(0.4, 0.975, 2.925),
            ParameterSet.fromValues(0.8, 1.4, 1.4),
            ParameterSet.fromValues(0.8, 1.35, 1.35),
            ParameterSet.fromValues(0.4, 1.95, 1.95),
            ParameterSet.fromValues(0.5, 0.975, 2.925),
            ParameterSet.fromValues(0.6, 0.925, 2.775),
            ParameterSet.fromValues(-0.2, 0.775, 2.325),
            ParameterSet.fromValues(0.4, 1, 3),
            ParameterSet.fromValues(-0.4, 0.6, 1.8),
            ParameterSet.fromValues(0.5, 0.95, 2.85),
            ParameterSet.fromValues(0.3, 0.95, 2.85),
            ParameterSet.fromValues(0.7, 1.75, 1.75),
            ParameterSet.fromValues(0.8, 1.3, 1.3),
            ParameterSet.fromValues(0.4, 0.95, 2.85),
            ParameterSet.fromValues(0.7, 1.55, 1.55),
            ParameterSet.fromValues(0.8, 1.45, 1.45),
            ParameterSet.fromValues(0.6, 0.9, 2.7),
            ParameterSet.fromValues(0.7, 0.825, 2.475),
            ParameterSet.fromValues(0.5, 0.925, 2.775),
            ParameterSet.fromValues(0.7, 0.85, 2.55),
            ParameterSet.fromValues(0.8, 2.025, 0.675),
            ParameterSet.fromValues(0.8, 2.1, 0.7),
            ParameterSet.fromValues(0.7, 0.8, 2.4),
            ParameterSet.fromValues(0.6, 1.7, 1.7),
            ParameterSet.fromValues(0.8, 0.675, 2.025),
            ParameterSet.fromValues(0.7, 1.5, 1.5),
            ParameterSet.fromValues(0.2, 0.925, 2.775),
            ParameterSet.fromValues(0.6, 0.875, 2.625),
            ParameterSet.fromValues(0, 0.875, 2.625),
            ParameterSet.fromValues(0.8, 1.25, 1.25),
            ParameterSet.fromValues(0.4, 0.925, 2.775),
            ParameterSet.fromValues(0.8, 0.65, 1.95),
            ParameterSet.fromValues(0.7, 0.775, 2.325),
            ParameterSet.fromValues(-0.5, 0.5, 1.5),
            ParameterSet.fromValues(0.4, 2.05, 2.05),
            ParameterSet.fromValues(0, 0.95, 2.85),
            ParameterSet.fromValues(0.5, 0.9, 2.7),
            ParameterSet.fromValues(0.8, 0.625, 1.875),
            ParameterSet.fromValues(0.3, 0.925, 2.775),
            ParameterSet.fromValues(0.3, 2.05, 2.05),
            ParameterSet.fromValues(0.5, 1.8, 1.8),
            ParameterSet.fromValues(0.8, 1.95, 0.65),
            ParameterSet.fromValues(0.6, 0.85, 2.55),
            ParameterSet.fromValues(0.8, 0.7, 2.1),
            ParameterSet.fromValues(0.9, 0.85, 0.85),
            ParameterSet.fromValues(0.4, 1.9, 1.9),
            ParameterSet.fromValues(0.9, 0.9, 0.9),
            ParameterSet.fromValues(0.9, 0.4, 1.2),
            ParameterSet.fromValues(0.7, 0.75, 2.25),
            ParameterSet.fromValues(0.6, 1.95, 1.95),
            ParameterSet.fromValues(0.6, 1.65, 1.65),
            ParameterSet.fromValues(0.6, 0.95, 2.85),
            ParameterSet.fromValues(0.9, 1.2, 0.4),
            ParameterSet.fromValues(0.8, 1.2, 1.2),
            ParameterSet.fromValues(0.7, 2.4, 0.8),
            ParameterSet.fromValues(0.6, 2.85, 0.95),
            ParameterSet.fromValues(0.8, 0.6, 1.8),
            ParameterSet.fromValues(0.5, 1, 3),
            ParameterSet.fromValues(-0.1, 0.825, 2.475),
            ParameterSet.fromValues(0.9, 0.425, 1.275),
            ParameterSet.fromValues(0.1, 0.9, 2.7),
            ParameterSet.fromValues(0.3, 2, 2),
            ParameterSet.fromValues(0.5, 0.875, 2.625),
            ParameterSet.fromValues(0.7, 0.725, 2.175),
            ParameterSet.fromValues(0.4, 0.9, 2.7),
            ParameterSet.fromValues(0.9, 0.8, 0.8),
            ParameterSet.fromValues(0.8, 1.5, 1.5),
            ParameterSet.fromValues(0.7, 1.45, 1.45),
            ParameterSet.fromValues(0.9, 1.275, 0.425),
            ParameterSet.fromValues(0.7, 1.8, 1.8),
            ParameterSet.fromValues(0.6, 0.825, 2.475),
            ParameterSet.fromValues(0.9, 0.375, 1.125)
    };

}
