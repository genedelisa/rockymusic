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
import java.util.LinkedList;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 * @param <E>
 */
public class CircularLinkedList<E> extends LinkedList<E> implements
		CircularList<E> {

	/**
     * 
     */
	private static final long serialVersionUID = -7528652646990925519L;

	public CircularLinkedList() {
	}

	public CircularLinkedList(Collection<? extends E> c) {
		super(c);
	}

	private int index = 0;

	@Override
	public E next() {
		if (this.index == size()) {
			this.index = 0;
		}
		return get(this.index++);
	}

	@Override
	public E previous() {
		if (--this.index < 0) {
			this.index = size() - 1;
		}
		return get(this.index);
	}

	@Override
	public void reset() {
		this.index = 0;
	}

	public static void main(String[] args) {
		CircularLinkedList<Integer> cal = new CircularLinkedList<Integer>();
		cal.add(1);
		cal.add(2);
		cal.add(3);
		cal.add(4);
		for (int i = 0; i < 10; i++) {
			System.out.println(cal.next());
		}
		System.out.println();
		for (int i = 0; i < 10; i++) {
			System.out.println(cal.previous());
		}
	}

}
