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

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Compares two Scales for sorting.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ScaleComparator implements Comparator<Scale>, Serializable {
    /**
     * For serialization.
     */
    private static final long serialVersionUID = 3503273205438516729L;
    private boolean ascending = true;
    private String propertyName;

    public ScaleComparator() {
        this("name", true);
    }

    public ScaleComparator(String propertyName, boolean b) {
        ascending = b;
        this.propertyName = propertyName;
    }

    public void setAscending(boolean b) {
        ascending = b;
    }

    public void toggleAscending() {
        ascending = !ascending;
    }

    @Override
    public int compare(Scale o1, Scale o2) {

        Scale c1 = o1;
        Scale c2 = o2;
        if (ascending == false) {
            c1 = o2;
            c2 = o1;
        }

        if (propertyName.equals("name")) {
            return c1.getName().compareTo(c2.getName());
        } else if (propertyName.equals("length")) {
            long a1 = c1.getLength();
            long a2 = c2.getLength();
            if (a1 == a2) {
                return 0;
            } else if (a1 > a2) {
                return 1;
            } else {
                return -1;
            }

//        } else if (propertyName.equals("degrees")) {
//        } else if (propertyName.equals("degreesAsPitches")) {
//        } else if (propertyName.equals("degreesAsString")) {
//        } else if (propertyName.equals("descendingDifferent")) {
//        } else if (propertyName.equals("descendingIntervals")) {
//        } else if (propertyName.equals("descendingIntervalsAsString")) {
//        } else if (propertyName.equals("description")) {
        } else if (propertyName.equals("intervals")) {
            int[] a1 = c1.getIntervals();
            int[] a2 = c2.getIntervals();
            return compareArrays(a1, a2);
//        } else if (propertyName.equals("intervalsAsString")) {
//        } else if (propertyName.equals("octave")) {
//        } else if (propertyName.equals("spelling")) {
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
	    //logger.debug(Arrays.toString(a1));
		if (a1.length > a2.length) {
			return 1;
		} else if (a1.length < a2.length) {
			return -1;
		}
		if (a1.length == a2.length) {
			outer: for (int i = 0; i < a2.length; i++) {
				while (a1[i] == a2[i]) {
					continue outer;
				}

				if (a1[i] < a2[i]) {
					return -1;
				} else {
					return 1;
				}
			}
		}
		return 0;
	}

//    static class IntegerComparator implements Comparator<Integer> {
//        public int compare(Integer a, Integer b) {
//            return b.compareTo(a);
//        }
//    }

    public static void main(String[] args) {
        List<Scale> scales = null;
        // List<Scale> scales = new ArrayList<Scale>();
        scales = ScaleFactory.getAll();
        // Scale s = ScaleFactory.getScaleByName("Raga Hamsa Vinodini");
        // scales.add(s);
        // System.err.println("created " + s);
        // s = ScaleFactory.getScaleByName("Harmonic Minor");
        // scales.add(s);
        // System.err.println("created " + s);
        //
        // s = ScaleFactory.getScaleByName("Hungarian Gypsy");
        // scales.add(s);
        // System.err.println("created " + s);
        //
        //
        // s = ScaleFactory.getScaleByName("Persian");
        // scales.add(s);
        // System.err.println("created " + s);

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
