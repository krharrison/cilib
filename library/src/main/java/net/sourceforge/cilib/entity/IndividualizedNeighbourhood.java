/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity;

/**
 * An entity which can define its own neighbourhood size.
 */
public interface IndividualizedNeighbourhood {
    int getNeighbourhoodSize();

    void setNeighbourhoodSize(int neighbourhoodSize);
}
