package com.rockhoppertech.collections;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class CircularArrayListTest {
    private static final Logger logger = LoggerFactory
            .getLogger(CircularArrayListTest.class);

    @Test
    public void testCircularArrayList() {
        List<Integer> list = new CircularArrayList<Integer>();
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
    public void testCircularArrayListCollectionOfQextendsE() {
        List<Integer> orig = Lists.newArrayList(1, 2, 3);
        List<Integer> list = new CircularArrayList<Integer>(orig);
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
    public void testCircularArrayListEArray() {
        Integer[] a = new Integer[] { 1, 2, 3 };
        List<Integer> list = new CircularArrayList<Integer>(a);
        assertThat("list is not null",
                list,
                notNullValue());
        assertThat("list length is correct",
                list.size(),
                equalTo(a.length));

        List<Integer> expected = Lists.newArrayList(a);
        assertThat("list contents is correct",
                list,
                equalTo(expected));

    }

    @Test
    public void testCircularArrayListInt() {
        // initial capacity
        List<Integer> list = new CircularArrayList<Integer>(5);
        assertThat("list is not null",
                list,
                notNullValue());
    }

    @Test
    public void testIsFirst() {
        Integer[] a = new Integer[] { 1, 2, 3 };
        CircularArrayList<Integer> list = new CircularArrayList<Integer>(a);
        logger.debug("the list:\n{}", list);
        
        boolean expected = true;
        assertThat("isFirst is correct",
                list.isFirst(),
                equalTo(expected));

        Integer i = list.next();
        expected = false;
        assertThat("isFirst is correct",
                list.isFirst(),
                equalTo(expected));
        assertThat("first is 1",
                i,
                equalTo(1));

    }

    @Test
    public void testIsLast() {
        Integer[] a = new Integer[] { 1, 2, 3 };
        CircularArrayList<Integer> list = new CircularArrayList<Integer>(a);

        boolean expected = false;
        assertThat("isLast is correct",
                list.isLast(),
                equalTo(expected));

        Integer i = list.next();
        assertThat("isLast is correct",
                list.isLast(),
                equalTo(expected));
        assertThat("value is 1",
                i,
                equalTo(1));

        i = list.next();
        assertThat("isLast is correct",
                list.isLast(),
                equalTo(expected));
        assertThat("value is 2",
                i,
                equalTo(2));

        i = list.next();
        expected = true;
        assertThat("isLast is correct",
                list.isLast(),
                equalTo(expected));
        assertThat("value is 3",
                i,
                equalTo(3));

    }

    @Test
    public void testNext() {
        Integer[] a = new Integer[] { 1, 2, 3 };
        CircularArrayList<Integer> list = new CircularArrayList<Integer>(a);

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
        CircularArrayList<Integer> list = new CircularArrayList<Integer>(a);

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
        CircularArrayList<Integer> list = new CircularArrayList<Integer>(a);

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
