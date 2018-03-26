/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove, Ben Choi
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package util.lab.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.wustl.cse231s.NotYetImplementedException;
import net.jcip.annotations.NotThreadSafe;

/**
 * @author __STUDENT_NAME__
 * @author Ben Choi (benjaminchoi@wustl.edu)
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@NotThreadSafe
/* package-private */ class LinkedNodesIterator<E> implements Iterator<E> {
	private LinkedNode<E> curr;
	private LinkedNode<E> prevNode;
	private boolean move;
	private LinkedNodesCollection<E> collectionList;
	public LinkedNodesIterator(LinkedNodesCollection<E> collection) {
		curr = collection.getHeadNode();
		prevNode = null;
		move = false;
		collectionList = collection;		
	}

	/**
	 * {@inheritDoc}
	 * a function to check if the current node has next node
	 */
	@Override
	public boolean hasNext() {			
		return curr.getNext() != null;
	}

	/**
	 * {@inheritDoc}
	 * the next function of iterator, if current node or next node is null, throw no such element exception
	 * else, update the current and previous node to reflect the move. and set move boolean to true
	 */
	@Override
	public E next() {
		move = true;
		if (curr == null || curr.getNext() == null) {
			throw new NoSuchElementException();
		} 
	
		E val = curr.getNext().getValue();
		prevNode = curr;
		curr = curr.getNext();
		return val;
	}

	/**
	 * {@inheritDoc}
	 * iterator remove function, if haven't called move yet, or current node or previous node null, throw a illegalstateexception
	 * else, remove currentnode, set move to false, decrease size
	 */
	@Override
	public void remove() {
		if (!move) {
			throw new IllegalStateException();
		}
		if (curr == null || prevNode == null) {
			//return;
			throw new IllegalStateException();
		}
		else {
			prevNode.setNext(curr.getNext());
			curr = prevNode;
			collectionList.decrementSize();
			move = false;
		}
	}
}
