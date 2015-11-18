/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.nn.architecture.visitors;

import cilib.util.Visitor;
import cilib.nn.architecture.Architecture;
import cilib.util.Cloneable;

/**
 * Interface extends a Visitor that visits a neural network {@link Architecture}
 */
public interface ArchitectureVisitor extends Visitor<Architecture>, Cloneable {

}
