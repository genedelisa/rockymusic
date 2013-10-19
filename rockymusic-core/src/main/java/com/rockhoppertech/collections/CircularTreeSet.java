package com.rockhoppertech.collections;

/*
 * #%L
 * Rocky Music Core
 * %%
 * Copyright (C) 1996 - 2013 Rockhopper Technologies
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

//TODO the previous does not start at the same place as next.

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 * @param <E>
 */
public class CircularTreeSet<E> extends TreeSet<E> {

	/**
     * 
     */
	private static final long serialVersionUID = 2099856485946476464L;

	public static void main(final String[] args) {
		final CircularTreeSet<Integer> cal = new CircularTreeSet<Integer>();

		cal.add(2);
		cal.add(3);
		cal.add(4);
		cal.add(3);
		cal.add(4);
		cal.add(1);
		System.err.println(cal);
		for (int i = 0; i < 10; i++) {
			System.err.println(cal.next());
		}
		System.out.println();
		for (int i = 0; i < 10; i++) {
			System.err.println(cal.previous());
		}
	}

	private transient Iterator<E> currentIterator = iterator();

	private transient Iterator<E> descendingIterator = descendingIterator();

	public CircularTreeSet() {
	}

	public CircularTreeSet(final Collection<? extends E> c) {
		super(c);
	}

	public CircularTreeSet(final E[] array) {
		for (final E o : array) {
			add(o);
		}
	}

	public E next() {
		if (this.currentIterator.hasNext()) {
			return this.currentIterator.next();
		} else {
			this.currentIterator = iterator();
			return this.currentIterator.next();
		}
	}

	public E previous() {
		if (this.descendingIterator.hasNext()) {
			System.err.println("has next");
			return this.descendingIterator.next();
		} else {
			System.err.println("new it");
			this.descendingIterator = descendingIterator();
			return this.descendingIterator.next();
		}
	}

	public void reset() {
		this.currentIterator = iterator();
		this.descendingIterator = descendingIterator();
	}

}
