package com.rockhoppertech.collections;

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

    private Iterator<E> currentIterator = this.iterator();

    private Iterator<E> descendingIterator = this.descendingIterator();

    public CircularTreeSet() {
    }

    public CircularTreeSet(final Collection<? extends E> c) {
        super(c);
    }

    public CircularTreeSet(final E[] array) {
        for (final E o : array) {
            this.add(o);
        }
    }

    public E next() {
        if (this.currentIterator.hasNext()) {
            return this.currentIterator.next();
        } else {
            this.currentIterator = this.iterator();
            return this.currentIterator.next();
        }
    }

    public E previous() {
        if (this.descendingIterator.hasNext()) {
            System.err.println("has next");
            return this.descendingIterator.next();
        } else {
            System.err.println("new it");
            this.descendingIterator = this.descendingIterator();
            return this.descendingIterator.next();
        }
    }

    public void reset() {
        this.currentIterator = this.iterator();
        this.descendingIterator = this.descendingIterator();
    }

}
