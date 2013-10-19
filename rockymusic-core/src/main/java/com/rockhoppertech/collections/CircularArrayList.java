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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 * @param <E>
 */
public class CircularArrayList<E> extends ArrayList<E> implements
		CircularList<E>, Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 2099856485946476464L;

	public static void main(final String[] args) {
		final CircularArrayList<Integer> cal = new CircularArrayList<Integer>();
		// cal.add(1);
		// cal.add(2);
		// cal.add(3);
		// cal.add(4);
		// for (int i = 0; i < 10; i++) {
		// System.out.println(cal.next());
		// }
		// System.out.println();
		// for (int i = 0; i < 10; i++) {
		// System.out.println(cal.previous());
		// }

		cal.clear();
		cal.add(1);
		cal.add(2);
		cal.add(3);
		for (int i = 0; i < 10; i++) {
			System.err.println("index " + i);
			System.err.printf("value %d%n",
					cal.next());
			System.err.printf("is first %b ",
					cal.isFirst());
			System.err.printf("is last %b ",
					cal.isLast());

			System.err.println();
		}
	}

	private int index = 0;

	boolean first = true;

	boolean last = false;

	public CircularArrayList() {
	}

	public CircularArrayList(final Collection<? extends E> c) {
		super(c);
	}

	public CircularArrayList(final E[] array) {
		for (final E o : array) {
			this.add(o);
		}
	}

	public CircularArrayList(final int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * You must call previous or next before calling this. Perhaps "wasFirst"
	 * would be a better name but the JavaBean convention differs.
	 * 
	 * @return
	 */
	public boolean isFirst() {
		return this.first;
	}

	public boolean isLast() {
		return this.last;
	}

	@Override
	public E next() {
		if (this.index == size()) {
			this.index = 0;
		}

		this.updateFirstLast();

		return get(this.index++);
	}

	@Override
	public E previous() {
		if (--this.index < 0) {
			this.index = size() - 1;
		}
		this.updateFirstLast();
		return get(this.index);
	}

	@Override
	public void reset() {
		this.index = 0;
		this.first = true;
		this.last = false;
	}

	private void updateFirstLast() {
		if (this.index == size() - 1) {
			this.last = true;
		} else {
			this.last = false;
		}
		if (this.index == 0) {
			this.first = true;
		} else {
			this.first = false;
		}
	}

}
