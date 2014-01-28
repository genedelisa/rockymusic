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
        Set<Integer> set = new CircularTreeSet<Integer>();
        logger.debug("the set:\n{}", set);
        assertThat(
                "set is not null",
                set,
                notNullValue());
        assertThat(
                "set length is correct",
                set.size(),
                equalTo(0));
    }

    @Test
    public void testCircularTreeSetCollectionOfQextendsE() {
        Set<Integer> orig = Sets.newTreeSet();
        orig.add(1);
        orig.add(2);
        orig.add(3);
        CircularTreeSet<Integer> set = new CircularTreeSet<Integer>(orig);
        logger.debug("the set:\n{}", set);
        assertThat("orig is not null",
                orig,
                notNullValue());
        assertThat("set is not null",
                set,
                notNullValue());
        assertThat("set length is correct",
                set.size(),
                equalTo(orig.size()));
        assertThat("set contents is correct",
                set,
                equalTo(orig));

    }

    @Test
    public void testCircularTreeSetArrayOfQextendsE() {
        Integer[] orig = { 1, 2, 3 };

        CircularTreeSet<Integer> set = new CircularTreeSet<Integer>(orig);
        logger.debug("the set:\n{}", set);
        assertThat("orig is not null",
                orig,
                notNullValue());
        assertThat("set is not null",
                set,
                notNullValue());
        assertThat("set length is correct",
                set.size(),
                equalTo(orig.length));
        for (Integer i : orig) {
            assertThat("set contents is correct",
                    set.next(),
                    equalTo(i));
        }
    }
    
    

    @Test
    public void testNext() {
        Integer[] a = new Integer[] { 1, 2, 3, 1, 2, 3 };
        CircularTreeSet<Integer> set = new CircularTreeSet<Integer>(a);
        logger.debug("the set:\n{}", set);

        int expected = 1;
        int result = set.next();
        assertThat("next is correct",
                result,
                equalTo(expected));

        expected = 2;
        result = set.next();
        assertThat("next is correct",
                result,
                equalTo(expected));

        expected = 3;
        result = set.next();
        assertThat("next is correct",
                result,
                equalTo(expected));

        expected = 1;
        result = set.next();
        assertThat("next is correct",
                result,
                equalTo(expected));

        expected = 2;
        result = set.next();
        assertThat("next is correct",
                result,
                equalTo(expected));

        expected = 3;
        result = set.next();
        assertThat("next is correct",
                result,
                equalTo(expected));
    }

    @Test
    public void testPrevious() {
        Integer[] a = new Integer[] { 1, 2, 3 };
        CircularTreeSet<Integer> set = new CircularTreeSet<Integer>(a);
        logger.debug("the set:\n{}", set);

        int expected = 3;
        int result = set.previous();
        assertThat("previous is correct",
                result,
                equalTo(expected));

        expected = 2;
        result = set.previous();
        assertThat("previous is correct",
                result,
                equalTo(expected));

        expected = 1;
        result = set.previous();
        assertThat("previous is correct",
                result,
                equalTo(expected));

        expected = 3;
        result = set.previous();
        assertThat("previous is correct",
                result,
                equalTo(expected));

        expected = 2;
        result = set.previous();
        assertThat("previous is correct",
                result,
                equalTo(expected));

        expected = 1;
        result = set.previous();
        assertThat("previous is correct",
                result,
                equalTo(expected));
    }

    @Test
    public void testReset() {
        Integer[] a = new Integer[] { 1, 2, 3 };
        CircularTreeSet<Integer> set = new CircularTreeSet<Integer>(a);
        logger.debug("the set:\n{}", set);

        int expected = 1;
        int result = set.next();
        assertThat("next is correct",
                result,
                equalTo(expected));

        expected = 2;
        result = set.next();
        assertThat("next is correct",
                result,
                equalTo(expected));

        set.reset();

        expected = 1;
        result = set.next();
        assertThat("next is correct",
                result,
                equalTo(expected));

    }

}
