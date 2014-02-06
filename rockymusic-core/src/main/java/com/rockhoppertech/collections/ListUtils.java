/*
 * $Id$
 *
 * Copyright 1998,1999,2000,2001 by Rockhopper Technologies, Inc.,
 * 75 Trueman Ave., Haddonfield, New Jersey, 08033-2529, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Rockhopper Technologies, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with RTI.
 */

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


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Some {@code List} utilities that seem to be missing from all the big guy's
 * list implementations.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 */
public class ListUtils {
    public static void main(String[] args) {

    }

    public static String asString(List<Double> list) {
        return asString(list, " ");
    }

    public static String asString(Set<Double> list, String delim) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(0);
        int size = list.size();
        int counter = 0;
        StringBuilder sb = new StringBuilder();
        for (Double o : list) {
            sb.append(nf.format(o));
            if (counter++ < size)
                sb.append(delim);
        }
        return sb.toString();
    }

    public static String asString(List<Double> list, String delim) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(0);
        int size = list.size();
        int counter = 0;
        StringBuilder sb = new StringBuilder();
        for (Double o : list) {
            sb.append(nf.format(o));
            if (counter++ < size)
                sb.append(delim);
        }
        return sb.toString();
    }

    /**
     * delimiter can be whitespace or ,
     * 
     * @param s
     * @return
     */
    public static List<Integer> stringToIntegerList(String s) {
        Scanner scanner = new Scanner(s);
        if (s.indexOf(',') != -1) {
            scanner.useDelimiter(",");
        }
        List<Integer> data = new ArrayList<Integer>();
        while (scanner.hasNextInt()) {
            data.add(scanner.nextInt());
        }
        scanner.close();
        return data;
    }

    public static CircularList<Integer> stringToIntegerCircularList(String s) {
        Scanner scanner = new Scanner(s);
        if (s.indexOf(',') != -1) {
            scanner.useDelimiter(",");
        }
        CircularList<Integer> data = new CircularArrayList<Integer>();
        while (scanner.hasNextInt()) {
            data.add(scanner.nextInt());
        }
        scanner.close();
        return data;
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
        scanner.close();
        return data;
    }

    public static CircularList<Double> stringToDoubleCircularList(String s) {
        Scanner scanner = new Scanner(s);
        if (s.indexOf(',') != -1) {
            scanner.useDelimiter(",");
        }
        CircularList<Double> data = new CircularArrayList<Double>();
        while (scanner.hasNextDouble()) {
            data.add(scanner.nextDouble());
        }
        scanner.close();
        return data;
    }

    public static Object asString(Collection<Number> list) {
        return asString(list, " ");
    }

    public static Object asString(Collection<Number> list, String delim) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(0);
        int size = list.size();
        int counter = 0;
        StringBuilder sb = new StringBuilder();
        for (Number o : list) {
            sb.append(nf.format(o));
            if (counter++ < size)
                sb.append(delim);
        }
        return sb.toString();
    }

    // can also do this
    public static List<String> commaDelimitedToList(String commaDelimited) {
        List<String> items = Arrays.asList(commaDelimited.split("\\s*,\\s*"));
        return items;
    }
    
    public static <T> CircularList <T> newCircularArrayList() {
        return new CircularArrayList<T>();
    }
    public static <T> CircularList <T> newCircularArrayList(CircularList<T> list) {
        return new CircularArrayList<T>(list);
    }


}
