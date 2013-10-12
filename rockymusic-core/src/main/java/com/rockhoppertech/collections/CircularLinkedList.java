package com.rockhoppertech.collections;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 * @param <E>
 */
public class CircularLinkedList<E> extends LinkedList<E> implements CircularList<E> {

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

    public E next() {
        if (this.index == this.size()) {
            this.index = 0;
        }
        return this.get(this.index++);
    }

    public E previous() {
        if (--this.index < 0) {
            this.index = this.size() - 1;
        }
        return this.get(this.index);
    }
    
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
