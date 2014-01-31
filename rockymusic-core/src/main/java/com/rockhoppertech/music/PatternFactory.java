package com.rockhoppertech.music;

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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class PatternFactory {
    private static final Logger logger = LoggerFactory
            .getLogger(PatternFactory.class);

    /**
     * Calculate all the patterns of a given size up to a limit.
     * 
     * Example: limit 3, size 2:
     * 
     * <pre>
     *     0 0 
     *     0 1 
     *     0 2 
     *     1 0 
     *     1 1 
     *     1 2 
     *     2 0 
     *     2 1 
     *     2 2
     * </pre>
     * 
     * @param limit
     *            the upper number of unique values.
     * @param size
     *            the length of each pattern.
     * @param weedOutRepeats
     *            remove patterns with consecutive values e.g. 0,0
     * @return a List of integer arrays. Each array contains a pattern.
     */
    public static List<int[]> getPatterns(int limit, int size,
            boolean weedOutRepeats) {
        int num = (int) Math.pow(limit, size);
        logger.debug("number of patterns is {}", num);
        List<int[]> patterns = new ArrayList<int[]>(num);
        for (int i = 0; i < num; i++) {
            int[] a = new int[size];
            int count = size - 1;
            int b = 0;
            while (count >= 0) {
                a[count] = (int) ((i) / Math.pow(limit, b++) % limit);
                count--;
            }

            if (weedOutRepeats) {
                if (!hasRepeats(a)) {
                    patterns.add(a);
                }
            } else {
                patterns.add(a);
            }
        }
        return patterns;
    }

    public static List<int[]> getPatterns(int limit, int size) {
        return getPatterns(limit, size, false);
    }

    /**
     * Checks to determine if there are consecutive values. e.g. 0,0 1,1 2,2,2,2
     * etc.
     * 
     * @param a
     *            an array
     * @return if there are repeats
     */
    public static boolean hasRepeats(int[] a) {
        boolean ret = false;
        for (int x = 1; x < a.length; x++) {
            if (a[x] == a[x - 1]) {
                return true;
            }
        }
        return ret;
    }

}
