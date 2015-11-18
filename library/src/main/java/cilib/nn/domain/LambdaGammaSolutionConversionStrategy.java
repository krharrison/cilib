/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.nn.domain;

import cilib.nn.architecture.visitors.ArchitectureVisitor;
import cilib.nn.architecture.visitors.LambdaGammaVisitor;
import cilib.type.types.Type;
import cilib.type.types.container.Vector;

public class LambdaGammaSolutionConversionStrategy implements SolutionConversionStrategy {

    /*@Override
    public SolutionConversionStrategy initialise(NeuralNetwork neuralNetwork) {
        return this;
    }*/

    @Override
    public ArchitectureVisitor interpretSolution(Type solution) {
        return new LambdaGammaVisitor((Vector)solution);
    }
}
