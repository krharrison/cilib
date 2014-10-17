/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.behaviour.generator;

import net.sourceforge.cilib.entity.behaviour.Behaviour;

/**
 *
 */
public interface BehaviourGenerator extends Cloneable {

	public BehaviourGenerator getClone();
	
	public Behaviour generate();
}
