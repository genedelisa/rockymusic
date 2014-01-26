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


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.chord.ChordFactory;

/**
 * Class <code>ArrayUtils</code>
 * 
 * using a bit of introspection to create a generic array printing method. Can
 * pass any type of array.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @version $Revision$, $Date$
 * @since 1.0
 */
public class ArrayFunctions {
    private static final Logger logger = LoggerFactory
            .getLogger(ArrayFunctions.class);

    public static void main(String[] args) {

        // ragged initialized
        int[][] rag = { { 1, 2 }, { 3, 4, 5 }, { 6 } };
        ArrayFunctions.printArray(rag);

        // dynamically create the rows -- here it is ragged
        int[][] dynamic = new int[5][];
        for (int i = 0; i < dynamic.length; i++) {
            dynamic[i] = new int[i * 2];
        }
        ArrayFunctions.printArray(dynamic);

        // arrays of object references
        ArrayFunctions[] orefs = new ArrayFunctions[5];

        // they are not pointing anywhere
        System.out.println("printarrays orefs should be null");
        ArrayFunctions.printArray(orefs);

        // now they are
        for (int i = 0; i < orefs.length; i++) {
            orefs[i] = new ArrayFunctions();
        }
        System.out.println("printarrays orefs should not be null");
        ArrayFunctions.printArray(orefs);

        // anonymous arrays of each primitive type
        ArrayFunctions.printArray(new int[] { 4, 5, 6 });
        ArrayFunctions.printArray(new short[] { 4, 5, 6 });
        ArrayFunctions.printArray(new long[] { 4, 5, 6 });
        ArrayFunctions.printArray(new double[] { 4.0, 5.0, 6.0 });
        ArrayFunctions.printArray(new float[] { 4.0f, 5.0f, 6.0f });
        ArrayFunctions.printArray(new byte[] { 4, 5, 6 });
        ArrayFunctions.printArray(new char[] { '4', '5', '6' });
        ArrayFunctions.printArray(new boolean[] { true, false, true, true });

        ArrayFunctions.printArray(new int[][] { { 4, 5 }, { 6 }, { 7, 8 } });
        ArrayFunctions.printArray(new int[][][] { { { 4, 5 }, { 1 } },
                { { 2 }, { 3, 4 } } });
    }

    public static void dumpName(Object o) {
        Class<?> c = o.getClass();
        if (c.isArray()) {
            System.out.println(c.getName() + " is an array");
        } else {
            System.out.println(c.getName() + " is not an array");
        }
    }

    public static void printArray(Object o) {
        if (o.getClass().isArray() == false) {
            return;
        }

        Class<?> t = o.getClass().getComponentType();
        if (t.isPrimitive()) { // it's an array of primitives
            if (t == Integer.TYPE) {
                int[] a = (int[]) o;
                for (int i = 0; i < a.length; i++) {
                    System.out.print(a[i] + " ");
                }
                System.out.println();
            } else if (t == Boolean.TYPE) {
                boolean[] a = (boolean[]) o;
                for (int i = 0; i < a.length; i++) {
                    System.out.print(a[i] + " ");
                }
                System.out.println();
            } else if (t == Double.TYPE) {
                double[] a = (double[]) o;
                for (int i = 0; i < a.length; i++) {
                    System.out.print(a[i] + " ");
                }
                System.out.println();
            } else if (t == Float.TYPE) {
                float[] a = (float[]) o;
                for (int i = 0; i < a.length; i++) {
                    System.out.print(a[i] + " ");
                }
                System.out.println();
            } else if (t == Long.TYPE) {
                long[] a = (long[]) o;
                for (int i = 0; i < a.length; i++) {
                    System.out.print(a[i] + " ");
                }
                System.out.println();
            } else if (t == Short.TYPE) {
                short[] a = (short[]) o;
                for (int i = 0; i < a.length; i++) {
                    System.out.print(a[i] + " ");
                }
                System.out.println();
            } else if (t == Byte.TYPE) {
                byte[] a = (byte[]) o;
                for (int i = 0; i < a.length; i++) {
                    System.out.print(a[i] + " ");
                }
                System.out.println();
            }
        } else if (t.isArray()) { // its an array of arrays
            Object[] a = (Object[]) o;
            for (int i = 0; i < a.length; i++) {
                printArray(a[i]);
            }

        } else { // it's an array of objects
            Object[] a = (Object[]) o;
            for (int i = 0; i < a.length; i++) {
                System.out.print(a[i] + " ");
            }
            System.out.println();
        }
    }

    public static void printArray(Object o, PrintWriter writer) {
        if (o.getClass().isArray() == false) {
            return;
        }

        Class<?> t = o.getClass().getComponentType();
        if (t.isPrimitive()) { // it's an array of primitives
            if (t == Integer.TYPE) {
                int[] a = (int[]) o;
                for (int i = 0; i < a.length; i++) {
                    writer.print(a[i] + " ");
                }
                // writer.println();
            } else if (t == Boolean.TYPE) {
                boolean[] a = (boolean[]) o;
                for (int i = 0; i < a.length; i++) {
                    writer.print(a[i] + " ");
                }
                // writer.println();
            } else if (t == Double.TYPE) {
                double[] a = (double[]) o;
                for (int i = 0; i < a.length; i++) {
                    writer.print(a[i] + " ");
                }
                // writer.println();
            } else if (t == Float.TYPE) {
                float[] a = (float[]) o;
                for (int i = 0; i < a.length; i++) {
                    writer.print(a[i] + " ");
                }
                // writer.println();
            } else if (t == Long.TYPE) {
                long[] a = (long[]) o;
                for (int i = 0; i < a.length; i++) {
                    writer.print(a[i] + " ");
                }
                // writer.println();
            } else if (t == Short.TYPE) {
                short[] a = (short[]) o;
                for (int i = 0; i < a.length; i++) {
                    writer.print(a[i] + " ");
                }
                // writer.println();
            } else if (t == Byte.TYPE) {
                byte[] a = (byte[]) o;
                for (int i = 0; i < a.length; i++) {
                    writer.print(a[i] + " ");
                }
                // writer.println();
            }
        } else if (t.isArray()) { // its an array of arrays
            Object[] a = (Object[]) o;
            for (int i = 0; i < a.length; i++) {
                printArray(a[i]);
            }

        } else { // it's an array of objects
            Object[] a = (Object[]) o;
            for (int i = 0; i < a.length; i++) {
                writer.print(a[i] + " ");
            }
            // writer.println();
        }
    }

    public static void printArray(Object o, Logger logger) {
        if (o.getClass().isArray() == false) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        Class<?> t = o.getClass().getComponentType();
        if (t.isPrimitive()) { // it's an array of primitives
            if (t == Integer.TYPE) {
                int[] a = (int[]) o;
                for (int i = 0; i < a.length; i++) {
                    sb.append(a[i]).append(' ');
                }
                // logger.debugln();
            } else if (t == Boolean.TYPE) {
                boolean[] a = (boolean[]) o;
                for (int i = 0; i < a.length; i++) {
                    sb.append(a[i]).append(' ');
                }
                // logger.debugln();
            } else if (t == Double.TYPE) {
                double[] a = (double[]) o;
                for (int i = 0; i < a.length; i++) {
                    sb.append(a[i]).append(' ');
                }
                // logger.debugln();
            } else if (t == Float.TYPE) {
                float[] a = (float[]) o;
                for (int i = 0; i < a.length; i++) {
                    sb.append(a[i]).append(' ');
                }
                // logger.debugln();
            } else if (t == Long.TYPE) {
                long[] a = (long[]) o;
                for (int i = 0; i < a.length; i++) {
                    sb.append(a[i]).append(' ');
                }
                // logger.debugln();
            } else if (t == Short.TYPE) {
                short[] a = (short[]) o;
                for (int i = 0; i < a.length; i++) {
                    sb.append(a[i]).append(' ');
                }
                // logger.debugln();
            } else if (t == Byte.TYPE) {
                byte[] a = (byte[]) o;
                for (int i = 0; i < a.length; i++) {
                    sb.append(a[i]).append(' ');
                }
                // logger.debugln();
            }
            logger.debug(sb.toString());
        } else if (t.isArray()) { // its an array of arrays
            Object[] a = (Object[]) o;
            for (int i = 0; i < a.length; i++) {
                printArray(a[i], logger);
            }

        } else { // it's an array of objects
            Object[] a = (Object[]) o;
            for (int i = 0; i < a.length; i++) {
                logger.debug(a[i] + " ");
            }

        }
    }

    static void hexprint(int[] a) {
        for (int i = 0; i < a.length; i++) {
            String s = Integer.toHexString(a[i]) + " ";
            System.out.print(s.toUpperCase());
        }
        System.out.println();
    }

    public static String toString(Object o) {
        return toString(o, " ");
    }

    public static String toString(Object o, String delim) {
        if (o.getClass().isArray() == false) {
            System.err.println(o.getClass());
            throw new IllegalArgumentException();
        }
        StringBuffer sb = new StringBuffer();
        Class<?> t = o.getClass().getComponentType();
        if (t.isPrimitive()) { // it's an array of primitives
            if (t == Integer.TYPE) {
                int[] a = (int[]) o;
                for (int i = 0; i < a.length; i++) {
                    sb.append(a[i]).append(delim);
                }
            } else if (t == Boolean.TYPE) {
                boolean[] a = (boolean[]) o;
                for (int i = 0; i < a.length; i++) {
                    sb.append(a[i]).append(delim);
                }
            } else if (t == Double.TYPE) {
                double[] a = (double[]) o;
                for (int i = 0; i < a.length; i++) {
                    sb.append(a[i]).append(delim);
                }
            } else if (t == Float.TYPE) {
                float[] a = (float[]) o;
                for (int i = 0; i < a.length; i++) {
                    sb.append(a[i]).append(delim);
                }
            } else if (t == Long.TYPE) {
                long[] a = (long[]) o;
                for (int i = 0; i < a.length; i++) {
                    sb.append(a[i]).append(delim);
                }
            } else if (t == Short.TYPE) {
                short[] a = (short[]) o;
                for (int i = 0; i < a.length; i++) {
                    sb.append(a[i]).append(delim);
                }
            } else if (t == Byte.TYPE) {
                byte[] a = (byte[]) o;
                for (int i = 0; i < a.length; i++) {
                    sb.append(a[i]).append(delim);
                }
            }
            return sb.toString();
        } else if (t.isArray()) { // its an array of arrays
            Object[] a = (Object[]) o;
            sb = new StringBuffer();
            for (int i = 0; i < a.length; i++) {
                sb.append(toString(a[i]));
            }
            return sb.toString();

        } else { // it's an array of objects
            Object[] a = (Object[]) o;
            sb = new StringBuffer();
            for (int i = 0; i < a.length; i++) {
                sb.append(a[i]).append(delim);
            }
            return sb.toString();
        }
    }

    public static void printArrayAsJava(Object o, String varname) {
        if (o.getClass().isArray() == false) {
            return;
        }

        Class<?> t = o.getClass().getComponentType();
        if (t.isPrimitive()) { // it's an array of primitives
            if (t == Integer.TYPE) {
                int[] a = (int[]) o;
                System.out.print("int[] ");
                System.out.print(varname);
                System.out.print(" = {");
                for (int i = 0; i < a.length; i++) {
                    System.out.print(a[i]);
                    if (i < a.length - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println("};");
            } else if (t == Boolean.TYPE) {
                System.out.print("boolean[] a = {");
                boolean[] a = (boolean[]) o;
                for (int i = 0; i < a.length; i++) {
                    System.out.print(a[i]);
                    if (i < a.length - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println(" };");
            } else if (t == Double.TYPE) {
                System.out.print("double[] a = {");
                double[] a = (double[]) o;
                for (int i = 0; i < a.length; i++) {
                    System.out.print(a[i]);
                    if (i < a.length - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println(" };");
            } else if (t == Float.TYPE) {
                System.out.print("float[] a = {");
                float[] a = (float[]) o;
                for (int i = 0; i < a.length; i++) {
                    System.out.print(a[i]);
                    if (i < a.length - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println(" };");
            } else if (t == Long.TYPE) {
                System.out.print("long[] a = {");
                long[] a = (long[]) o;
                for (int i = 0; i < a.length; i++) {
                    System.out.print(a[i] + ", ");
                }
                System.out.println(" };");
            } else if (t == Short.TYPE) {
                System.out.print("short[] a = {");
                short[] a = (short[]) o;
                for (int i = 0; i < a.length; i++) {
                    System.out.print(a[i] + ", ");
                }
                System.out.println(" };");
            } else if (t == Byte.TYPE) {
                System.out.print("byte[] a = {");
                byte[] a = (byte[]) o;
                for (int i = 0; i < a.length; i++) {
                    System.out.print(a[i] + ", ");
                }
                System.out.println(" };");
            }
        } else if (t.isArray()) { // its an array of arrays
            Object[] a = (Object[]) o;
            for (int i = 0; i < a.length; i++) {
                printArray(a[i]);
            }

        } else { // it's an array of objects
            Object[] a = (Object[]) o;
            for (int i = 0; i < a.length; i++) {
                System.out.print(a[i] + ", ");
            }
            System.out.println();
        }
    }

    static double min(double[] a) {
        double min = 0;
        double max = 0;
        for (int i = 0; i < a.length; i++) {
            min = Math.min(min, a[i]);
            max = Math.max(max, a[i]);
        }
        return min;
    }

    /**
     * Compares numbers in the two arrays. Useful for Comparators since it
     * returns the same -1 0 1 tap dance they want.
     * 
     * @param a1
     * @param a2
     * @return
     */
    public static int compare(int[] a1, int[] a2) {
        if (a1.length > a2.length) {
            return 1;
        } else if (a1.length < a2.length) {
            return -1;
        }
        if (a1.length == a2.length) {
            outer: for (int i = 0; i < a2.length; i++) {
                while (a1[i] == a2[i])
                    continue outer;

                if (a1[i] < a2[i]) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
        return 0;
    }

    public static String asString(double[] a) {
        return asString(a, ' ');
    }

    public static String asString(double[] a, char delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
            if (i < a.length - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static String asString(int[] a) {
        return asString(a, ' ');
    }

    public static String asString(int[] a, char delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
            if (i < a.length - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static String asString(Integer[] a) {
        return asString(a, ' ');
    }

    public static String asString(Integer[] a, char delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
            if (i < a.length - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static int[] toPrimitive(Integer[] a) {
        int[] aa = new int[a.length];
        int j = 0;
        for (Integer i : a) {
            aa[j++] = i.intValue();
        }
        return aa;
    }

    /**
     * delimiter can be whitespace or ,
     * 
     * @param s
     * @return
     */
    public static Integer[] stringToIntegerArray(String s) {
        Scanner scanner = new Scanner(s);
        if (s.indexOf(',') != -1) {
            scanner.useDelimiter(",");
        }
        List<Integer> data = new ArrayList<Integer>();
        while (scanner.hasNextInt()) {
            data.add(scanner.nextInt());
        }
        Integer[] r = new Integer[data.size()];
        r = (Integer[]) data.toArray(r);
        return r;
    }

    public static Double[] stringToDoubleArray(String s) {
        Scanner scanner = new Scanner(s);
        if (s.indexOf(',') != -1) {
            scanner.useDelimiter(",");
        }
        List<Double> data = new ArrayList<Double>();
        while (scanner.hasNextDouble()) {
            data.add(scanner.nextDouble());
        }
        Double[] r = new Double[data.size()];
        r = (Double[]) data.toArray(r);
        return r;
    }

    public static List<Double> stringToDoubleList(String s) {
        Scanner scanner = new Scanner(s);
        if (s.indexOf(',') != -1) {
            scanner.useDelimiter(",");
        }
        List<Double> data = new ArrayList<Double>();
        while (scanner.hasNextDouble()) {
            data.add(scanner.nextDouble());
        }
        return data;
    }

    public static Double[] wrap(double[] d) {
        Double[] a = new Double[d.length];
        for (int i = 0; i < a.length; i++) {
            a[i] = new Double(d[i]);
        }
        return a;
    }

    public static double[] unwrap(Double[] d) {
        double[] a = new double[d.length];
        for (int i = 0; i < a.length; i++) {
            a[i] = d[i].doubleValue();
        }
        return a;
    }

    /**
     * Is the value contained in the array?
     * 
     * @param array
     * @param i
     * @return
     */
    public static boolean contains(int[] array, int i) {
        for (int j = 0; j < array.length; j++) {
            if (array[j] == i)
                return true;
        }
        return false;
    }

    /**
     * return the sum of all the values in the array delimited by the specified
     * indexes.
     * 
     * @param array
     * @param from
     * @param to
     * @return
     */
    public static int sum(int[] array, int from, int to) {
        int sum = 0;
        if (from < 0 || from >= array.length) {
            String s = String.format("from=%d is bad", from);
            throw new IllegalArgumentException(s);
        }

        for (int i = from; i <= to; i++) {
            sum += array[i];
        }
        return sum;
    }

    /**
     * A copy of the array is appended to itself.
     * 
     * @param array
     * @return
     */
    public static int[] appendCopy(int[] array) {
        int[] copy = new int[array.length * 2];
        System.arraycopy(array, 0, copy, 0, array.length);
        System.arraycopy(array, 0, copy, array.length, array.length);
        return copy;
    }

    public static int max(int[] values) {
        int max = Integer.MIN_VALUE;
        for (int i : values) {
            if (i > max)
                max = i;
        }
        return max;

    }
}