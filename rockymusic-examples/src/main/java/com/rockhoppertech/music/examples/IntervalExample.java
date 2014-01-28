package com.rockhoppertech.music.examples;

/*
 * #%L
 * Rocky Music Examples
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

import javax.swing.JOptionPane;

import com.rockhoppertech.music.Interval;

public class IntervalExample {

    public static void main(final String[] args) {
        final String[] s = { "getIntervalsFromScaleSpelling",
                "degreesToIntervals",
                "getIntervalsFromDegreeString", "getIntervalsFromPitchString",
                "getIntervalsFromString", "intervalsToDegrees" };
        String v = null;

        v = (String) JOptionPane.showInputDialog(null, "Choose an operation",
                "Possible Operations", JOptionPane.QUESTION_MESSAGE, null, s,
                s[0]);

        if (v.equals(s[0])) {
            v = JOptionPane.showInputDialog("Enter a scale spelling");
            final int[] a = Interval.getIntervalsFromScaleSpelling(v);
            JOptionPane.showMessageDialog(null, Interval.arrayToString(a));
        } else if (v.equals(s[1])) {
            v = JOptionPane
                    .showInputDialog("Enter a chord spelling in degrees");
            final int[] a = Interval.degreesToIntervals(v);
            JOptionPane.showMessageDialog(null, Interval.arrayToString(a));
        } else if (v.equals(s[2])) {
            v = JOptionPane
                    .showInputDialog("Enter a chord spelling in degrees");
            final int[] a = Interval.getIntervalsFromDegreeString(v);
            JOptionPane.showMessageDialog(null, Interval.arrayToString(a));
        } else if (v.equals(s[3])) {
            v = JOptionPane.showInputDialog("Enter pitches");
            final int[] a = Interval.getIntervalsFromPitchString(v);
            JOptionPane.showMessageDialog(null, Interval.arrayToString(a));
        } else if (v.equals(s[4])) {
            v = JOptionPane.showInputDialog("Enter a series of ints");
            final int[] a = Interval.getIntervalsFromString(v);
            JOptionPane.showMessageDialog(null, Interval.arrayToString(a));
        } else if (v.equals(s[5])) {
            v = JOptionPane.showInputDialog("Enter a series of ints");

            final int[] a = Interval.intervalsToDegrees(
                    Interval.getIntervalsFromString(v),
                    1);
            JOptionPane.showMessageDialog(null, Interval.arrayToString(a));
        }

    }

    /**
     * <p>
     * given a string like 1, 2, 3, 4, b5, b6, b7 return the intervals like
     * 2,2,1,1,2,2
     * </p>
     * 
     * @param args
     */
    public static void oldmain(final String[] args) {
        final String s = JOptionPane.showInputDialog("Enter pitches");
        final int[] array = Interval.getIntervalsFromSpelling(s);
        // logger.debug("array {}", array);

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(',');
            }
            // logger.debug("array i {}", array[i]);
        }
        /*
         * 
         * = new Scanner(s); scanner.useDelimiter(","); List<Integer> intervals
         * = new ArrayList<Integer>(); while (scanner.hasNext()) { String
         * interval = scanner.next().trim(); logger.debug("interval " +
         * interval); logger.debug("spelling " + getSpelling(interval));
         * intervals.add(getSpelling(interval)); } int size = intervals.size();
         * for (int i = 1; i < size; i++) { int val = intervals.get(i) -
         * intervals.get(i - 1); sb.append(val); if (i < size - 1)
         * sb.append(','); logger.debug(val); }
         */
        JOptionPane.showMessageDialog(null, sb.toString());
        // logger.debug(sb.toString());

        /*
         * sb = new StringBuilder(); for (Integer i : intervals) {
         * sb.append(i).append(' '); } logger.debug(sb.toString());
         */
    }

}
