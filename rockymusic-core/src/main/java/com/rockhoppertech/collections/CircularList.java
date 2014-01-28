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

import java.util.List;

/**
 * A List that wraps.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 * @param <E>
 *            the type contained in the list.
 */
public interface CircularList<E> extends List<E> {

    /**
     * Get the next element, wrapping in necessary.
     * 
     * @return the next element
     */
    E next();

    /**
     * Get the previous element, wrapping in necessary.
     * 
     * @return the previous element
     */
    E previous();

    /**
     * reset the index to the beginning.
     */
    void reset();

}
