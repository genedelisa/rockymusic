package com.rockhoppertech.collections;

/*
 * #%L
 * Rocky Music Core
 * %%
 * Copyright (C) 1996 - 2014 Rockhopper Technologies
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

import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class CircularTreeSetTest {
    private static final Logger logger = LoggerFactory
            .getLogger(CircularTreeSetTest.class);

    @Test
    public void testCircularTreeSet() {
        Set<Integer> list = new CircularTreeSet<Integer>();
        assertThat(
                "list is not null",
                list,
                notNullValue());
        assertThat(
                "list length is correct",
                list.size(),
                equalTo(0));
    }

    @Test
    public void testCircularTreeSetCollectionOfQextendsE() {
        Set<Integer> orig = Sets.newTreeSet();
        orig.add(1);
        orig.add(2);
        orig.add(3);
        Set<Integer> list = new CircularTreeSet<Integer>(orig);
        logger.debug("the list:\n{}", list);
        assertThat("orig is not null",
                orig,
                notNullValue());
        assertThat("list is not null",
                list,
                notNullValue());
        assertThat("list length is correct",
                list.size(),
                equalTo(orig.size()));
        assertThat("list contents is correct",
                list,
                equalTo(orig));

    }

    @Test
    public void testNext() {
        Integer[] a = new Integer[] { 1, 2, 3 };
        CircularTreeSet<Integer> list = new CircularTreeSet<Integer>(a);

        int expected = 1;
        int result = list.next();
        assertThat("next is correct",
                result,
                equalTo(expected));

        expected = 2;
        result = list.next();
        assertThat("next is correct",
                result,
                equalTo(expected));

        expected = 3;
        result = list.next();
        assertThat("next is correct",
                result,
                equalTo(expected));

        expected = 1;
        result = list.next();
        assertThat("next is correct",
                result,
                equalTo(expected));

        expected = 2;
        result = list.next();
        assertThat("next is correct",
                result,
                equalTo(expected));

        expected = 3;
        result = list.next();
        assertThat("next is correct",
                result,
                equalTo(expected));
    }

    @Test
    public void testPrevious() {
        Integer[] a = new Integer[] { 1, 2, 3 };
        CircularTreeSet<Integer> list = new CircularTreeSet<Integer>(a);

        int expected = 3;
        int result = list.previous();
        assertThat("previous is correct",
                result,
                equalTo(expected));

        expected = 2;
        result = list.previous();
        assertThat("previous is correct",
                result,
                equalTo(expected));

        expected = 1;
        result = list.previous();
        assertThat("previous is correct",
                result,
                equalTo(expected));

        expected = 3;
        result = list.previous();
        assertThat("previous is correct",
                result,
                equalTo(expected));

        expected = 2;
        result = list.previous();
        assertThat("previous is correct",
                result,
                equalTo(expected));

        expected = 1;
        result = list.previous();
        assertThat("previous is correct",
                result,
                equalTo(expected));
    }

    @Test
    public void testReset() {
        Integer[] a = new Integer[] { 1, 2, 3 };
        CircularTreeSet<Integer> list = new CircularTreeSet<Integer>(a);

        int expected = 1;
        int result = list.next();
        assertThat("next is correct",
                result,
                equalTo(expected));

        expected = 2;
        result = list.next();
        assertThat("next is correct",
                result,
                equalTo(expected));

        list.reset();

        expected = 1;
        result = list.next();
        assertThat("next is correct",
                result,
                equalTo(expected));

    }

}
