/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.nn.domain;

import cilib.nn.NeuralNetwork;
import cilib.type.DomainRegistry;

public interface DomainInitialisationStrategy {

    DomainRegistry initialiseDomain(NeuralNetwork neuralNetwork);

}
