/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.nn.domain;

import cilib.type.DomainRegistry;
import cilib.util.Cloneable;
/**
 * Inteface for neuron domain generators.
 */
public interface NeuronDomainProvider extends Cloneable {

    /**
     * {@inheritDoc }
     */
    @Override
    public NeuronDomainProvider getClone();

    /**
     * Generates a DomainRegistry representing the weight-space of a neuron.
     */
    public DomainRegistry generateDomain(int fanin);

}
