package com.rockhoppertech.collections;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ArrayFunctionsTest extends ArrayFunctions {
    private static final Logger logger = LoggerFactory
            .getLogger(ArrayFunctionsTest.class);

    @Test
    public void testDumpName() {
        int[] a = new int[] { 1, 2, 3 };
        ArrayFunctions.dumpName(a);
        // just prints to stdout

    }

    @Test
    public void testPrintArrayObject() {
        int[] a = new int[] { 1, 2, 3 };
        // prints 1 2 3 to stdout
        ArrayFunctions.printArray(a);
    }

    @Test
    public void testPrintArrayObjectPrintWriter() {
        int[] a = new int[] { 1, 2, 3 };
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ArrayFunctions.printArray(a, pw);
        String s = sw.toString();
        assertThat("string is not null",
                s, notNullValue());
        assertThat("string content is correct",
                s, equalTo("1 2 3 "));
    }

    @Test
    public void testPrintArrayObjectLogger() {
        int[] a = new int[] { 1, 2, 3 };
        ArrayFunctions.printArray(a, logger);
    }

    @Test
    public void testHexprint() {
        int[] a = new int[] { 1, 2, 3, 10, 11, 12, 13, 14, 15, 16, 17 };
        ArrayFunctions.hexprint(a);
        // writes to stdout
        // 1 2 3 A B C D E F 10 11
    }

    @Test
    public void testToStringObject() {
        int[] a = new int[] { 1, 2, 3 };
        String s = ArrayFunctions.toString(a);
        logger.debug("returned '{}'", s);
        assertThat("string is not null",
                s, notNullValue());
        assertThat("string content is correct",
                s, equalTo("1 2 3 "));
    }

    @Test
    public void testToStringObjectString() {
        int[] a = new int[] { 1, 2, 3 };
        // delimited
        String s = ArrayFunctions.toString(a, ",");
        logger.debug("returned '{}'", s);
        assertThat("string is not null",
                s, notNullValue());
        assertThat("string content is correct",
                s, equalTo("1,2,3,"));
    }

    @Test
    public void testPrintArrayAsJava() {
        int[] a = new int[] { 1, 2, 3 };
        ArrayFunctions.printArrayAsJava(a, "arrayString");
        // prints to stdout
        // int[] arrayString = {1, 2, 3};
    }

    @Test
    public void testMin() {
        double[] a = new double[] { 1d, 2d, 3d };
        double min = ArrayFunctions.min(a);
        assertThat("min is correct",
                min, equalTo(1d));
    }

    @Test
    public void testMax() {
        double[] a = new double[] { 1d, 2d, 3d };
        double max = ArrayFunctions.max(a);
        assertThat("max is correct",
                max, equalTo(3d));

        int[] ia = new int[] { 1, 2, 3 };
        int imax = ArrayFunctions.max(ia);
        assertThat("int max is correct",
                imax, equalTo(3));
    }

    @Test
    public void testCompare() {
        int[] a1 = new int[] { 1, 2, 3 };
        int[] a2 = new int[] { 1, 2, 3 };
        int result = ArrayFunctions.compare(a1, a2);
        assertThat("= comparison is correct",
                result, equalTo(0));

        a1 = new int[] { 1, 2, 3 };
        a2 = new int[] { 4, 5, 6 };
        result = ArrayFunctions.compare(a1, a2);
        assertThat("< comparison is correct",
                result, equalTo(-1));

        a1 = new int[] { 4, 5, 6 };
        a2 = new int[] { 1, 2, 3 };
        result = ArrayFunctions.compare(a1, a2);
        assertThat("> comparison is correct",
                result, equalTo(1));
    }

    @Test
    public void testAsStringDoubleArray() {
        double[] da = new double[] { 1d, 2d, 3d };
        String result = ArrayFunctions.asString(da);
        assertThat("string is correct",
                result, equalTo("1.0 2.0 3.0"));
    }

    @Test
    public void testAsStringDoubleArrayChar() {
        double[] da = new double[] { 1d, 2d, 3d };
        String result = ArrayFunctions.asString(da, ',');
        assertThat("string is correct",
                result, equalTo("1.0,2.0,3.0"));
    }

    @Test
    public void testAsStringIntArray() {
        int[] a = new int[] { 1, 2, 3 };
        String result = ArrayFunctions.asString(a);
        assertThat("string is correct",
                result, equalTo("1 2 3"));
    }

    @Test
    public void testAsStringIntArrayChar() {
        int[] a = new int[] { 1, 2, 3 };
        String result = ArrayFunctions.asString(a, ',');
        assertThat("string is correct",
                result, equalTo("1,2,3"));
    }

    @Test
    public void testAsStringIntegerArray() {
        Integer[] a = new Integer[] { 1, 2, 3 };
        String result = ArrayFunctions.asString(a);
        assertThat("string is correct",
                result, equalTo("1 2 3"));
    }

    @Test
    public void testAsStringIntegerArrayChar() {
        Integer[] a = new Integer[] { 1, 2, 3 };
        String result = ArrayFunctions.asString(a, ',');
        assertThat("string is correct",
                result, equalTo("1,2,3"));
    }

    @Test
    public void testToPrimitive() {
        Integer[] ia = new Integer[] { 1, 2, 3 };
        int[] a = ArrayFunctions.toPrimitive(ia);
        assertThat("primitive array is not null",
                a, notNullValue());
        int[] expected = new int[] { 1, 2, 3 };
        assertThat("primitive content is correct",
                a, equalTo(expected));

        // assertThat("primitive content is correct",
        // a, hasItemsArray(expected));

        // assertThat("primitive content is correct",
        // a, IsArrayContainingInOrder(ia));

        // assertThat(
        // "",
        // a,
        // hasItems(expected.toArray(new Integer[expected.size()])));

        // assertThat(a, hasItemInArray(1));

    }

    @Test
    public void testStringToIntegerArray() {
        Integer[] expected = new Integer[] { 1, 2, 3 };
        Integer[] result = ArrayFunctions.stringToIntegerArray("1 2 3");
        assertThat("string is correct",
                result, equalTo(expected));
    }

    @Test
    public void testStringToDoubleArray() {
        Double[] expected = new Double[] { 1d, 2d, 3d };
        Double[] result = ArrayFunctions.stringToDoubleArray("1 2 3");
        assertThat("string is correct",
                result, equalTo(expected));
    }

    @Test
    public void testStringToDoubleList() {
        List<Double> result = ArrayFunctions.stringToDoubleList("1 2 3");
        List<Double> expected = new ArrayList<Double>();
        expected.add(1d);
        expected.add(2d);
        expected.add(3d);
        assertThat("list is correct",
                result, equalTo(expected));
    }

    @Test
    public void testWrap() {
        double[] da = new double[] { 1d, 2d, 3d };
        Double[] result = ArrayFunctions.wrap(da);
        Double[] expected = new Double[] { 1d, 2d, 3d };
        assertThat("wrap is correct",
                result, equalTo(expected));
    }

    @Test
    public void testUnwrap() {
        Double[] da = new Double[] { 1d, 2d, 3d };
        double[] result = ArrayFunctions.unwrap(da);
        double[] expected = new double[] { 1d, 2d, 3d };
        assertThat("unwrap is correct",
                result, equalTo(expected));
    }

    @Test
    public void testContains() {
        int[] a = new int[] { 1, 2, 3 };
        boolean result = ArrayFunctions.contains(a, 1);
        boolean expected = true;
        assertThat("contains is correct",
                result, equalTo(expected));

        result = ArrayFunctions.contains(a, 42);
        expected = false;
        assertThat("contains is correct",
                result, equalTo(expected));
    }

    @Test
    public void testSum() {
        int[] a = new int[] { 1, 2, 3 };
        int result = ArrayFunctions.sum(a, 0, a.length - 1);
        int expected = 6;
        assertThat("sum is correct",
                result, equalTo(expected));

        result = ArrayFunctions.sum(a, 1, a.length - 1);
        expected = 5;
        assertThat("sum is correct",
                result, equalTo(expected));

        result = ArrayFunctions.sum(a, 0, 1);
        expected = 3;
        assertThat("sum is correct",
                result, equalTo(expected));
    }

    @Test
    public void testAppendCopy() {
        int[] a = new int[] { 1, 2, 3 };
        int[] expected = new int[] { 1, 2, 3, 1, 2, 3 };
        int[] result = ArrayFunctions.appendCopy(a);
        assertThat("append is correct",
                result, equalTo(expected));
    }

}
