/**
 * 
 */
package com.rockhoppertech.music.chord;

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


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ChordComparator implements Comparator<Chord> {
    private static final Logger logger = LoggerFactory
            .getLogger(ChordComparator.class);

    /*
     * Sorts by interval length and then individual intervals
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Chord o1, Chord o2) {
        if (o1 == null || o2 == null)
            return 0;
        int[] a1 = o1.getIntervals();
        int[] a2 = o2.getIntervals();
        if (a1.length > a2.length) {
            return 1;
        } else if (a1.length < a2.length) {
            return -1;
        }
        if (a1.length == a2.length) {
            if (logger.isDebugEnabled()) {
                logger.debug("length =" + a1.length);
            }
            outer: for (int i = 0; i < a2.length; i++) {
                while (a1[i] == a2[i])
                    continue outer;

                if (a1[i] < a2[i]) {
                    logger.debug(a1[i] + "<" + a2[i]);
                    return -1;
                } else {
                    logger.debug(a1[i] + ">" + a2[i]);
                    return 1;
                }
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        List<Chord> chords = new ArrayList<Chord>();
        chords = ChordFactory.getAll();

        Collections.sort(chords, new ChordComparator());
        System.err.println("Done sorting");
        for (Chord sc : chords) {
            System.err.println(sc.getDescription());
            System.err.println(sc.getRoot());
            System.err.println(sc.getSymbol());
            System.err.println(sc.getSpelling());
            System.err.println(sc.getIntervalstoString());
            System.err.println();
        }
    }

}
