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

package com.rockhoppertech.music.scale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ScaleComparator implements Comparator<Scale> {
    private boolean ascending = true;
    private String propertyName;

    public ScaleComparator() {
        this("name", true);
    }

    public ScaleComparator(String propertyName, boolean b) {
        this.ascending = b;
        this.propertyName = propertyName;
    }

    public void setAscending(boolean b) {
        this.ascending = b;
    }

    public void toggleAscending() {
        this.ascending = !this.ascending;
    }

    public int compare(Scale o1, Scale o2) {

        Scale c1 = o1;
        Scale c2 = o2;
        if (ascending == false) {
            c1 = o2;
            c2 = o1;
        }

        if (this.propertyName.equals("name")) {
            return c1.getName().compareTo(c2.getName());
        } else if (this.propertyName.equals("length")) {
            long a1 = c1.getLength();
            long a2 = c2.getLength();
            if (a1 == a2) {
                return 0;
            } else if (a1 > a2) {
                return 1;
            } else {
                return -1;
            }

        } else if (this.propertyName.equals("degrees")) {
        } else if (this.propertyName.equals("degreesAsPitches")) {
        } else if (this.propertyName.equals("degreesAsString")) {
        } else if (this.propertyName.equals("descendingDifferent")) {
        } else if (this.propertyName.equals("descendingIntervals")) {
        } else if (this.propertyName.equals("descendingIntervalsAsString")) {
        } else if (this.propertyName.equals("description")) {
        } else if (this.propertyName.equals("intervals")) {
            int[] a1 = c1.getIntervals();
            int[] a2 = c2.getIntervals();
            return compareArrays(a1, a2);
        } else if (this.propertyName.equals("intervalsAsString")) {
        } else if (this.propertyName.equals("octave")) {
        } else if (this.propertyName.equals("spelling")) {
        }

        // make the compiler happy
        return 0;
    }

    /**
    *
    * @param a1
    * @param a2
    */
    private int compareArrays(int[] a1, int[] a2) {
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

    public boolean equals(Object obj) {
        return true;
    }

    public static void main(String[] args) {
        List<Scale> scales = new ArrayList<Scale>();
        scales = ScaleFactory.getAll();
        //        Scale s = ScaleFactory.getScaleByName("Raga Hamsa Vinodini");
        //       scales.add(s);
        //        System.err.println("created " + s);
        //        s = ScaleFactory.getScaleByName("Harmonic Minor");
        //        scales.add(s);
        //        System.err.println("created " + s);
        //        
        //        s = ScaleFactory.getScaleByName("Hungarian Gypsy");
        //        scales.add(s);
        //        System.err.println("created " + s);
        //        
        //        
        //        s = ScaleFactory.getScaleByName("Persian");
        //        scales.add(s);
        //        System.err.println("created " + s);

        Collections.sort(scales, new ScaleComparator());
        System.err.println("Done sorting");
        for (Scale sc : scales) {
            System.err.println(sc.getName());
            System.err.println(sc.getIntervalsAsString());
            System.err.println();
        }
        
        Collections.sort(scales, new ScaleComparator("intervals", true));
        System.err.println("Done sorting by intervals");
        for (Scale sc : scales) {
            System.err.println(sc.getName());
            System.err.println(sc.getIntervalsAsString());
            System.err.println();
        }

    }
}

/*
 * History:
 *
 * $Log$
 *
 * This version: $Revision$
 * Last modified: $Date$
 * Last modified by: $Author$
 */
