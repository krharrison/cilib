/*
 * GeneralTree.java
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.container.visitor.PreOrderVisitorDecorator;
import net.sourceforge.cilib.container.visitor.Visitor;

public class GeneralTree<E extends Comparable<E>> extends AbstractTree<E> {
	private static final long serialVersionUID = 3453326928796685749L;
	
	private List<Tree<E>> nodes;
	
	public GeneralTree() {
		key = null;
		nodes = new ArrayList<Tree<E>>();
	}
	
	public GeneralTree(E element) {
		this();
		this.key = element;
	}
	
	public GeneralTree<E> clone() {
		return null;
	}

	public boolean addSubTree(Tree<E> subtree) {
		if (subtree == null)
			throw new IllegalArgumentException("Cannot add a null object as a child of a tree");
	
		if (getKey() == null)
			throw new IllegalStateException("Cannot add a subtree to a tree with a null for the key value");
		
		this.nodes.add(subtree);
		return true;
	}

	/**
	 * The number of edges emanating from this tree node. Also known as the
	 * degree of the tree.
	 */
	public int edges() {
		return this.nodes.size();
	}

	/**
	 * Create an empty tree object and add it to the current tree node.
	 */
	public boolean add(E element) {
		Tree<E> subTree = new GeneralTree<E>(element);
		return addSubTree(subTree);
	}

	public void clear() {
		this.nodes.clear();
	}

	public boolean contains(E element) {
		for (Tree<E> e : this.nodes) {
			if (e.getKey().equals(element))
				return true;
		}

		return false;
	}

	public boolean remove(E element) {
		for (Tree<E> tree : this.nodes) {
			if (tree.getKey().equals(this.key)) {
				this.nodes.remove(tree);
				return true;
			}
		}
		
		return false;
	}
	
	// TODO: Implement this method 
	public E remove(int index) {
		if (index >= this.nodes.size())
			throw new IndexOutOfBoundsException("");
		
		return this.nodes.remove(index).getKey();
	}

	public Tree<E> removeSubTree(E element) {
		//return this.remove(subTree.getKey());
		throw new UnsupportedOperationException("Implement me");
	}
	
	@Override
	public Tree<E> removeSubTree(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public int size() {
		return this.nodes.size();
	}

	public int getDimension() {
		return size();
	}

	public String getRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}

	public void randomise() {
		// TODO Auto-generated method stub
		
	}

	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public boolean isConnected(E a, E b) {
		return false;
	}

	public void accept(Visitor<E> visitor) {
		PreOrderVisitorDecorator<E> preOrder = new PreOrderVisitorDecorator<E>(visitor);
		depthFirstTraversal(preOrder);
	}
	
	public boolean addAll(Structure<E> structure) {
		for (E e : structure)
			add(e);

		return true;
	}

	public Iterator<E> iterator() {
		return null;
	}

	public boolean removeAll(Structure<E> structure) {
		for (E e : structure) {
			this.remove(e);
		}
		return true;
	}

	public boolean isInsideBounds() {
		// TODO Auto-generated method stub
		return false;
	}

	public Tree<E> getSubTree(E element) {
		for (Tree<E> tree : this.nodes) {
			if (tree.getKey().equals(element))
				return tree;
		}

		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tree<E> getSubTree(int index) {
		if (isEmpty()) 
			throw new UnsupportedOperationException();
		
		return this.nodes.get(index);
	}

	@Override
	public boolean isLeaf() {
		return this.nodes.size() == 0;
	}

}