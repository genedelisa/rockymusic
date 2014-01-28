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
import java.util.Set;
import java.util.TreeSet;

//TODO the previous does not start at the same place as next.

/**
 * A set that wraps its values retrieved via {@code next} and {@code previous}.
 * Based on a {@link TreeSet}.
 * 
 * @param <E>
 *            The type of the elements
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @see Collection
 * @see Set
 * @see TreeSet
 */
public class CircularTreeSet<E> extends TreeSet<E> {

    /**
     * For Serialization.
     */
    private static final long serialVersionUID = 2099856485946476464L;

    /**
     * Iterator for next.
     */
    private transient Iterator<E> currentIterator = iterator();

    /**
     * Iterator for previous.
     */
    private transient Iterator<E> descendingIterator = descendingIterator();

    /**
     * default constructor.
     */
    public CircularTreeSet() {
    }

    /**
     * Initialize the set with the elements in the provided Collection.
     * 
     * @param c
     *            another Colleciton.
     */
    public CircularTreeSet(final Collection<? extends E> c) {
        super(c);
    }

    /**
     * Adds the elements in the array. Does not clone.
     * 
     * @param array
     *            and array of values.
     */
    public CircularTreeSet(final E[] array) {
        for (final E o : array) {
            add(o);
        }
    }

    /**
     * Retrieve the next element.
     * 
     * @return the next element.
     */
    public final E next() {
        if (this.currentIterator.hasNext()) {
            return this.currentIterator.next();
        } else {
            this.currentIterator = iterator();
            return this.currentIterator.next();
        }
    }

    /**
     * Retrieve the previous element.
     * 
     * @return the previous element.
     */
    public final E previous() {
        if (this.descendingIterator.hasNext()) {
            return this.descendingIterator.next();
        } else {
            this.descendingIterator = descendingIterator();
            return this.descendingIterator.next();
        }
    }

    /**
     * Resets the iterators to the beginning of the Set.
     */
    public final void reset() {
        this.currentIterator = iterator();
        this.descendingIterator = descendingIterator();
    }

}
