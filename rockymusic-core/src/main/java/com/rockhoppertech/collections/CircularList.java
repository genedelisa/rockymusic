package com.rockhoppertech.collections;

import java.util.List;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 * @param <E>
 */
public interface CircularList<E> extends List<E> {

    public abstract E next();

    public abstract E previous();

    public abstract void reset();

}