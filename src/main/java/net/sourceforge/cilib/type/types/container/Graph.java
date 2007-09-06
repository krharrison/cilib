/*
 * Graph.java
 * 
 * Copyright (C) 2004 - CIRG@UP 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.type.types.container;

public interface Graph<E> extends Structure<E> {
	
	/**
	 * Determine the number of vertices contained within the current <tt>Graph</tt>
	 * structure.
	 * @return The number of contained vertices.
	 */
	public int vertices();
	
	/**
	 * Determine the number of edges contained within the current <tt>Graph</tt>
	 * structure.
	 * @return The number of contained edges.
	 */
	public int edges();
	
	/**
	 * Create an edge / link between the given two vertices.
	 * @param a The vertex the edge should emanate from.
	 * @param b The vertex the edge should be incident to.
	 * @return <tt>true</tt> if the edge was created, <tt>false</tt> otherwise.
	 */
	public boolean addEdge(E a, E b);
	
	/**
	 * Determine if <tt>a</tt> and <tt>b</tt> are connected by an edge.
	 * @param a The vertex the edge should be emanating from.
	 * @param b The vertex the edge should be incident to.
	 * @return <tt>true</tt> if an edge is defined, <tt>false</tt> otherwise.
	 */
	public boolean isConnected(E a, E b);

}