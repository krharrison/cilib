/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.velocityprovider.generators;

import cilib.pso.velocityprovider.VelocityProvider;
/**
 *
 */
public interface VelocityProviderGenerator extends Cloneable {

	public VelocityProviderGenerator getClone();	
	
	public VelocityProvider generate();
}