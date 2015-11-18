/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.nn.domain;

import cilib.nn.architecture.visitors.ArchitectureVisitor;
import cilib.type.types.Type;

public interface SolutionConversionStrategy {

    //SolutionConversionStrategy initialise(NeuralNetwork neuralNetwork);

    ArchitectureVisitor interpretSolution(Type solution);
}
