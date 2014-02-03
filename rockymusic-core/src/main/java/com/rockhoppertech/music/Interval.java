package com.rockhoppertech.music;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.scale.Scale;

/**
 * Utilities to convert between scale or chord spellings, intervals, degrees.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class Interval {
    private static final Logger logger = LoggerFactory
            .getLogger(Interval.class);
    public static final int UNISON = 0;
    public static final int MINOR_SECOND = 1;
    public static final int MAJOR_SECOND = 2;
    public static final int MINOR_THIRD = 3;
    public static final int MAJOR_THIRD = 4;
    public static final int PERFECT_FOURTH = 5;
    public static final int AUGMENTED_FOURTH = 6;
    public static final int DIMINISHED_FIFTH = 6;
    public static final int PERFECT_FIFTH = 7;
    public static final int AUGMENTED_FIFTH = 8;
    public static final int MINOR_SIXTH = 8;
    public static final int MAJOR_SIXTH = 9;
    public static final int DIMINISHED_SEVENTH = 9;
    public static final int MINOR_SEVENTH = 10;
    public static final int MAJOR_SEVENTH = 11;
    public static final int OCTAVE = 12;

    public static final int MINOR_NINTH = 13;
    public static final int MAJOR_NINTH = 14;
    // public static final int MINOR_ELEVENTH = 13;
    public static final int MAJOR_ELEVENTH = 17;
    public static final int MINOR_THIRTEENTH = 20;
    public static final int MAJOR_THIRTEENTH = 21;

    private static Map<String, Integer> spellings =
            new HashMap<String, Integer>();
    private static Map<String, Integer> scaleSpellings =
            new LinkedHashMap<String, Integer>();
    private static List<String> names = new ArrayList<String>();
    private static List<String> roman = new ArrayList<String>();
    static {
        // chord spellings
        Interval.spellings.put("1", 0);
        Interval.spellings.put("b2", 1);
        Interval.spellings.put("2", 2);
        Interval.spellings.put("b3", 3);
        Interval.spellings.put("#2", 3);
        Interval.spellings.put("3", 4);
        Interval.spellings.put("4", 5);
        Interval.spellings.put("#4", 6);
        Interval.spellings.put("b5", 6);
        Interval.spellings.put("5", 7);
        Interval.spellings.put("#5", 8);
        Interval.spellings.put("b6", 8);
        Interval.spellings.put("6", 9);
        Interval.spellings.put("#6", 10);
        Interval.spellings.put("bb7", 9);
        Interval.spellings.put("b7", 10);
        Interval.spellings.put("7", 11);
        Interval.spellings.put("b8", 11);
        Interval.spellings.put("12", 12); // TODO not used in chords but in
                                          // scales. huh? these are chord
                                          // spellings.
        Interval.spellings.put("b9", 13);
        Interval.spellings.put("9", 14);
        Interval.spellings.put("#9", 15);
        Interval.spellings.put("b11", 16);
        Interval.spellings.put("11", 17);
        Interval.spellings.put("#11", 18);
        Interval.spellings.put("b13", 20);
        Interval.spellings.put("13", 21);

        // scaleSpellings.put("1", 0);
        Interval.scaleSpellings.put("1", 0);
        Interval.scaleSpellings.put("b2", 1);
        Interval.scaleSpellings.put("2", 2);
        Interval.scaleSpellings.put("b3", 3);
        Interval.scaleSpellings.put("#2", 3);
        Interval.scaleSpellings.put("3", 4);
        Interval.scaleSpellings.put("4", 5);
        Interval.scaleSpellings.put("#4", 6);
        Interval.scaleSpellings.put("b5", 6);
        Interval.scaleSpellings.put("5", 7);
        Interval.scaleSpellings.put("#5", 8);
        Interval.scaleSpellings.put("b6", 8);
        Interval.scaleSpellings.put("6", 9);
        Interval.scaleSpellings.put("#6", 10);
        Interval.scaleSpellings.put("bb7", 9);
        Interval.scaleSpellings.put("b7", 10);
        Interval.scaleSpellings.put("7", 11);
        Interval.scaleSpellings.put("8", 12);
        Interval.scaleSpellings.put("b9", 13);
        Interval.scaleSpellings.put("9", 14);
        Interval.scaleSpellings.put("#9", 15);
        Interval.scaleSpellings.put("b11", 16);
        Interval.scaleSpellings.put("11", 17);
        Interval.scaleSpellings.put("#11", 18);
        Interval.scaleSpellings.put("b13", 20);
        Interval.scaleSpellings.put("13", 21);

        // not used yet
        Interval.names.add("Unison");
        Interval.names.add("Minor second");
        Interval.names.add("Major second");
        Interval.names.add("Minor third");
        Interval.names.add("Major third");
        Interval.names.add("Perfect fourth");
        Interval.names.add("Tritone");
        Interval.names.add("Perfect fifth");
        Interval.names.add("Minor sixth");
        Interval.names.add("Major sixth");
        Interval.names.add("Minor seventh");
        Interval.names.add("Major seventh");
        Interval.names.add("Octave");
        Interval.names.add("Minor ninth");
        Interval.names.add("Major ninth");
        Interval.names.add("Minor tenth");
        Interval.names.add("Major tenth");
        Interval.names.add("Eleventh");
        Interval.names.add("Augmented eleventh");
        // made this up
        Interval.names.add("'Twelfth'");
        Interval.names.add("Minor thirteenth");
        Interval.names.add("Major thirteenth");
        // there's no real name for the next two
        Interval.names.add("Augmented thirteenth");
        Interval.names.add("'Fourteenth'");
        Interval.names.add("2 Octaves");

        // TODO use these
        Interval.roman.add("I");
        Interval.roman.add("ii");
        Interval.roman.add("II");
        Interval.roman.add("iii");
        Interval.roman.add("III");
        Interval.roman.add("IV");
        Interval.roman.add("vo");
        Interval.roman.add("V");
        Interval.roman.add("V+");
        Interval.roman.add("VI");
        Interval.roman.add("vii");
        Interval.roman.add("VII");
        Interval.roman.add("I");
    }

    /**
     * Get the name of an interval.
     * 
     * @param i
     *            an interval
     * @return the interval name or "Unknown"
     */
    public static String getName(final int i) {
        if (i >= names.size()) {
            return "Unknown";
        }
        return names.get(i);
    }

    /**
     * Intervals in relation to root.
     * 
     * <pre>
     * {@code
     *  actual = Interval.degreesToIntervals("1 2 3 4 5 6 7");
        expected = new int[] { 2, 4, 5, 7, 9, 11 };
     * }
     * </pre>
     * 
     * @param spelling
     *            is a chord spelling
     * @return an array of intervals
     */
    public static int[] degreesToIntervals(final String spelling) {
        Interval.logger.debug("spelling: \'" + spelling + "\'");
        // \s is A whitespace character: [ \t\n\x0B\f\r]
        // see java.util.regex.Pattern
        final String[] degrees = spelling.split("\\s");
        for (final String degree : degrees) {
            Interval.logger.debug("degrees: " + degree);
        }
        // given "1 3 5" split will give an array
        // with 1 in index 0 3 in index 2 etc

        final int[] intervals = new int[degrees.length - 1];
        // all of the degrees start with 1 so we skip that one
        // for 1 3 5 we get 4 7 for the intervals
        for (int i = 0; i < intervals.length; i++) {
            intervals[i] = Interval.getSpelling(degrees[i + 1]);
            Interval.logger.debug("intervals {}", intervals[i]);
        }
        return intervals;
    }

    /**
     * Calculate the intervals between the specified degrees.
     * 
     * <pre>
     * {@code
        actual = Interval.getIntervalsFromDegreeString("1 2 3 4 5 6 7");
        expected = new int[] { 2, 2, 1, 2, 2, 2 };
       }
        </pre>
     * 
     * Input can be comma or whitespace delimited.
     * 
     * @param s
     *            a scale spelling
     * @return an array of intervals
     */
    public static int[] getIntervalsFromDegreeString(final String s) {

        final Scanner scanner = new Scanner(s);
        if (s.indexOf(',') != -1) {
            scanner.useDelimiter(",");
            Interval.logger.debug("comma delimited");
        }

        final List<Integer> pitches = new ArrayList<Integer>();
        while (scanner.hasNext()) {
            final String string = scanner.next().trim();
            logger.debug("degreestring: " + string);

            int pc = Interval.getScaleSpelling(string);
            pc++;
            pitches.add(pc);
        }
        final int size = pitches.size();
        final int[] array = new int[size - 1];
        for (int i = 1; i < size; i++) {
            final int val = pitches.get(i) - pitches.get(i - 1);
            logger.debug("val {}", val);
            array[i - 1] = val;
        }
        scanner.close();
        return array;
    }

    /**
     * Obtain the intervals from a string containing pitches. Can be space or
     * comma delimited.
     * 
     * @param s
     *            a pitch string
     * @return an array of intervals
     */
    public static int[] getIntervalsFromPitchString(final String s) {

        final Scanner scanner = new Scanner(s);
        if (s.indexOf(',') != -1) {
            scanner.useDelimiter(",");
            Interval.logger.debug("comma delimited");
        }

        final List<Integer> pitches = new ArrayList<Integer>();
        while (scanner.hasNext()) {
            final String string = scanner.next().trim();
            Interval.logger.debug("pitchstring: " + string);
            final int pc = PitchFormat.stringToPC(string);
            pitches.add(pc);
        }
        final int size = pitches.size();
        final int[] array = new int[size - 1];
        for (int i = 1; i < size; i++) {
            final int val = pitches.get(i) - pitches.get(i - 1);
            Interval.logger.debug("val {}", val);
            array[i - 1] = val;
        }
        scanner.close();
        return array;
    }

    /**
     * Given a scale spelling, calculate the intervals between each degree.
     * 
     * <pre>
      {@code 
        Scale scale = ScaleFactory.getScaleByName("Chromatic");
        String spelling = scale.getSpelling();
        // 1 b2 2 b3 3 4 #4 5 #5 6 #6 7 8
        actual = Interval.getIntervalsFromScaleSpelling(spelling);
        expected = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
        
      }
     * Can be space or comma delimited.
     * 
     * @param s
     *            a scale spelling
     * @return an array of intervals
     */
    public static int[] getIntervalsFromScaleSpelling(final String s) {
        final Scanner scanner = new Scanner(s);
        if (s.indexOf(',') != -1) {
            scanner.useDelimiter(",");
        }

        final List<Integer> intervals = new ArrayList<Integer>();
        while (scanner.hasNext()) {
            final String interval = scanner.next().trim();
            intervals.add(Interval.getScaleSpelling(interval));
        }
        final int size = intervals.size();
        final int[] array = new int[size - 1];
        for (int i = 1; i < size; i++) {
            final int val = intervals.get(i) - intervals.get(i - 1);
            Interval.logger.debug("val {}", val);
            array[i - 1] = val;
        }
        scanner.close();
        return array;
    }

    /**
     * Degrees to intervals. e.g. Input 1 3 5 will give you 4,3. The degree can
     * be raised or lowered with a prefixed b or #.
     * 
     * <pre>
      {@code
      int[] actual = Interval.getIntervalsFromSpelling("1 2 3 4 5 6 7");
      int[] expected = new int[] { 2, 2, 1, 2, 2, 2 };
      actual = Interval.getIntervalsFromSpelling("1 b2 3 #4 5 6 7");
      expected = new int[] { 1, 3, 2, 1, 2, 2 };
       }        
        </pre>
     * 
     * Can be space or comma delimited.
     * 
     * @param s
     *            a chord spelling
     * @return an array of intervals
     */
    public static int[] getIntervalsFromSpelling(final String s) {

        // if the string has commas as delimiters
        final Scanner scanner = new Scanner(s);
        if (s.indexOf(',') != -1) {
            scanner.useDelimiter(",");
        }

        final List<Integer> intervals = new ArrayList<Integer>();
        while (scanner.hasNext()) {
            final String interval = scanner.next().trim();
            intervals.add(Interval.getSpelling(interval));
        }
        final int size = intervals.size();
        final int[] array = new int[size - 1];
        for (int i = 1; i < size; i++) {
            final int val = intervals.get(i) - intervals.get(i - 1);
            logger.debug("val {}", val);
            array[i - 1] = val;
        }
        scanner.close();
        return array;
    }

    /**
     * Calculate relative intervals.
     * 
     * Wraps integers in a string into an array. Non specific. The input
     * integers could be midinumbers, durations etc.
     * <p>
     * Can be space or comma delimited.
     * 
     * <pre>
     * {@code 
       int[] actual = Interval.getIntervalsFromString("2 2 1 2");
       int[] expected = { 0, -1, 1 };
       }
     * </pre>
     * 
     * @param s
     *            a String containing integers
     * @return an int array of the same integers
     */
    public static int[] getIntervalsFromString(final String s) {

        final Scanner scanner = new Scanner(s);
        if (s.indexOf(',') != -1) {
            scanner.useDelimiter(",");
        }

        final List<Integer> ints = new ArrayList<Integer>();
        while (scanner.hasNext()) {
            final String interval = scanner.next().trim();
            ints.add(Integer.parseInt(interval));
        }
        final int size = ints.size();
        // final int[] array = new int[size];
        // for (int i = 0; i < array.length; i++) {
        // array[i] = ints.get(i).intValue();
        // }

        final int[] array = new int[size - 1];
        for (int i = 1; i < size; i++) {
            final int val = ints.get(i) - ints.get(i - 1);
            logger.debug("val {}", val);
            array[i - 1] = val;
        }
        scanner.close();
        return array;
    }

    /**
     * Return intervals in relation to first int.
     * 
     * <p>
     * Non domain specific: input is just a string of ints.
     * <p>
     * Can be space or comma delimited.
     * 
     * @param s
     *            a String that can be parsed into ints
     * @return an int[] array of absolute intervals
     */
    public static int[] getAbsoluteIntervalsFromString(final String s) {

        final Scanner scanner = new Scanner(s);
        if (s.indexOf(',') != -1) {
            scanner.useDelimiter(",");
        }

        final List<Integer> ints = new ArrayList<Integer>();
        while (scanner.hasNext()) {
            final String interval = scanner.next().trim();
            ints.add(Integer.parseInt(interval));
        }
        final int size = ints.size();
        final int[] array = new int[size - 1];
        int root = ints.get(0);
        for (int i = 1; i < size; i++) {
            final int val = ints.get(i) - root;
            array[i - 1] = val;
        }
        scanner.close();

        return array;
    }

    /**
     * Get the key from the map.
     * 
     * @param interval
     *            an interval
     * @return the spelling or null.
     */
    public static String getScaleSpelling(final int interval) {
        // TODO not really.
        for (final Entry<String, Integer> e : Interval.scaleSpellings
                .entrySet()) {
            if (e.getValue() == interval) {
                return e.getKey();
            }
        }
        return null;
    }

    /**
     * Get the number of half steps in a degree from root. Input is a scale
     * degree spelling.
     * 
     * <pre>
     * {@code 
      actual = Interval.getScaleSpelling("#4");
      expected = 6;
     * }
     * </pre>
     * 
     * @param s
     *            a scale spelling
     * @return the number of half steps in the spelling
     */
    public static int getScaleSpelling(final String s) {
        logger.debug(s);
        return Interval.scaleSpellings.get(s);
    }

    /**
     * Return the spelling for an interval.
     * 
     * <pre>
     * {@code 
      String actual = Interval.getSpelling(5);
      String expected = "4";
      }
     * </pre>
     * 
     * @param interval
     *            an interval in half steps
     * @return null or the chord spelling for an interval
     */
    public static String getSpelling(final int interval) {
        for (final Entry<String, Integer> e : Interval.spellings.entrySet()) {
            if (e.getValue() == interval) {
                return e.getKey();
            }
        }
        return null;
    }

    /**
     * Get the number of half steps in a degree from root. Input is an interval
     * degree spelling. <code>getSpelling("13")</code> would yield the number of
     * half steps in a thirteenth.
     * 
     * <pre>
     {@code 
      actual = Interval.getSpelling("5");
      expected = 7;
     }
     * </pre>
     * 
     * @param s
     *            A chord spelling
     * @return the number of half steps
     */
    public static int getSpelling(final String s) {
        logger.debug(s);
        return Interval.spellings.get(s);
    }

    /**
     * Turn intervals into Scale degrees.
     * 
     * 
     * @see com.rockhoppertech.music.scale.Scale
     * @param intervals
     *            the intervals to turn into degrees
     * @return an array of degrees
     */
    public static int[] intervalsToDegrees(final int[] intervals) {
        final int[] degrees = new int[intervals.length + 1];
        // degrees[0] = 1;
        for (int i = 0; i < intervals.length; i++) {
            degrees[i + 1] = degrees[i] + intervals[i];
        }
        return degrees;
    }

    /**
     * Can turn the intervals in a scale into their scale degrees. You can
     * generate patterns.
     * 
     * @see com.rockhoppertech.music.scale.ScaleFactory#getTracktPattern(Scale,
     *      int[], int, int, int, double, boolean, double, boolean)
     * @param intervals
     *            an array of intervals
     * @param nOctaves
     *            how many octaves to use
     * @return an array of degrees
     */
    public static int[] intervalsToDegrees(final int[] intervals,
            final int nOctaves) {
        final int[] roots = { 0, 12, 24, 36, 48, 60, 72, 84, 96, 108, 120 };
        final List<Integer> list = new ArrayList<Integer>();
        list.add(0);
        for (int octave = 0; octave < nOctaves; octave++) {
            int previous = roots[octave];
            Interval.logger.debug("p " + previous);
            for (final int interval : intervals) {
                final int n = previous + interval;
                list.add(n);
                previous = n;
                Interval.logger.debug("pp " + previous);
                Interval.logger.debug("int " + interval);
            }
        }

        Integer[] ia = new Integer[list.size()];
        ia = list.toArray(ia);
        final int[] degrees = new int[ia.length];
        for (int i = 0; i < ia.length; i++) {
            Interval.logger.debug("degree " + ia[i]);
            degrees[i] = ia[i].intValue();
        }
        return degrees;
    }

    /**
     * <p>
     * Spell a chord.
     * </p>
     * 
     * @param intervals
     *            an array of intervals
     * @return a chord spelling
     */
    public static String spell(final int[] intervals) {
        final StringBuffer spelling = new StringBuffer();
        for (final int interval : intervals) {
            spelling.append(Interval.getSpelling(interval));
            spelling.append(' ');
        }
        return spelling.toString();
    }

    /**
     * Spell a scale.
     * 
     * @param intervals
     *            an array of intervals
     * @return a String with the scale spelling
     */
    public static String spellScale(final int[] intervals) {
        final StringBuffer spelling = new StringBuffer();
        for (final int interval : intervals) {
            spelling.append(Interval.getScaleSpelling(interval));
            spelling.append(' ');
        }
        return spelling.toString();
    }

    /**
     * Default constructor.
     */
    public Interval() {

    }

}
